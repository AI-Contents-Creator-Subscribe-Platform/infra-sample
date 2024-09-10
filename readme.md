## 개발

---

docker-compose up -d
./gradlew 


## 요구사항

- 아래 API 목록을 구현 합니다. 
  - 광고 등록 API 
  - 광고 조회 API 
  - 광고 참여 API 
  - 광고 참여 이력 조회 API 
- 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 문제가 없어야 합니다. 
- 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.

## 평가항목

- 프로젝트 구성 방법 및 관련된 시스템 아키텍쳐 설계 방법이 적절한가? 
- 요구사항을 잘 이해하고 구현하였는가? 
- 작성한 어플리케이션 코드의 가독성 좋고 의도가 명확한가? 
- 작성한 테스트 코드는 적절한 범위의 테스트를 수행하고 있는가? (예. 유닛/통합 테스트 등)
- 어플리케이션은 다량의 트래픽에도 무리가 없도록 효율적으로 작성되었는가?



## 작성사항

- 설계 내용과 설계의 이유
- 핵심 문제해결 전략 및 분석한 내용

## 시스템 설계를 위한 대략적인 계산 (Back-of-the-envelope Estimation)

---
데이터 근거: ${프로젝트경로}/kakaopay_AD_product_2406.pdf

**Users**

- 사용자 구분: 일반 사용자 / 광고 가맹점
  - 일반 사용자: 광고 조회, 광고 참여, 광고
  - 광고 가맹점: 광고 등록
- 전체 수: 4,000만명 / 100만개
- MAU: 2,400만명 / 50만개 (높은 활성 비율 50% 기준) 
- DAU
  - 일반 사용자: 디스플레이 전체 광고 상품 단가표 기준(페이홈>홈 기준) 
    - 아이콘(고정형)/아이콘(확장형)/전면팝업/머니박스 상단/브랜딩보드
    - (1,500만 + 1,500만 + 150만 + 800만 + 1,500만) / 7(주 단위) 
    - = 7,785,714 ≈ 800만
  - 광고 가맹점: 높은 활성 비율(20% 기준)
    - 50만 * 20% = 10만

**TPS(Transaction Per Second)**

- 하루 초: 86,400초 
- 일반 사용자
  - 사용자는 하루에 평균 페이지 방문 횟수: 2회 (카카오페이 화면 방문 가정 수치)
  - 페이지 내 광고 상호작용 3(광고 조회, 광고 참여, 광고 참여 조회 이력) 
  - TPS = DAU / 86,400 * 2(평균 페이지 방문 횟수) * 3(광고 상호작용)
    - 800만 / 86,400 * 2 * 3 
    - = 556 ≈ 600 TPS 
- 광고 가맹점
  - 광고 가맹점은 하루에 평균 페이지 방문 횟수: 1회 
  - 광고 상호작용 1(광고 등록)
  - TPS = DAU / 86,400 * 1(평균 페이지 방문 횟수) * 1(광고 상호작용)
  - 10만 / 86,400 * 1 * 1
  - = 1.16 ≈ 1 TPS

**QPS(Query Per Second)**

- 한 광고 트랜잭션당 2번의 쿼리가 발생한다고 가정(공통)
- 총 쿼리수는 TPS * 쿼리 수
- 일반 사용자
  - QPS = TPS * 2
    - 600 TPS * 2 = 1,200 QPS
  - Peak QPS = QPS * 2
    - 1,200 QPS * 2 = 2,400 QPS
- 광고 가맹점
  - QPS = TPS * 2
    - 1 TPS * 2 = 2 QPS
  - Peak QPS = QPS * 2
    - 2 QPS * 2 = 4 QPS

**Storage(DB)**

- 광고, 광고 참여 이력(부수적인 데이터 무시)
  - 광고
    - Size: 500 bytes(가정)
    - 등록 빈도수: 가맹점 TPS = 1 TPS  
      - day ≈ 86,400 rows / 40 MB
      - year ≈ 3,200만 rows / 1400 TB
  - 광고 참여 이력
    - Size: 100 bytes(가정)
    - 등록 빈도수: 일반 사용자 TPS / 3 = 200 TPS
      - day ≈ 1,700만 rows / 2 GB
      - year ≈ 63억 rows / 11,000 PB


