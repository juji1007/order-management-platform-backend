# 음식 주문 관리 플랫폼



## 프로젝트 개요

이 플랫폼은 온라인과 오프라인에서 고객이 음식을 주문하고 결제할 수 있도록 지원합니다. 주요 사용자로는 음식점 주인, 고객, 관리자 등이 있으며, 각 사용자에게 맞는 권한과 기능을 제공합니다.



## 주요 기능
- 사용자 관리
  - 회원가입 및 사용자 정보 관리 
  - 관리자 권한 관리 기능
    
- AI 기능
  - 외부 AI API 연동

- 인증 및 보안
  - JWT 기반 사용자 인증 및 권한 관리
  - 보안 필터 및 예외 처리

- 결제 및 쿠폰
  - 쿠폰 발급 및 사용자 쿠폰 관계 관리
  - 결제 처리 및 예외 관리

- 스토어 및 상품 관리
  - 상품 등록, 수정, 삭제
  - 카테고리 관리
  - 상점 정보 관리

- 주문 및 리뷰 관리
  - 주문 생성 및 조회
  - 주문리뷰 작성 및 관리
  - 주문-상품 관계 관리

- 공통 유틸 및 설정
  - 글로벌 예외 처리
  - 공통 응답 포맷 제공
  - 공통 Auditing 관리

    

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
- ai/ : AI 관련 로직 (API 연동, 서비스, 데이터 관리 등)

- auth/ : 인증 및 보안 관리 (JWT 인증, 필터, 컨트롤러 등)

- user/ : 사용자 정보 및 계정 관리
  
- userCoupon/: 사용자-쿠폰 관계 관리

- coupon/ : 쿠폰 발급 및 관리 기능

- payment/ : 결제 처리 및 예외 관리

- store/ : 상점 및 상품 관련 로직
  
- serviceArea/ : 상점 - 서비스지역 관계 관리
  
- area/ : 지역 생성 및 관리

- product/ : 상품 정보 및 서비스

- order/ : 주문 생성 및 관리

- orderReview/ : 리뷰 작성 및 관리

- orderProduct/ : 주문-상품 관계 관리

- global/ : 공통 유틸 및 설정(공통Auditing, 글로벌 에러, 공통 응답 처리)
  

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

### 프로젝트 주요 기술 구현

- 시큐리티 기반 사용자 Role 관리 및 API 권한 부여
- 사용자 주문 결제 시 쿠폰 적용
- AI 기반 음식 설명 자동 생성
- 쿼리dsl을 이용 상점, 상품, 평점 통합 검색
- 보안 강화를 위한 JWT 기반 인증 및 인가
- 공통 유틸 및 설정 기반 모든 디비 Auditing 관리 및 응답,에러 컨벤션 적용
- 모든 DB softDelete 적용 및 조회 시 필터링 기능



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
![image](![nineteen-omp-infra drawio](https://github.com/user-attachments/assets/8ebe4eeb-f1c9-4b1f-bae5-74db37164089))
- 링크 

## ERD
- ![ERD - 19조](![image](https://github.com/user-attachments/assets/bc00f7d5-2a10-4d79-8248-9bf241156a3a))

- [ERD 및 테이블 설계](https://www.notion.so/teamsparta/ERD-1982dc3ef51480e3a46bded8c16217a9)

## 프로젝트 개선점
- 쿼리dsl로 통합검색 시 N + 1 문제 해결
- 상점 조회 시 서비스 지역 리스트 표시

- MSA 로의 확장
  

## API docs 
- 

## 팀원 역할 

