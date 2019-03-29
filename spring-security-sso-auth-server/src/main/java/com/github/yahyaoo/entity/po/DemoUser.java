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

package com.github.yahyaoo.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * User information entity
 *
 * @author yahyaoo
 * @date 2019/3/24 17:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DemoUser extends User {

    private static final long serialVersionUID = 4350346246815768912L;

    private Integer userId;

    private String userMail;

    private String userAddress;

    private String userTel;

    private Date userBirthday;

    private Date userCreated;

    private Date userUpdated;

    /**
     * <p>
     * In a real production environment, you would have to make the authorization field final,
     * And you must initialize the field in the constructor,
     * and since the superclass has no argument constructs,
     * you would have to customize a PO object that implements {@link UserDetails}.
     * </p>
     * <pre>for example: {@link User}</pre>
     */
    private Set<GrantedAuthority> authorities;

    /**
     * If you need to set permissions, query from the database and fill in the permissions.This is just a sample
     * program.
     * <p>
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     */
    @AutomapConstructor
    public DemoUser(Integer userId, String username, String userMail, String password, String userAddress, String userTel,
                    Date userBirthday,
                    Date userCreated, Date userUpdated) {
        super(username, password, new ArrayList<>());
        this.userId = userId;
        this.userMail = userMail;
        this.userAddress = userAddress;
        this.userTel = userTel;
        this.userBirthday = userBirthday;
        this.userCreated = userCreated;
        this.userUpdated = userUpdated;
    }

    public DemoUser(Integer userId, String username, String userMail, String password, String userAddress, String userTel,
                    Date userBirthday,
                    Date userCreated, Date userUpdated, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.userMail = userMail;
        this.userAddress = userAddress;
        this.userTel = userTel;
        this.userBirthday = userBirthday;
        this.userCreated = userCreated;
        this.userUpdated = userUpdated;
    }

}
