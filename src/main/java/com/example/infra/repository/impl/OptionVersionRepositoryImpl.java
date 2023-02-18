package com.example.infra.repository.impl;

import com.alibaba.fastjson.JSON;
import com.example.domain.entity.OptionVersion;
import com.example.domain.repository.OptionVersionRepository;
import com.example.infra.mapper.OptionVersionMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * (OptionVersion)资源库
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
@Component
public class OptionVersionRepositoryImpl implements OptionVersionRepository {
    @Resource
    private OptionVersionMapper optionVersionMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<OptionVersion> selectList(OptionVersion optionVersion) {
        List<OptionVersion> list = JSON.parseArray(stringRedisTemplate.opsForValue().get("optionVersion_" + optionVersion.getUserId()), OptionVersion.class);
        if (list != null) {
            // 查询到数据
            return list;
        } else {
            // 缓存中没有，从数据库中查询
            List<OptionVersion> optionVersionList = optionVersionMapper.selectList(optionVersion);
            if (optionVersionList.size() == 0) {
                return null;
            } else {
                // 将查询结果存入到缓存中，同时还可以设置有效期
                stringRedisTemplate.opsForValue().set("optionVersion_" + optionVersion.getUserId(), JSON.toJSONString(optionVersionList), 1, TimeUnit.DAYS);
                return optionVersionList;
            }
        }
    }

    @Override
    public OptionVersion selectByPrimary(Long id) {
        OptionVersion optionVersion = new OptionVersion();
        optionVersion.setId(id);
        List<OptionVersion> optionVersions = optionVersionMapper.selectList(optionVersion);
        if (optionVersions.size() == 0) {
            return null;
        }
        return optionVersions.get(0);
    }

}

