# WHOKIE

## 🔗 배포 주소
https://whokie.com

## 🎯 프로젝트 목표

- “타인의 긍정적 평가로 나를 알아갈 수 있는 소셜 미디어 플랫폼”을 구현
- 단순 기능 구현을 넘어 대규모 트래픽에서도 안정적인 서비스 제공
- 객체지향 설계 원칙을 준수하고 확장 가능한 아키텍처를 구축
- 철저한 문서화, CI/CD 파이프라인 구축을 통해 효율적인 협업이 가능한 프로젝트 지향
- 실패 케이스를 고려한 견고한 테스트 작성과 높은 테스트 커버리지 유지에 중점
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)를 기반으로 한 코드 컨벤션과 Git 커밋 컨벤션을 준수하여 일관성 있는 코드베이스를 유지

## 🛠 기술 스택

### 프로젝트 기술 스택

- **Framework:** Spring Boot
- **Database:** MySQL, Redis, H2 (테스트용)
- **Infrastructure:** AWS (S3, EC2, etc.)
- **CI/CD:** GitHub Actions, Docker
- **Authentication:** OAuth2.0 (Kakao)
- **Web Server:** NGINX (SSL, 로깅, gzip, proxy_pass 설정)
- **APIs:** Kakao API, KakaoPay API

### 주요 설정 및 참고 사항

- **NGINX**:
    - SSL 설정으로 보안 강화
    - 로그 설정 및 proxy_pass를 통한 경로 라우팅
- **Redis**: JWT 관리 및 캐싱 용도로 사용
- **MySQL**: 주 데이터베이스로 활용
- **EC2 & S3**:
    - EC2에서 애플리케이션 실행
    - S3에서 정적 파일 호스팅
- **GitHub Actions**: 코드 푸시 시 테스트 및 배포 자동화

