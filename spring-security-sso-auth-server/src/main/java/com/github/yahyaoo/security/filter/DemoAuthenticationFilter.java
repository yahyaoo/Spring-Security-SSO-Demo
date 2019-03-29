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

package com.github.yahyaoo.security.filter;

import com.github.yahyaoo.security.token.DemoAuthenticationToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import java.util.Iterator;
import java.util.Set;

/**
 * Authorization request Filter
 *
 * @author yahyaoo
 * @date 2019/2/25 13:16
 */
public class DemoAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String SPRING_SECURITY_FORM_USERNAME_OR_MAIL_KEY = "integration";
    private static final String SPRING_SECURITY_FORM_MAIL_KEY = "mail";

    private String integrationParameter = SPRING_SECURITY_FORM_USERNAME_OR_MAIL_KEY;
    private String mailParameter = SPRING_SECURITY_FORM_MAIL_KEY;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.toString().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws AuthenticationServiceException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String integration = obtainIntegration(request);
        String mail = obtainMail(request);

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (integration == null) {
            integration = "";
        }
        if (mail == null) {
            mail = "";
        }

        validateParameter(new UserDetails(username, password, mail, integration));

        return new DemoAuthenticationToken(username, password, integration, mail);

    }

    private String obtainIntegration(HttpServletRequest request) {
        return request.getParameter(integrationParameter);
    }

    private String obtainMail(HttpServletRequest request) {
        return request.getParameter(mailParameter);
    }

    /**
     * This is where you can inject a
     * {@link org.springframework.security.web.authentication.AuthenticationFailureHandler} implement type and jump to
     * the failed information display page
     *
     * @param userDetails userDetails
     * @throws AuthenticationServiceException AuthenticationServiceException
     */
    private void validateParameter(UserDetails userDetails) throws AuthenticationServiceException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        if (!"".equals(userDetails.getMail())) {
            Set<ConstraintViolation<UserDetails>> validate = validator.validate(userDetails);
            Iterator<ConstraintViolation<UserDetails>> iterator = validate.iterator();
            if (iterator.hasNext()) {
                throw new AuthenticationServiceException(iterator.next().getMessage());
            }
        }
    }

    @Data
    @AllArgsConstructor
    private class UserDetails {
        private String username;
        private String password;
        @Email
        private String mail;
        private String integration;
    }

}
