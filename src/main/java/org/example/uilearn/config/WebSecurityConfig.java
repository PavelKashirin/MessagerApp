package org.example.uilearn.config;

import lombok.RequiredArgsConstructor;
import org.example.uilearn.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/registration").permitAll() // доступ на корень и регистрацию есть у всех
                    .anyRequest().authenticated() // остальные запросы требуют авторизацию
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll() // форма логина - это шаблон login.mustache и доступ у всех
                .and()
                    .logout()
                    .permitAll(); // у логаута формы нет, доступ у всех
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());

//                .usersByUsernameQuery("select username, password, is_active from application_user where username=?")
//                .authoritiesByUsernameQuery("select u.username, ur.roles from application_user u inner join user_role ur on u.id = ur.user_id where u.username=?");

    }
}
