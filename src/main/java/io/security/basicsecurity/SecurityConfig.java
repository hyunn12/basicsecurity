package io.security.basicsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 인가
        http
                .authorizeRequests()
                .anyRequest().authenticated()
        ;

        // 인증
        http
                .formLogin() // Form 로그인 인증 기능이 동작함
//                .loginPage("/loginPage") // 사용자 정의 로그인 페이지 = 인증없이도 접근 가능해야함
                .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트
                .failureUrl("/login") // 로그인 실패 후 리다이렉트
                .usernameParameter("userId") // 아이디 파라미터명
                .passwordParameter("passwd") // 비밀번호 파라미터명
                .loginProcessingUrl("/login_proc") // 로그인 Form Action Url
                .successHandler((request, response, authentication) -> {
                    System.out.println("authentication ::: " + authentication.getName());
                    response.sendRedirect("/");
                }) // 로그인 성공 후 핸들러
                .failureHandler((request, response, exception) -> {
                    System.out.println("exception ::: " + exception.getMessage());
                    response.sendRedirect("/login");
                }) // 로그인 실패 후 핸들러
                .permitAll()
        ;

        http
                .logout() // 로그아웃 기능이 동작함
                .logoutUrl("/logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동 페이지
                .deleteCookies("JSESSIONID", "remember-me") // 로그아웃 후 쿠키 삭제
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    session.invalidate();
                }) // 로그아웃 핸들러
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.sendRedirect("/login");
                }) // 로그아웃 성공 후 핸들러

        ;

    }
}
