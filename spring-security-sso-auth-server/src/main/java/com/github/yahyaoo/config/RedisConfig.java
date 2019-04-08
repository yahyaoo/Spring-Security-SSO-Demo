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

package com.github.yahyaoo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author yahyaoo
 * @date 2019/3/31 16:06
 */
@Configuration
public class RedisConfig {

    // ~ Static fields/initializers
    // =====================================================================================

    private static final String REDIS_CONNECTION_FACTORY_BEAN_NAME = "redisConnectionFactory";

    // ~ Instance fields
    // ==========================================================================================

    private final RedisProperties redisProperties;

    // ~ Constructors
    // =============================================================================================

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    // ~ Methods
    // =======================================================================================================

    @Primary
    @Bean(name = RedisConfig.REDIS_CONNECTION_FACTORY_BEAN_NAME)
    public RedisConnectionFactory getRedisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort()));
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(RedisTemplate.class)
    @DependsOn(RedisConfig.REDIS_CONNECTION_FACTORY_BEAN_NAME)
    public RedisTemplate getRedisTemplate(RedisConnectionFactory factory) {

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(factory);

        template.setDefaultSerializer(serializer);

        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        template.afterPropertiesSet();

        return template;
    }

}
