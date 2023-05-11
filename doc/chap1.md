## 1. 스프링 시큐리티 기본 API 및 Filter 이해


### 로그인 시 Security 관련 filter 작동 순서
![img.png](img/img.png)

- `UsernamePasswordAuthenticationFilter`: 사용자 요청을 받아 로그인 인증처리 담당
- `AntPathRequestMatcher`: 해당 요청 정보가 입력된 경로와 일치하는지 확인
  - default: /login
  - loginProcessingUrl 에 등록된 링크로 match 확인
  - 일치하지 않는 경우 chain.doFilter 전달
  - 일치하는 경우 `Authentication` 객체 생성해 로그인 유저 정보 저장 (Username + Password)
- `AuthenticationManager`: 인증객체 전달받아 인증처리 진행
  - 내부적으로 `AuthenticationProvider` 가지는데 이 중 하나에 인증 위임 -> 실제로 인증 담당
  - 인증 실패 시 `AuthenticationException` 발생
  - 인증 성공 시 새로운 인증정보 담긴 `Authentication` 객체 생성 (User + Authorities)
- `SecurityContext`: 최종 인증 정보 저장되는 저장소. 세션에도 저장됨
- `SuccessHandler`: 인증 성공 이후 작업 처리


---


### 로그아웃 시 관련 filter 작동
![img_1.png](img/img_1.png)

- GET 방식은 원칙적으로 오류 발생 -> POST 방식으로 요청
- `LogoutFilter`: 요청을 받아 로그아웃 인증 처리 담달
- `AntPathRequestMatcher`: 해당 요청 정보가 입력된 경로와 일치하는지 확인
  - default: /logout
  - 일치하지 않는 경우 chain.doFilter 전달
  - 일치하는 경우 `SecutiryContext` 에서 `Authentication` 인증객체 꺼내와 LogoutHandler 에 전달
- `SecurityContextLogoutHandler`
  - 세션 무효화
  - 쿠키 삭제
  - `SecurityContextHolder.clearContext()`: SecurityContext 삭제
- `SimpleUrlLogoutSuccessHandler`: 로그아웃 성공 시 작동해 설정 페이지로 리다이렉션


---


### Remember Me 인증
- 세션이 만료되고 웹 브라우저가 종료된 이후에도 어플리케이션이 사용자를 기억하는 기능
  - ex. 자동로그인

- Remember-Me 쿠키에 대한 HTTP 요청 확인 후 토큰기반 인증 사용해 유효성 검사
  - -> 검증되면 로그인

#### 사용자 라이프 사이클
- 인증 성공: Remember-Me 쿠키 설정
- 인증 실패: 쿠키 존재 시 쿠키 무효화
- 로그아웃: 쿠키 존재 시 쿠키 무효화


---


### AnonymousAuthenticationFilter 익명 사용자 인증 처리 필터
- 인증을 받은 특정 사용자 -> 세션에 인증 객체 저장 -> 세션에서 인증정보 조회해서 접근판단
  - => 매번 조회??
- 익명 사용자와 인증 사용자를 구분해 처리하기 위해 사용
- 화면에서 인증 여부 구현 시 `isAnonymous()` / `isAuthenticated()` 구분해 사용
  - 익명 사용자여도 _null_ 로 처리하는게 아닌 익명사용자 객체를 생성해 인증사용자처럼 **ROLE** 저장해 사용
- 인증 객체를 세션에 저장하지 않음

