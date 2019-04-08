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

package com.github.yahyaoo.util;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yahyaoo
 * @date 2019/4/7 16:53
 */
@Component
public class RedisCommandsExt<T> {

    private final RedisTemplate redisTemplate;

    public RedisCommandsExt(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<String> scanGetKeys(int count, String pattern) {

        Object executeResult = redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            List<String> result = new ArrayList<>();
            Cursor<byte[]> scan = connection.scan(new ScanOptions.ScanOptionsBuilder().count(count).match(pattern).build());
            while (scan.hasNext()) {
                result.add(new String(scan.next()));
            }
            return result;
        });

        return (List<String>) executeResult;
    }

    @SuppressWarnings({"unchecked"})
    public List<T> scanGetValues(int count, String pattern) {

        Object executeResult = redisTemplate.execute((RedisCallback<List<T>>) connection -> {
            List<T> result = new ArrayList<>();
            Cursor<byte[]> scan = connection.scan(new ScanOptions.ScanOptionsBuilder().count(count).match(pattern).build());
            while (scan.hasNext()) {
                T obj = (T) redisTemplate.opsForValue().get(new String(scan.next()));
                result.add(obj);
            }
            return result;
        });

        return (List<T>) executeResult;
    }

}
