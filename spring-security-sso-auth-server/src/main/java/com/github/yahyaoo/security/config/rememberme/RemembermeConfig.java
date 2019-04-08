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

package com.github.yahyaoo.security.config.rememberme;

import com.github.yahyaoo.security.rememberme.RedisPoolTokenRepositoryImpl;
import com.github.yahyaoo.security.rememberme.RedisTokenRepository;
import com.github.yahyaoo.security.token.RedisRememberMeToken;
import com.github.yahyaoo.util.RedisCommandsExt;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author yahyaoo
 * @date 2019/4/7 18:45
 */
@Data
@Component
@ConfigurationProperties(prefix = "application.rememberme")
public class RemembermeConfig {

    private Integer expire;

    private String key;

    @Bean
    @ConditionalOnMissingBean(RedisTokenRepository.class)
    public RedisTokenRepository redisPoolTokenRepository(RedisTemplate<String, RedisRememberMeToken> redisTemplate,
                                                         RedisCommandsExt<RedisRememberMeToken> redisCommandsExt) {
        return new RedisPoolTokenRepositoryImpl(redisTemplate, redisCommandsExt, expire);
    }

}
