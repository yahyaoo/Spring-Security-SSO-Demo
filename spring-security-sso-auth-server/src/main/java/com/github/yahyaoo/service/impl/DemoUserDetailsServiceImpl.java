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

package com.github.yahyaoo.service.impl;

import com.github.yahyaoo.entity.po.DemoUser;
import com.github.yahyaoo.entity.po.DemoUserExample;
import com.github.yahyaoo.mapper.DemoUserMapper;
import com.github.yahyaoo.service.DemoUserDetailsService;
import com.github.yahyaoo.util.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User Information service implement
 *
 * @author yahyaoo
 * @date 2019/2/25 11:54
 */
@Service
@Primary
public class DemoUserDetailsServiceImpl implements DemoUserDetailsService {

    private final DemoUserMapper demoUserMapper;

    @Autowired
    public DemoUserDetailsServiceImpl(DemoUserMapper demoUserMapper) {
        this.demoUserMapper = demoUserMapper;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        DemoUserExample demoUserExample = new DemoUserExample();
        DemoUserExample.Criteria criteria = demoUserExample.createCriteria();
        criteria.andUserNameEqualTo(username);
        List<DemoUser> demoUsers = this.demoUserMapper.selectByExample(demoUserExample);

        if (ListUtils.isEmpty(demoUsers)) {
            throw new UsernameNotFoundException(String.format("Username not found for username=%s", username));
        }

        DemoUser demoUser = demoUsers.get(0);
        demoUser.setAuthorities(getAuthority());

        return demoUser;
    }

    /**
     * Find the user through the mailbox
     *
     * @param mail mail
     * @return see {@link UserDetails}
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByMail(String mail) throws UsernameNotFoundException {

        DemoUserExample demoUserExample = new DemoUserExample();
        DemoUserExample.Criteria criteria = demoUserExample.createCriteria();
        criteria.andUserMailEqualTo(mail);
        List<DemoUser> demoUsers = this.demoUserMapper.selectByExample(demoUserExample);

        if (ListUtils.isEmpty(demoUsers)) {
            throw new UsernameNotFoundException(String.format("mail not found for mail=%s", mail));
        }

        DemoUser demoUser = demoUsers.get(0);
        demoUser.setAuthorities(getAuthority());

        return demoUser;
    }

    /**
     * The user can input the mailbox or the user name to inquire the detail
     *
     * @param str A string containing a mailbox or user name
     * @return see {@link UserDetails}
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsernameOrMail(String str) throws UsernameNotFoundException {

        final String search = "@";
        UserDetails userDetails;
        if (StringUtils.contains(str, search)) {
            //  if the str contain @
            userDetails = this.loadUserByMail(str);
        } else {
            userDetails = this.loadUserByUsername(str);
        }

        return userDetails;
    }

    /**
     * The user needs to enter both a username and a mailbox to query for details
     *
     * @param username username
     * @param mail     mail
     * @return see {@link UserDetails}
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsernameAndMail(String username, String mail) throws UsernameNotFoundException {

        DemoUser userDetails = (DemoUser) this.loadUserByMail(mail);

        if (!userDetails.getUserMail().equals(mail)) {
            throw new UsernameNotFoundException(String.format("mail and username not found for mail=%s,username=%s",
                    mail, username));
        }

        return userDetails;
    }

    /**
     * If you need to set permissions, query from the database and fill in the permissions.This is just a sample
     * program.
     *
     * @return see {@link GrantedAuthority}
     */
    private Set<GrantedAuthority> getAuthority() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

}
