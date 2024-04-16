<!-- 프로젝트 소개 -->

# Tech Mentor Defense API

## :star2: 프로젝트 소개

- OpenAI API 를 활용한 AI 멘토링 웹 애플리케이션 백엔드 RestAPI 입니다.
- 2023 스파르타 해커톤 AI 부분 2위 프로젝트로 2024년 3월 현재 RestAPI로 리팩토링 및 스프링부트 3.0, 자바 21로 마이그레이션 중입니다.

<!-- 핵심기능 -->

### :dart: 핵심기능

- 기술 주제별 전문가의 멘토링 API
    - 질문에 대한 멘토링으로 진행됩니다.
    - 프롬프트 엔지니어링이 적용되어 정확하고 이해하기 쉬운 예시를 들어 해당 주제의 전문가 답변을 제공합니다.
    - 유저의 행동(질문)이 일정시간 없으면 자동으로 면접 질문을 생성하여 먼저 질문을 합니다.
    - 질문에 대한 유저의 답변이 정확하지 않다면 꼬리 질문을 합니다.
- 유저 가입 및 카카오, 구글 Oauth 로그인 API
- 유저 정보 수정 API
- 기술 목록 조회 API

## 기술스택

### 언어 및 의존성

- Java 21 Amazon Corretto
- SpringBoot 3.2.1
- Spring Data JPA 3.2.1
- Spring Validation 3.2.1
- Spring Data Redis 3.2.1
- Spring RestDocs 3.0.1
- Spring Security 6.2.1
- JJWT 0.12.3
- JUnit5, Mockito
- Apache Jmeter

## api 명세서

- RestDocs:

## 핵심문제 해결과정 및 전략