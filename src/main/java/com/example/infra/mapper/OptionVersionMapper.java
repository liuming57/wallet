package com.example.infra.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.entity.OptionVersion;

import java.util.List;

/**
 * (OptionVersion)应用服务
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
public interface OptionVersionMapper extends BaseMapper<OptionVersion> {
    /**
     * 基础查询
     *
     * @param optionVersion 查询条件
     * @return 返回值
     */
    List<OptionVersion> selectList(OptionVersion optionVersion);
}

