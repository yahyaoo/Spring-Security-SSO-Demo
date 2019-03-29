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

package com.github.yahyaoo.mapper;

import com.github.yahyaoo.entity.po.DemoUser;
import com.github.yahyaoo.entity.po.DemoUserExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User information dao access layer
 *
 * @author yahyaoo
 * @date 2019/3/24 17:23
 */
@Repository
public interface DemoUserMapper {

    int countByExample(DemoUserExample example);

    int deleteByExample(DemoUserExample example);

    int deleteByPrimaryKey(Integer userId);

    int insert(DemoUser record);

    int insertSelective(DemoUser record);

    List<DemoUser> selectByExample(DemoUserExample example);

    DemoUser selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") DemoUser record, @Param("example") DemoUserExample example);

    int updateByExample(@Param("record") DemoUser record, @Param("example") DemoUserExample example);

    int updateByPrimaryKeySelective(DemoUser record);

    int updateByPrimaryKey(DemoUser record);
}