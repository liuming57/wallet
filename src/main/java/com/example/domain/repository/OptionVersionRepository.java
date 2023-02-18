package com.example.domain.repository;

import com.example.domain.entity.OptionVersion;

import java.util.List;

/**
 * (OptionVersion)资源库
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
public interface OptionVersionRepository {
    /**
     * 查询
     *
     * @param optionVersion 查询条件
     * @return 返回值
     */
    List<OptionVersion> selectList(OptionVersion optionVersion);

    /**
     * 根据主键查询（可关联表）
     *
     * @param id 主键
     * @return 返回值
     */
    OptionVersion selectByPrimary(Long id);
}
