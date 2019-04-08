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

import com.github.yahyaoo.security.token.RedisRememberMeToken;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

/**
 * The abstraction used by {@link RedisRememberMeServices} to store the
 * persistent login tokens for a user.
 *
 * @author yahyaoo
 * @date 2019/4/7 9:45
 * @see JdbcTokenRepositoryImpl
 * @see InMemoryTokenRepositoryImpl
 */
public interface RedisTokenRepository {

    void createNewToken(RedisRememberMeToken token);

    void updateToken(RedisRememberMeToken oldToken, RedisRememberMeToken newToken);

    RedisRememberMeToken getTokenForSeries(String seriesId);

    void removeUserTokens(RedisRememberMeToken token);

}
