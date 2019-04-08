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

package com.github.yahyaoo;

import com.github.yahyaoo.util.RedisCommandsExt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.List;

/**
 * @author yahyaoo
 * @date 2019/3/28 15:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoTest {

    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisCommandsExt redisCommandsExt;
    @Autowired
    private KeyPair keyPair;

    @Test
    public void demoTest() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("secret"));
    }

    @Test
    public void demo() {
        redisTemplate.opsForValue().set("demo", redisTemplate.toString());
    }

    @Test
    public void demo2() {
        List<String> scan = redisCommandsExt.scanGetKeys(Integer.MAX_VALUE, "*:userinfo*");
        logger.info("start");
        for (String s : scan) {
            logger.info(s);
        }
    }

    @Test
    public void demo3() {
        RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyPair.getPrivate();
        logger.debug(privateKey);
    }
}
