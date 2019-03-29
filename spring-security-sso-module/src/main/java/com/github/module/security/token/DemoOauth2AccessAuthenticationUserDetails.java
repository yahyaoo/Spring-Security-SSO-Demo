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

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Here is the user information returned to the client, which can be customized, not necessarily the same as the database field
 *
 * @author yahyaoo
 * @date 2019/3/27 8:14
 */
@Data
public class DemoOauth2AccessAuthenticationUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = -3447763991517861708L;

    private Integer userId;

    private String username;

    private String userMail;

    private String password;

    private String userAddress;

    private String userTel;

    private Date userBirthday;

    private Date userCreated;

    private Date userUpdated;

    /**
     * <p>
     * In a real production environment, you would have to make the authorization field final,
     * and since the superclass has no argument constructs,
     * you would have to customize a PO object that implements {@link UserDetails}.
     * </p>
     * <pre>for example: {@link User}</pre>
     */
    private Set<GrantedAuthority> authorities;

    /**
     * Get user information from a given map that contains user information and instantiate the class with that
     * information.
     *
     * @param map Map containing user information
     * @return Instantiates this class that contains user information
     */
    public DemoOauth2AccessAuthenticationUserDetails extractEntity(Map<String, ?> map) {
        Assert.notEmpty(map, "Map cannot be empty");
        Map<String, Field> fields = getFields();
        Class<? extends DemoOauth2AccessAuthenticationUserDetails> clazz = getClass();
        for (Map.Entry<String, ?> detailEntry : map.entrySet()) {
            String key = detailEntry.getKey().trim();
            if (fields.containsKey(key)) {
                Method method = null;
                try {
                    method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(key), fields.get(key).getType());
                    invokeMethodBaseParameterType(method, detailEntry);
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException(String.format("Authorized server return the %s, but you don't " +
                            "find the field or set method in the entity class", key), e);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(String.format("%s Method call failed", method.getName()), e);
                }
            }
        }
        return this;
    }

    /**
     * Matches the type of the method's incoming parameters,
     * allowing the {@link DemoOauth2AccessAuthenticationUserDetails#extractEntity(Map)}  to invoke the method normally
     *
     * @param method      method
     * @param detailEntry Map.Entry<String, ?> map
     * @throws InvocationTargetException InvocationTargetException
     * @throws IllegalAccessException    IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private void invokeMethodBaseParameterType(Method method, Map.Entry<String, ?> detailEntry) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (Set.class.equals(parameterTypes[0])) {
            List<Map<String, ?>> value = (List<Map<String, ?>>) detailEntry.getValue();
            Set<GrantedAuthority> authorities = new HashSet<>();
            for (Map<String, ?> stringMap : value) {
                Map.Entry<String, ?> entry = stringMap.entrySet().iterator().next();
                String authority = (String) entry.getValue();
                authorities.add(new SimpleGrantedAuthority(authority));
            }
            method.invoke(this, authorities);
            return;
        }
        if (Date.class.equals(parameterTypes[0])) {
            method.invoke(this, new Date(Long.parseLong(detailEntry.getValue().toString())));
            return;
        }
//        More types of parameters that need to be deserialization;

        method.invoke(this, detailEntry.getValue());
    }

    /**
     * Gets all non-final fields and non-static for this class
     *
     * @return This class contains fields that are non-final fields and non-static
     */
    private Map<String, Field> getFields() {
        Field[] declaredFields = getClass().getDeclaredFields();
        Map<String, Field> map = new LinkedHashMap<>();
        for (Field declaredField : declaredFields) {
            boolean isFinal = Modifier.isFinal(declaredField.getModifiers());
            boolean isStatic = Modifier.isStatic(declaredField.getModifiers());
            if (!isFinal && !isStatic) {
                map.put(declaredField.getName(), declaredField);
            }
        }
        return map;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
