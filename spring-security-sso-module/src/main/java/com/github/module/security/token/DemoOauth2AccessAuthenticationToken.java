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

package com.github.module.security.token;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author yahyaoo
 * @date 2019/3/26 11:38
 */
public class DemoOauth2AccessAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -8360224555391941929L;

    @Getter
    private UserDetails userDetails;

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     *
     * @param principal   principal
     * @param credentials credentials
     * @param userDetails see {@link UserDetails}
     */
    public DemoOauth2AccessAuthenticationToken(Object principal, Object credentials, UserDetails userDetails) {
        super(principal, credentials);
        this.userDetails = userDetails;
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal   principal
     * @param credentials credentials
     * @param authorities authorities
     * @param userDetails see {@link UserDetails}
     */
    public DemoOauth2AccessAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, UserDetails userDetails) {
        super(principal, credentials, authorities);
        this.userDetails = userDetails;
    }
}
