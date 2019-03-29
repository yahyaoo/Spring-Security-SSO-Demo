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

package com.github.module.config;

import com.github.module.security.converter.DemoUserAuthenticationConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.security.KeyPair;

/**
 * Register a custom {@link JwtAccessTokenConverter} bean
 *
 * @author yahyaoo
 * @date 2019/3/28 8:56
 */
@Configuration
public class DemoUserDetailsConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(KeyPair.class)
    public JwtAccessTokenConverter jwtAccessTokenConverter(JwtAccessTokenConverter jwtAccessTokenConverter) {
        //Custom return token information
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(new DemoUserAuthenticationConverter());
        jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter);
        return jwtAccessTokenConverter;
    }

    @Bean
    @Primary
    @ConditionalOnBean(KeyPair.class)
    public JwtAccessTokenConverter jwtAccessTokenConverter(KeyPair keyPair) {
//        JWT Config
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);

        //Custom return token information
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(new DemoUserAuthenticationConverter());
        converter.setAccessTokenConverter(defaultAccessTokenConverter);

        return converter;
    }

}
