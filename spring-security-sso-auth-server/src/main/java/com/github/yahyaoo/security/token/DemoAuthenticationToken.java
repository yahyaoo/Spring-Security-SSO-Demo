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

package com.github.yahyaoo.security.token;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author yahyaoo
 * @date 2019/2/25 19:20
 */
public class DemoAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -5224742192267595966L;

    @Getter
    private String integration;

    @Getter
    private String mail;

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     *
     * @param principal   principal
     * @param credentials credentials
     * @param mail        mail
     * @param integration mail Or Username field
     */
    public DemoAuthenticationToken(Object principal, Object credentials, String integration, String mail) {
        super(principal, credentials);
        this.integration = integration;
        this.mail = mail;
    }

    /**
     * This method must exist to modify the user authorization state after authorization by the framework
     *
     * @param principal   principal
     * @param credentials credentials
     * @param integration mail Or Username field
     * @param authorities authorities
     * @param mail        mail
     */
    public DemoAuthenticationToken(Object principal, Object credentials, String integration,
                                   Collection<? extends GrantedAuthority> authorities, String mail) {
        super(principal, credentials, authorities);
        this.integration = integration;
        this.mail = mail;
    }

}
