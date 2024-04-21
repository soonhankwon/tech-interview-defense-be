<!-- 프로젝트 소개 -->

# Tech Interview Defense API

## :star2: 프로젝트 소개

- OpenAI API 를 활용한 AI 멘토링 웹 애플리케이션 백엔드 RestAPI 입니다.
- IT 기술에 대한 정확하고 이해하기 쉬운 멘토링 니즈와 기술 면접 질문 준비 요구를 해결하기 위해 만든 API 입니다.
- 2023 스파르타 해커톤 AI 부분 2위 프로젝트로 2024년 3월 현재 RestAPI 로 리팩토링 및 스프링부트 3.0, 자바 21로 마이그레이션 중입니다.

<!-- 핵심기능 -->

### :dart: 핵심기능

- `기술 주제별 전문가의 멘토링` API
    - 질문에 대한 멘토링으로 진행됩니다.
    - `프롬프트 엔지니어링`이 적용되어 정확하고 이해하기 쉬운 예시를 들어 해당 주제의 `전문가 답변`을 제공합니다.
    - 유저의 행동(질문)이 일정시간 없으면 `자동으로 면접 질문을 생성`하여 먼저 질문을 합니다.
    - 질문에 대한 유저의 답변이 정확하지 않다면 `꼬리 질문`을 합니다.
- 유저 가입 및 카카오, 구글 Oauth 로그인 API
- 유저 정보 수정 API
- 기술 주제별 채팅(멘토링) 생성 및 삭제 API
- 기술 목록 조회 API

## 기술스택

### 언어 및 의존성

- Java 21 Amazon Corretto
- SpringBoot 3.2.3
- Spring Data JPA 3.2.3
- Spring Validation 3.2.3
- Spring Data Redis 3.2.3
- Spring RestDocs 3.0.1
- Spring Security 6.2.2
- JJWT 0.12.3
- JUnit5, Mockito
- Apache Jmeter

## api 명세서

- RestDocs:

## 핵심문제 해결과정 및 전략

### AI 멘토와의 대화의 흐름이 부자연스러운 문제발생

- ChatCompletions 사용시 AI가 중복 질문을 하는 등 대화의 흐름이 매우 부자연스러운 문제가 발생했습니다.
- OpenAI에서 제공하는 GPT API는 ChatCompletions 와 Completions로 구분됩니다.
    - Completions는 특정 문장을 이어서 완성시키거나 새로운 텍스트를 만드는 특징을 갖습니다.
    - ChatCompletions의 가장 큰 특징은 이전 대화와의 경험으로 다음 질문에 대한 대답을 도출합니다.
- 따라서 멘토링의 특성상 자연스러운 대화를 이어나가기 위해 `ChatCompletions`이 비용이 10%가량 비싸지만 해당 기술을 선택했습니다.
- ChatCompletions 요청시 `과거 대화내용을 리스트`로 보내주어 AI가 부자연스러운 답변을 하는 문제를 개선시켰습니다.

### 기술목록 조회 API 불필요 DB I/O 발생 문제

- `기술목록 조회 API`는 거의 변화가 없는 고정된 결과를 조회하는 API입니다.
    - `해당 조회 결과를 캐싱`하여 불필요한 DB I/O가 발생하는것을 개선시켰습니다.
- 글로벌한 캐싱전략을 가져가기 위해(확장성) 캐싱에 `Redis`를 활용했습니다.