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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * The user information endpoint
 *
 * @author yahyaoo
 * @date 2019/3/28 9:59
 */
@RestController
public class UserController {

    @RequestMapping(value = "/user/me", produces = "application/json; charset=utf-8")
    public Principal user(Principal principal) {
        return principal;
    }
    
}
