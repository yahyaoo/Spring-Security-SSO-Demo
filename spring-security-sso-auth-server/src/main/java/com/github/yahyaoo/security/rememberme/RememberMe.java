/*
 *  Copyright 2019 Yahyaoo.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.yahyaoo.security.rememberme;

import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

/**
 * @author yuhong
 * @date 2019/4/6 13:58
 */
public class RememberMe {

    private static RememberMe rememberMe;

    @Getter
    private PersistentTokenBasedRememberMeServices services;

    private RememberMe(UserDetailsService userDetailsService) {
        services = new PersistentTokenBasedRememberMeServices("Yahyaoo", userDetailsService,
                new InMemoryTokenRepositoryImpl());
    }

    public static RememberMe getInstance(UserDetailsService userDetailsService) {
        if (rememberMe == null) {
            rememberMe = new RememberMe(userDetailsService);
        }
        return rememberMe;
    }

}
