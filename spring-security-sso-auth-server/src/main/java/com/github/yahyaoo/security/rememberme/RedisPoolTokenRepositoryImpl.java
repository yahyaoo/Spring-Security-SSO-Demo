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
import com.github.yahyaoo.util.RedisCommandsExt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yahyaoo
 * @date 2019/4/7 9:48
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RedisPoolTokenRepositoryImpl implements RedisTokenRepository {

    // ~ Static fields/initializers
    // =====================================================================================

    public final static String USER_INFO_REDIS_PREFIX_KEY = "user";
    public final static String DELIMITER = ":";

    // ~ Instance field
    // =============================================================================

    private final RedisCommandsExt<RedisRememberMeToken> redisCommandsExt;
    private final RedisTemplate<String, RedisRememberMeToken> redisTemplate;

    private final Log logger = LogFactory.getLog(getClass());

    private Integer expire;

    // ~ Constructors
    // ==================================================================================================

    public RedisPoolTokenRepositoryImpl(RedisCommandsExt<RedisRememberMeToken> redisCommandsExt, RedisTemplate<String, RedisRememberMeToken> redisTemplate) {
        this.redisCommandsExt = redisCommandsExt;
        this.redisTemplate = redisTemplate;
    }

    public RedisPoolTokenRepositoryImpl(RedisTemplate<String, RedisRememberMeToken> redisTemplate,
                                        RedisCommandsExt<RedisRememberMeToken> redisCommandsExt,
                                        Integer expire) {
        this.redisTemplate = redisTemplate;
        this.redisCommandsExt = redisCommandsExt;
        this.expire = expire;
    }

    // ~ Method
    // ================================================================================================

    @Override
    public void createNewToken(RedisRememberMeToken token) {
        RedisRememberMeToken current = redisTemplate.opsForValue().get(getKey(token));

        if (current != null) {
            throw new DataIntegrityViolationException("Series Id '" + token.getSeries() + "' already exists!");
        }

        logger.debug("To start creating a new token:" + token.toString());

        final String key = getKey(token);
        redisTemplate.opsForValue().set(key, token);
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);

    }

    @Override
    public void updateToken(RedisRememberMeToken oldToken, RedisRememberMeToken newToken) {
        // Store it, overwriting the existing one.
        final String key = getKey(oldToken);
        redisTemplate.opsForValue().set(key, newToken);
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public RedisRememberMeToken getTokenForSeries(String seriesId) {
        final String key = getKey(new RedisRememberMeToken(null, null, seriesId, null, null));

        List<RedisRememberMeToken> tokens = redisCommandsExt.scanGetValues(1, key);

        if (CollectionUtils.isEmpty(tokens)) {
            return null;
        }

        logger.debug("Get the user's previous login information:" + tokens.get(0).toString());
        return tokens.get(0);
    }

    @Override
    public void removeUserTokens(RedisRememberMeToken token) {

        if (StringUtils.isNotEmpty(token.getMail()) && StringUtils.isNotEmpty(token.getSeries())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Start deleting user information :mail: " + token.getMail() + ".Series: " + token.getSeries());
            }
            redisTemplate.delete(getKey(token));
            return;
        }

        List<String> keys = redisCommandsExt.scanGetKeys(Integer.MAX_VALUE, getKey(token));
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Start deleting user information:" + keys.toString() + ".Total:" + keys.size());
        }
        redisTemplate.delete(keys);
    }

    protected String getKey(RedisRememberMeToken token) {

        if (StringUtils.isEmpty(token.getSeries())) {
            return USER_INFO_REDIS_PREFIX_KEY + DELIMITER + token.getMail() + DELIMITER + "*";
        }

        if (StringUtils.isEmpty(token.getMail())) {
            return USER_INFO_REDIS_PREFIX_KEY + DELIMITER + "*" + DELIMITER + token.getSeries();
        }

        return USER_INFO_REDIS_PREFIX_KEY + DELIMITER + token.getMail() + DELIMITER + token.getSeries();
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

}
