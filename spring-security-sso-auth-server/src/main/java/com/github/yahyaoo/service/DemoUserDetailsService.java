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

package com.github.yahyaoo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * User Information service
 *
 * @author yahyaoo
 * @date 2019/3/24 17:26
 */

public interface DemoUserDetailsService extends UserDetailsService {

    /**
     * Find the user through the mailbox
     *
     * @param mail mail
     * @return see {@link UserDetails}
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    UserDetails loadUserByMail(String mail) throws UsernameNotFoundException;

    /**
     * The user can input the mailbox or the user name to inquire the detail
     *
     * @param str A string containing a mailbox or user name
     * @return see {@link UserDetails}
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    UserDetails loadUserByUsernameOrMail(String str) throws UsernameNotFoundException;

    /**
     * The user needs to enter both a username and a mailbox to query for details
     *
     * @param username username
     * @param mail     mail
     * @return see {@link UserDetails}
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    UserDetails loadUserByUsernameAndMail(String username, String mail) throws UsernameNotFoundException;

}
