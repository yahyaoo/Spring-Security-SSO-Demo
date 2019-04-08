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

package com.github.yahyaoo.security.config.oauth2.keypair;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

/**
 * keyPair Management
 *
 * @author yahyaoo
 * @date 2019/2/13 13:31
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "keypair")
public class KeyPairConfig {

    private String fileName;
    private String password;
    private String alias;

    @Bean
    @Primary
    public TokenStore jwtTokenStore(KeyPair keyPair) {
        return new JwtTokenStore(jwtAccessTokenConverter(keyPair));
    }

    @Bean
    @ConditionalOnMissingBean(JwtAccessTokenConverter.class)
    public JwtAccessTokenConverter jwtAccessTokenConverter(KeyPair keyPair) {
//        JWT Config
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(fileName), password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(alias);
    }

}
