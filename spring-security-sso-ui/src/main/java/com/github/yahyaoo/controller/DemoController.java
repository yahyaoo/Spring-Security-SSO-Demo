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

package com.github.yahyaoo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author yuhong
 * @date 2019/3/22 17:51
 */
@RestController
public class DemoController {

    @GetMapping("/info")
    @Secured(value = "ROLE_USER")
    public Principal demo(Principal principal) {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
        oAuth2Authentication.getName();
        return principal;
    }

}