**정리** 

| 구분            | 사용자       | 가맹점      |
|---------------|-----------|----------|
| 전체 수          | 4,000만명   | 100만개    |
| MAU           | 2,400만명   | 50만개     |
| DAU           | 800만명     | 10만개     |
| TPS           | 600 TPS   | 1 TPS    |
| QPS           | 1,200 QPS | 2 QPS    |
| Peak QPS      | 2,400 QPS | 4 QPS    |
| ROW(Day)      | 1,700만    | 86,400개  |
| ROW(Year)     | 63억       | 3,200만개  |
| STORAGE(Day)  | 2 GB      | 40 MB    |
| STORAGE(Year) | 11,000 PB | 1,400 TB |


## 시스템 아키텍처

---
![ads_archi](https://github.com/user-attachments/assets/733d9fe3-455c-4616-a1b4-26c19a57b009)
- 사전 과제 미포함 요소: API Gateway, Load Balancer, CDN
- 사전 과제 구성 변경 요소: API Server, Consumer 서버는 하나의 프로젝트로 구동

### 설계 고려 사항

1. 캐시 계층 도입: 광고 조회와 같은 빈번한 데이터 조회 API에 대한 응답 시간을 줄이기 위해 캐싱 활용
2. 서비스 별 데이터베이스 구분
   3. 광고 서비스: 사용자 대비 적은 가맹점 데이터, 참여 횟수, 기간 등 데이터 일관성 유지 필요하여 ACID 트랜잭션 지원하는 RDBMS 활용  
   4. 사용자/광고 참여 서비스: 대량의 트래픽, 데이터를 고려하여, 대용량 데이터 및 수평적 확장성을 고려한 NoSQL
3. 메시지 큐 및 비동기 처리
   4. 여러 사용자가 동시에 광고에 참여할 경우, 락을 사용자 서비스에서 적용하면 서비스 지연 발생.
   5. 광고 참여 발생 이벤트를 큐에 넣고, 이후 처리를 비동기로 수행하여 API 응답 및 서비스 가용성 개선

### 설계 미포함 고려 사항
대용량 데이터
- 조회를 위한 인덱스는 어떻게 ?
  - 동적 파티셔닝 ?
- 저장 공간은 어떻게 ? (사용자 1년: 63억 row / 11,000PB , 가맹점 1년: 3,200만개 / 1,400 TB)
  - 파티셔닝? 동적 테이블?



## 도메인 

### 테이블


### **SQL Schema (광고 테이블 생성)**

```sql
CREATE TABLE ads (
    ad_id varchar(50) PRIMARY KEY,  -- 광고 ID (Unique ID, 자동 증가)
    reward_amount DECIMAL(10, 2) NOT NULL, -- 광고 참여시 적립 액수
    participation_limit INT NOT NULL,      -- 광고 참여 가능 횟수
    current_participation_limit INT NOT NULL,      -- 현재 광고 참여 가능 횟수
    display_start_date DATE NOT NULL,      -- 광고 노출 시작 기간
    display_end_date DATE NOT NULL,        -- 광고 노출 종료 기간
);

CREATE TABLE ads_content (
   content_id INT AUTO_INCREMENT PRIMARY KEY,  -- 콘텐츠 ID (Unique ID, 자동 증가)
   ad_id varchar(50) NOT NULL,                         -- 광고 ID (외래 키)
   ad_name VARCHAR(255) NOT NULL,              -- 광고명
   ad_text VARCHAR(500),                       -- 광고 문구
   image_url VARCHAR(500),                     -- 광고 이미지 URL
   UNIQUE (ad_name),                           -- 광고명 중복 불가 제약 조건
   FOREIGN KEY (ad_id) REFERENCES ads(ad_id)   -- 광고 테이블과의 관계 설정
);

CREATE INDEX idx_ads_participation_date
  ON ads (current_participation_limit, display_start_date, display_end_date);

```

### **설명**:
1. **ad_id**: 광고의 고유 ID로, `AUTO_INCREMENT`를 사용해 자동으로 증가하는 정수형 값입니다. `PRIMARY KEY`로 설정하여 유니크한 값이 보장됩니다.
2. **ad_name**: 광고명은 중복을 허용하지 않기 때문에 `UNIQUE` 제약 조건을 추가했습니다.
3. **reward_amount**: 광고 참여 시 적립되는 금액은 소수점 이하 두 자리까지 표시할 수 있는 `DECIMAL` 타입을 사용했습니다.
4. **participation_limit**: 광고에 참여 가능한 횟수를 정수형으로 정의합니다.
5. **ad_text**: 광고에 표시되는 문구는 최대 500자의 문자열로 정의했습니다.
6. **image_url**: 광고 이미지의 URL을 최대 500자의 문자열로 정의했습니다.
7. **display_start_date** 및 **display_end_date**: 광고가 노출될 기간을 각각 `DATE` 형식으로 저장합니다. 기간 동안만 광고가 노출됩니다.
8. **UNIQUE (ad_name)**: 광고명은 중복될 수 없기 때문에 고유 제약 조건을 추가했습니다.


MongoDB에서 **사용자 광고 참여 이력** 테이블을 구성하는 방법은 각 사용자의 광고 참여 이력을 문서 형식으로 저장하는 것입니다. 아래는 제안하는 NoSQL 기반의 MongoDB 컬렉션 스키마입니다.

### **MongoDB Schema (광고 참여 이력 컬렉션)**

```json
{
    "user_id": "string",           // 유저 ID
    "ad_id": "string",             // 광고 ID
    "ad_name": "string",           // 광고명
    "reward_amount": "decimal",    // 적립 액수
    "participation_time": "date"   // 광고 참여 시각
}
```

### **MongoDB 컬렉션 구조**:

MongoDB에서 이 스키마에 맞는 문서를 다루는 방법은 아래와 같이 `insert` 명령으로 새로운 참여 이력을 추가하는 방식입니다.

### **예시 문서 구조**:

```json
{
    "user_id": "user12345",
    "ad_id": "ad9876",
    "ad_name": "Great Discount Campaign",
    "reward_amount": 1500.50,
    "participation_time": ISODate("2024-09-09T14:00:00Z")
}
```

### **설명**:
1. **user_id**: 유저의 고유 식별자로, 문자열 형식입니다. 각 사용자의 고유 ID가 저장됩니다.
2. **ad_id**: 참여한 광고의 고유 ID로, 문자열로 저장됩니다.
3. **ad_name**: 광고의 이름을 저장합니다. 이는 가독성을 위해 광고명도 함께 저장합니다.
4. **reward_amount**: 참여 시 적립되는 금액입니다. 소수점 이하 값까지 저장할 수 있도록 `decimal` 형식으로 설정합니다.
5. **participation_time**: 광고 참여 시각을 ISODate 형식으로 저장합니다. MongoDB에서 날짜/시간 정보는 `ISODate` 형식을 권장합니다.

### **장점**:
- **확장성**: NoSQL(MongoDB) 구조는 자유롭기 때문에, 이후 추가적인 필드를 쉽게 추가할 수 있습니다.
- **빠른 조회**: 사용자 ID, 광고 ID로 인덱스를 설정하면 참여 이력을 빠르게 조회할 수 있습니다.

### **인덱스 설정**:
효율적인 조회를 위해 인덱스를 설정할 수 있습니다.

```javascript
db.ad_participation_history.createIndex({ user_id: 1, participation_time: -1 });
```

이 인덱스는 사용자 ID별로 이력을 빠르게 조회하고, 최신 참여 시각 순으로 정렬된 결과를 제공합니다.



이 테이블은 광고 서비스에서 사용될 기본적인 구조로, 다양한 광고 데이터 및 상태를 관리할 수 있습니다.

사용자의 광고 참여 및 광고 참여 이력 조회 API를 구현할 때 **효율적인 MongoDB 인덱스**를 설정하여 성능을 최적화하는 것이 중요합니다. 특히, 사용자 ID와 광고 ID를 자주 조회하게 되므로 이에 맞는 인덱스를 설정해야 합니다.

### 1. **MongoDB 인덱스 개념**
- **단일 필드 인덱스**: 하나의 필드에 대해 빠르게 조회 가능.
- **복합 인덱스**: 여러 필드에 대해 효율적으로 조회 가능. 주로 자주 사용하는 조건문 필드에 대해 생성.
- **TTL(Time-To-Live) 인덱스**: 시간이 지나면 자동으로 데이터를 삭제할 수 있도록 설정.

### 2. **사용자의 광고 참여 및 광고 참여 이력 조회를 위한 필드**
#### 사용자가 광고에 참여할 때:
- `user_id`: 사용자의 참여 이력을 조회하거나 새로 참여할 때 중요한 필드.
- `ad_id`: 사용자가 특정 광고에 참여했는지 확인할 때 자주 사용되는 필드.

#### 광고 참여 이력을 조회할 때:
- `user_id`: 특정 사용자에 대한 참여 이력을 조회할 때 필요.
- `participation_time`: 특정 기간 동안의 참여 이력을 조회할 때 중요.

### 3. **필요한 인덱스**

#### 3.1. **사용자가 광고에 참여할 때**
사용자가 **특정 광고에 참여했는지** 여부를 빠르게 확인하기 위해, **복합 인덱스**가 필요합니다.

- **복합 인덱스** (`user_id`, `ad_id`): 사용자가 해당 광고에 참여했는지 확인할 때 빠른 조회를 위해 사용합니다.

```javascript
db.ads_participation_history.createIndex({ user_id: 1, ad_id: 1 });
```

이 인덱스는 `user_id`와 `ad_id`가 조합된 형태로 참여 여부를 빠르게 조회할 수 있게 합니다.

#### 3.2. **사용자가 참여한 광고 이력 조회**
특정 사용자의 광고 참여 이력을 조회할 때, **`user_id`**와 **`participation_time`** 필드가 자주 사용됩니다. 이 경우에도 **복합 인덱스**를 사용하면 효율적입니다.

- **복합 인덱스** (`user_id`, `participation_time`): 특정 사용자의 참여 이력을 시간순으로 빠르게 조회할 수 있습니다.

```javascript
db.ads_participation_history.createIndex({ user_id: 1, participation_time: -1 });
```

이 인덱스는 사용자별 참여 이력을 **참여 시간 순으로** 내림차순 정렬하여 빠르게 조회할 수 있게 합니다.

#### 3.3. **TTL 인덱스** (선택 사항)
광고 참여 이력은 일정 기간이 지나면 더 이상 필요하지 않을 수 있습니다. 이 경우, **TTL 인덱스**를 설정하여 **특정 기간**이 지나면 자동으로 데이터를 삭제할 수 있습니다.

- **TTL 인덱스** (`participation_time`): 예를 들어, 광고 참여 이력이 1년이 지나면 자동으로 삭제되도록 설정할 수 있습니다.

```javascript
db.ads_participation_history.createIndex({ participation_time: 1 }, { expireAfterSeconds: 31536000 }); // 1년 = 31,536,000초
```

이 TTL 인덱스는 **1년이 지난 참여 이력**을 자동으로 삭제하는 역할을 합니다.

### 4. **인덱스 요약**
1. **복합 인덱스** (`user_id`, `ad_id`): 사용자가 특정 광고에 참여했는지 빠르게 조회.
2. **복합 인덱스** (`user_id`, `participation_time`): 특정 사용자의 광고 참여 이력을 시간 순으로 조회.
3. **TTL 인덱스** (`participation_time`, 선택 사항): 일정 시간이 지난 참여 이력을 자동으로 삭제.

### 5. **Spring Data MongoDB 인덱스 설정 예시**

```java
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "ads_participation_history")
@CompoundIndexes({
    @CompoundIndex(name = "user_ad_idx", def = "{'user_id' : 1, 'ad_id' : 1}"),
    @CompoundIndex(name = "user_participation_time_idx", def = "{'user_id' : 1, 'participation_time' : -1}")
})
public class AdParticipationHistory {

    @Indexed
    private String userId;
    private String adId;
    private String adName;
    private double rewardAmount;
    private Date participationTime;

    // Getters and Setters
}
```

이 코드는 **Spring Data MongoDB**에서 복합 인덱스를 설정하는 예시입니다. `"user_ad_idx"`는 사용자와 광고를 기준으로 인덱스를 생성하고, `"user_participation_time_idx"`는 사용자와 참여 시간을 기준으로 내림차순 인덱스를 생성합니다.

이런 방식으로 인덱스를 설정하면 광고 참여 이력 조회와 참여 여부 확인을 효율적으로 할 수 있습니다.