## ⚠️ ISSUE
- 테스트 자동화 [https://velog.io/@momnpa333/github-actionsspring-test-자동화](https://velog.io/@momnpa333/github-actionsspring-test-%EC%9E%90%EB%8F%99%ED%99%94)
- https, 도메인 통합 [https://velog.io/@momnpa333/https-nginx-spring-s3-docker-로-배포하기](https://velog.io/@momnpa333/https-nginx-spring-s3-docker-%EB%A1%9C-%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0)
- 성능 테스트 도입기
 [https://velog.io/@momnpa333/성능테스트-ngrinder-활용하기](https://velog.io/@momnpa333/%EC%84%B1%EB%8A%A5%ED%85%8C%EC%8A%A4%ED%8A%B8-ngrinder-%ED%99%9C%EC%9A%A9%ED%95%98%EA%B8%B0)
- SSE
- TestConfiguration이 SpringBootTest에서 Scan되지 않은 이유
- n+1 해결 시 @EntityGraph 사용  https://geonit.tistory.com/72
- 카카오페이 api (포인트 결제)  https://geonit.tistory.com/71
- Redisson 분산락을 통한 동시성 제어  https://yso8296.tistory.com/29
- @Async를 이용한 비동기 처리 https://yso8296.tistory.com/28

## 🏗 시스템 아키텍쳐
<img width="689" alt="image" src="https://github.com/user-attachments/assets/84f81964-df7f-46af-9983-a5cbc2ec8a62">

## 🔄 프로젝트 구조 변경안

### V1. Command,Model 적용
<img width="707" alt="image" src="https://github.com/user-attachments/assets/79d32aab-a04f-443e-b89b-d33dea8cc7ba">

### V2. Service 분리
<img width="703" alt="image" src="https://github.com/user-attachments/assets/70cc556d-ead9-4c07-ab7c-f2a99952194b">

- Async 적용기
- Scheduler 젹용기

## 🌳 GIT 브랜치 전략

## 전체 워크플로우

[Whokie 프로젝트 레포지토리 워크 플로우]
<br>
<img width="399" alt="image" src="https://github.com/user-attachments/assets/e4f55217-1524-4e7a-9f6f-cb62df6016d6">

## Git branch 전략

1. 테크리더가 Weekly 브랜치 생성
    - ex) Weekly/8
2. 각자 Issue 브랜치를 생성하여 배정받은 기능 구현
    - ex) Weekly/8/issue#101
    - 구현한 기능은 Weekly 브랜치에 PR
    - 테크리더는 이후 코드리뷰 진행 후 Weekly 브랜치로 merge 진행
3. 프로젝트 리뷰 미팅 통해 오류 해결 및 보고서 작성
    - 테크리더는 이후 Weekly에서 Develop으로 conflit 해결하고 merge 진행
4. 주차별 최종 코드 Develop에서 Master 브랜치로 PR 후 merge 진행
5. Master에서 Prod 브랜치로 merge 진행
6. 정해진 기간 동안 담당 멘토와 코드리뷰 진행 (총 6회 수행)
    - Master에서 Review 브랜치로 merge 진행
    - 코드리뷰 요청시 테크리더가 Review 브랜치에 PR
    - Merge는 담당 멘토가 이후 진행

## 브랜치에 대한 간략한 설명

- **master** : 최종 브랜치로 develop의 브랜치를 Merge
- **develop** : 오류 수정 브랜치로 weekly브랜치에서 conflict 해결 후 작업한 기능들을 Merge
- **weekly** : 주차별 업데이트 코드 추합 브랜치로 이 브랜치를 기준으로 각자 작업한 기능들을 Merge
- **issue** : 단위 기능을 개발하는 브랜치로 기능 개발이 완료되면 weekly 브랜치에 Merge
- **prod** : 배포 서버에 영향을 주는 브랜치로 Master의 브랜치를 Merge
- **review** : 현업 멘토님 코드리뷰 브랜치로 한 주 동안 작성한 기능들을 리뷰받는 브랜치

## 📊 ERD
<img width="706" alt="image" src="https://github.com/user-attachments/assets/e396eeeb-bb66-4a74-b87d-9ba7bbb5578a">

## 🧪 테스트

- s3 비동기, 동기 테스트
- 테스트커버리지

## **프로젝트 중점사항**

- Service Layer 분리를 통해 순환 참조 가능성 제거
- nGrinder를 활용한 API별 TPS, MTT 관리
- N+1 문제 해결 통한 조회 성능 최적화
- Command, Model 패턴 적용을 통해 변화에 용이한 코드 구조 작성
- PR/머지 시 자동화된 테스트를 통한 코드 안정성 증가
- Nginx의 Reversed-Proxy 활용
- GitHub Actions 및 Docker를 통한 배포 자동화
- dev/prod 서버 분리를 통한 개발 효율성 증가
- 카카오 OAuth2.0을 활용한 로그인 구현
- JWT기반 로그인 인증/인가
- AWS S3를 이용한 프로필/그룹 이미지 저장
- Swagger를 통한 API 문서화
- Nginx와 Certbot을 활용하여 SSL 인증서 관리
- 관리자 페이지를 통한 사용자, 그룹, 질문 등의 효율적 데이터 관리 및 모니터링
- Redis를 활용하여 조회 성능 최적화 및 DB 접근 최소화
- 단위 테스트 및 통합 테스트 작성
- 로그 작성을 통한 서비스 모니터링 및 문제 추적
- SSE를 활용하여 클라이언트에게 실시간 알림 이벤트 발행
- 비동기 처리를 통해 응답 속도 향상
- Redisson Lock 및 동시성 제어
- Mock 객체를 통한 테스트 빌드 시간 감소
- record 도입을 통한 보일러 플레이트 코드 제거
- 카카오페이 api를 사용하여 결제 기능 구현
- ConfigurationProperties를 사용하여 환경변수 주입
- 이슈 발생 시 문서화
- 주2회 대면 팀미팅을 통해 코드 리뷰 및 피드백

