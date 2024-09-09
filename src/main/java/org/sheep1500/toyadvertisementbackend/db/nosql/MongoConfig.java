package org.sheep1500.toyadvertisementbackend.db.nosql;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "org.sheep1500.*"  // MongoDB 관련 repository 경로
)
public class MongoConfig {
    // 별도의 설정이 없으면 기본적으로 application.yml에서 설정을 읽음
}
