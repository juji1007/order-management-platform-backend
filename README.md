
# <음식 주문 관리 플랫폼>
## 프로젝트 목적 / 상세 
- 이 플랫폼은 온라인과 오프라인 모두에서 고객이 움식을 주문하고 결제할 수 있도록 지원합니다.
- 서비스는 음식점 주인, 고객, 관리자 등 세 가지 주요 사용자에 초점을 맞춰 각 사용자에게 맞는 권한과 기능을 제공합니다.
- ai 기능을 통해 음식 설명을 자동으로 생성을 통해 고객과 주인 모두의 편의를 도모함.
- 다각적인 상품 검색 기능을 통한 고객 만족도 향상을 도모함(?)
## 기술 스택 
| **Category**        | **Technology**    | **Version** |
|---------------------|-------------------|-------------|
| **Framework, Library** | Spring Boot       | 3.4.2     |
|                     | Spring Security   | 6.x         |
| **Database**        | PostgreSQL        | 15.x        |
|                     | Spring Data JPA   | 3.x         |
| **Build, Dependency** | Gradle           | 8.x         |
|                     | QueryDSL          | 5.0.0       |
| **Authentication**  | JWT               | 0.12.3      |
| **Test**            | JUnit             | 5.x         |
|                     | Mockito           | 5.x         |




## 서비스 구성 및 실행 방법 



## ERD


# 음식 주문 관리 플랫폼

## 프로젝트 개요

이 플랫폼은 온라인과 오프라인에서 고객이 음식을 주문하고 결제할 수 있도록 지원합니다. 주요 사용자로는 음식점 주인, 고객, 관리자 등이 있으며, 각 사용자에게 맞는 권한과 기능을 제공합니다.

## 주요 기능

- AI 기반 음식 설명 자동 생성
- 다양한 상품 검색 기능 제공
- 보안 강화를 위한 JWT 기반 인증 및 인가
- Redis를 활용한 성능 최적화
- 무중단 배포(블루/그린 배포) 지원

## 기술 스택

| **카테고리**          | **기술 스택**              | **버전** |
| ----------------- | ---------------------- | ------ |
| **프레임워크 & 라이브러리** | Spring Boot            | 3.4.2  |
|                   | Spring Security        | 6.2.1  |
|                   | Spring Data JPA        | 3.2.1  |
| **데이터베이스**        | PostgreSQL             | 15.x   |
|                   | H2 (테스트 DB)            | 최신 버전  |
| **빌드 & 의존성 관리**   | Gradle                 | 8.x    |
|                   | QueryDSL               | 5.0.0  |
| **인증 & 보안**       | JWT                    | 0.12.3 |
| **테스트**           | JUnit                  | 5.x    |
| **캐싱**            | Redis                  | 7.2.0  |
| **유효성 검사**        | Spring Boot Validation | 3.4.2  |

## 서비스 구성 및 실행 방법

### 환경 변수 설정

```yaml
# JWT 환경 변수
jwt:
  secret: "<JWT_SECRET_KEY>"
  refresh-token-validity: 86400000
  access-token-validity: 3600000
  prefix: "Bearer "
  header: "Authorization"

# AES 암호화 환경 변수
encryptor:
  aes:
    key: "<16_byte_key>"

# Redis 환경 변수
spring:
  data:
    redis:
      host: localhost
      port: 6379

# OpenAI API 키
openai:
  api:
    key: "<OPENAI_API_KEY>"
```

## CI/CD

### 블루/그린 무중단 배포 아키텍처

- **AWS EC2**
  - NGINX
  - BLUE 서버
  - GREEN 서버
  - Redis
- **RDS (PostgreSQL)**

## ERD

(추가 필요)

## API 문서

(추가 필요)

## 팀원 역할

(추가 필요)





## API docs 


## 팀원 역할 

