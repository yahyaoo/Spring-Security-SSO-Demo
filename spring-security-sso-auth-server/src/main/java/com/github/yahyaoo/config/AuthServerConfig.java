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

import com.github.module.config.EnableCustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * Authorization server configuration
 *
 * @author yahayoo
 * @date 2019/3/21 16:32
 */
@Configuration
@EnableAuthorizationServer
@EnableCustomUserDetails
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;
    private final DataSource dataSource;

    @Autowired
    public AuthServerConfig(PasswordEncoder passwordEncoder,
                            JwtAccessTokenConverter jwtAccessTokenConverter,
                            TokenStore tokenStore,
                            AuthenticationConfiguration authenticationConfiguration,
                            DataSource dataSource) throws Exception {
        this.passwordEncoder = passwordEncoder;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.dataSource = dataSource;
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .jdbc(dataSource)
                .passwordEncoder(passwordEncoder);

        /*The client information is stored in memory*/

//        clients.inMemory()
//                .withClient("SampleClientId")
//                .secret(passwordEncoder.encode("secret"))
//                .authorizedGrantTypes("authorization_code")
//                .scopes("user_info")
//                .autoApprove(true)
//                .redirectUris("http://localhost:8082/ui/login", "http://localhost:8083/ui2/login", "http://localhost:8082/login")
//                // 1 hour
//                .accessTokenValiditySeconds(3600)
//        ;

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter);
    }

}
