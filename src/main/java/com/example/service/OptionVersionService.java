package com.example.service;


import com.example.domain.entity.OptionVersion;

import java.util.List;

/**
 * (OptionVersion)应用服务
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
public interface OptionVersionService {

    /**
     * 查询数据
     *
     * @param optionVersions 查询条件
     * @return 返回值
     */
    List<OptionVersion> selectList(OptionVersion optionVersions);

    /**
     * 保存数据
     *
     * @param optionVersions 数据
     */
    void saveData(List<OptionVersion> optionVersions);

}

