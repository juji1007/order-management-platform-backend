# 음식 주문 관리 플랫폼

## 프로젝트 개요

이 플랫폼은 온라인과 오프라인에서 고객이 음식을 주문하고 결제할 수 있도록 지원합니다. 주요 사용자로는 음식점 주인, 고객, 관리자 등이 있으며, 각 사용자에게 맞는 권한과 기능을 제공합니다.

## 주요 기능

- AI 기반 음식 설명 자동 생성
- 다양한 가게,상품 검색 기능 제공
- 보안 강화를 위한 JWT 기반 인증 및 인가
- Redis를 활용한 성능 최적화
- 무중단 배포(블루/그린 배포) 지원
- 글로벌 에러 처리
- 

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

## 서비스 구성
- 컨벤션
- 베이스엔티티
- 글로벌 에러 처리
- 

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

## 실행 방법
이 프로젝트를 실행하려면 다음 단계를 따라주세요.

### 1. Git 클론

먼저 GitHub에서 프로젝트를 클론합니다:

```bash
git clone https://github.com/2025-order-management-platform/order-management-platform-backend.git
cd order-management-platform-backend
```

### 2. Docker 실행
프로젝트는 Docker Compose를 사용하여 실행됩니다. Docker를 사용하여 애플리케이션을 실행하려면 아래 명령어로 관련 컨테이너를 시작하세요.

```bash
docker-compose up --build
```

### 3. 애플리케이션 실행 확인
애플리케이션이 정상적으로 실행되면, 아래 URL을 통해 확인할 수 있습니다.

Backend: http://localhost:8080
위 주소에 접속하여 애플리케이션이 정상적으로 동작하는지 확인합니다.

## CI/CD

### 블루/그린 무중단 배포 아키텍처

- **AWS EC2**
  - NGINX
  - BLUE 서버
  - GREEN 서버
  - Redis
- **RDS (PostgreSQL)**

## 인프라 설계도

## ERD



## API 문서

(추가 필요)

## 프로젝트 개선점

## 팀원 역할

(추가 필요)





## API docs 


## 팀원 역할 

