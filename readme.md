## 개발

---
**TODO**

- 도메인 & 흐름도 (수요일 작업)
- 통합 테스트


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

# 1. 프로젝트 설명

---

기술 스택

Java(JDK 17), Spring Boot(3.3.3), RDBMS(MySQL, H2), NoSQL(MongoDB), Cache/Lock(Redis), Message Broker(RabbitMQ)

### 빌드 및 실행 방법

```
// docker 설치 및 실행
docker-compose up -d

// docker 설치 후 실행
docker-compose start

// 빌드 운영체제별 gradle 명령어 MAC: gradlew, WINDOWS: gradlew.bat

// 빌드, JDK 17
./gradlew build

// 프로젝트 실행, swagger ui 로 확인 가능 http://localhost/swagger-ui.html
// gradlew
./gradlew bootRun
// java
java -jar ./build/libs/toy-advertisement-backend-0.0.1-SNAPSHOT.jar

// 테스트 실행
./gradlew test
// 결과는 ./build/reports/tests/test/index.html 에서 확인 가능
```


# 2. 시스템 설계

---

### 핵심 문제 파악 및 전략

- 대용량 트래픽: 실제 사용자 데이터 기반으로 트래픽 추산
  - 사용자 구분: 일반 사용자 / 가맹점으로 구분
  - API TPS 계산: API를 분석하여, 구분 된 사용자가 사용하는 API로 분류 > 분류된 API를 사용자 수를 기반하여 TPS 계산
- 서비스 분리: 분류된 API를 기반으로 분류 특성에 맞는 서비스 구분
  
- 데이터 저장소 구분: 사용자 데이터 구분하여, 데이터 별 최적화 DB 구분
  - RDBMS: 빈번한 수정, 조회 데이터(광고 등록)
  - NoSQL: 대용량 삽입 데이터(광고 참여 이력)
- 성능 최적화
  - 대용량 조회 API: Caching 적용, 없을 경우 DB 조회 후 Caching
  - 대용량 처리 API: 두가지 조건을 충족하기 위해 Message Broker 적용하여 이벤트를 Queueing 해서 처리
- 동시성 처리
  - 여러개의 서버 구동을 고려하여 분산 Lock 적용 
- 도메인 설계: DDD 기반 
- 분리 된 서비스 간 의존성을 낮추기 위해 Event Driven 적용
- 도메인 기능과 분리되는 기능인 캐싱, 락 역할 분리 



## 시스템 설계를 위한 대략적인 계산 (Back-of-the-envelope Estimation)

---
데이터 근거: **카카오페이 디스플레이 광고 상품 소개서**
- 파일 경로: ${프로젝트경로}/kakaopay_AD_product_2406.pdf

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

| 구분            | 일반 사용자    | 가맹점      |
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


## 시스템 설계 - 아키텍쳐

---

### **시스템 아키텍쳐**
(위 시스템 설계를 위한 대략적인 계산 기반)  

![ads_archi](https://github.com/user-attachments/assets/733d9fe3-455c-4616-a1b4-26c19a57b009)
- 미포함 요소: API Gateway, Load Balancer, CDN
- 구성 변경 요소: API Server(광고, 사용자 광고), Consumer 서버는 하나의 프로젝트로 구동

### 설계 이유

서비스 사용자(일반 사용자, 가맹점)에 따른 기능 분리와 TPS, 데이터를 기반하여, 각 기능 별 서비스 분리

**1. 광고 서비스**

광고 등록(1 TPS)과 광고 조회(600 TPS)의 부하가 분리된 API 서버. 
- 구성: Load Balancer, API Server, Cache, RDBMS
- RDBMS: 조회, 수정을 고려하여 RDBMS로 적용
  - 일일 86,400건이지만 지속적인 증가에 대비해 성능 최적화 필요
- Cache: 대용량 트래픽(600 TPS)를 고려하여 조회 데이터 캐싱 처리
  - TTL: 조회 시간 지났을 경우(1일)  
  - Cache Evict: 조회 기간 내의 광고 등록 / 광고 참여 후 광고의 참여 가능 횟수가 0회로 변경 될 때


**2. 사용자 광고 서비스**

광고 참여 이력 조회, 광고 참여(600 TPS), 여러 사용자 광고 참여에 대한 동시성 이슈로 API 별 기능 분리
- 구성: Load Balancer, API Server, Message Broker, NoSQL
- NoSQL: 일일 1,700만 건의 광고 참여 이력 대용량 데이터를 저장하기 위해 NoSQL로 적용
- Message Broker: 대용량 트래픽과 동시성 이슈를 처리하기 위해 광고 참여 요청을 Message Queue로 이벤트 처리

**3. 사용자 광고 참여 서비스**

대용량 트래픽과 동시성 이슈를 처리하기 위한 광고 참여 서비스로 분리
- 구성: Consumer Server, (사용자 광고 서비스 구성) Message Broker, NoSQL
- Consumer Server: Message Broker 에서 이벤트를 받아 비동기적으로 처리하여 트래픽 분산
  - 급격한 트래픽 증가 대비 필요 


### 설계 미포함 고려 사항
- 모니터링 및 로깅 
  - 트래픽 증가에 대비해 실시간 스케일링 가능한 모니터링 시스템 필요
- 백업 및 장애 복구
  - DR 계획 
- API Rate Limiting
  - 트래픽 폭증 시 대비를 위한 Rate Limiting 설정 
- 대용량 데이터
  - 조회를 위한 인덱스는 어떻게 ?
    - 동적 파티셔닝 ?
  - 저장 공간은 어떻게 ? (사용자 1년: 63억 row / 11,000 PB , 가맹점 1년: 3,200만개 / 1,400 TB)
    - 파티셔닝? 동적 테이블?



# 3. 도메인 및 흐름도

---

## 도메인

---

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




## 흐름도

---

