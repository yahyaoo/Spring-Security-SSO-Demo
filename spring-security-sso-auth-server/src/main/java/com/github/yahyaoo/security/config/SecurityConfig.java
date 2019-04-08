/*
 *  Copyright 2019 Yahyaoo.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.yahyaoo.security.config;

import com.github.yahyaoo.security.authentication.DemoUserDetailsAuthenticationProvider;
import com.github.yahyaoo.security.config.rememberme.RemembermeConfig;
import com.github.yahyaoo.security.filter.DemoAuthenticationFilter;
import com.github.yahyaoo.security.rememberme.RedisRememberMeServices;
import com.github.yahyaoo.security.rememberme.RedisTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

/**
 * Configuration related to server security
 *
 * @author yahyaoo
 * @date 2019/2/25 11:36
 */
@Order(1)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // ~ Instance fields
    // =======================================================================================

    private final UserDetailsService userDetailsService;
    private final RedisTokenRepository tokenRepository;
    private final RemembermeConfig remembermeConfig;

    // ~ Constructors
    // ===================================================================================================

    public SecurityConfig(UserDetailsService userDetailsService,
                          RedisTokenRepository tokenRepository,
                          RemembermeConfig remembermeConfig) {
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.remembermeConfig = remembermeConfig;
    }

    // ~ Methods
    // =========================================================================================================

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rememberMeAuthenticationFilter(), RememberMeAuthenticationFilter.class)

                .requestMatcher(AnyRequestMatcher.INSTANCE)

                /*Resource Server which handles the requests for user endpoint*/
//                .requestMatchers()
//                .antMatchers("/login*", "/oauth/authorize", "/logout", "/", "/index", "/user/**", "/css/**")
//                .and()

                .authorizeRequests()
                .antMatchers("/css/**", "/login*", "/index", "/druid/**").permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler((LogoutHandler) rememberMeServices())

//                .and()
//                .rememberMe()
//                .rememberMeServices(RememberMe.getInstance(userDetailsService).getServices())
//                .key("Yahyaoo")
        ;

    }

    private DemoAuthenticationFilter authenticationFilter() throws Exception {
        DemoAuthenticationFilter authenticationFilter = new DemoAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler());
        authenticationFilter.setRememberMeServices(rememberMeServices());
        return authenticationFilter;
    }

    private RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
        return new RememberMeAuthenticationFilter(authenticationManagerBean(), rememberMeServices());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(authenticationProvider())
                .authenticationProvider(new RememberMeAuthenticationProvider(remembermeConfig.getKey()));
    }

    private AuthenticationProvider authenticationProvider() {
        return new DemoUserDetailsAuthenticationProvider(passwordEncoder(), userDetailsService);
    }

    private SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }

    private RememberMeServices rememberMeServices() {
        RedisRememberMeServices rememberMeServices = new RedisRememberMeServices(
                remembermeConfig.getKey(), userDetailsService, tokenRepository);
        rememberMeServices.setTokenValiditySeconds(remembermeConfig.getExpire());
        return rememberMeServices;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
