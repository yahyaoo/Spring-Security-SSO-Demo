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

package com.github.yahyaoo.service;

import com.github.yahyaoo.entity.po.DemoUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

/**
 * @author yahyaoo
 * @date 2019/2/26 11:04
 */
@Service
public class TestService {

    @Secured("ROLE_USER")
    public String demo(DemoUser principal) {
        System.out.println(principal.toString());
        return "Hello!";
    }
}
