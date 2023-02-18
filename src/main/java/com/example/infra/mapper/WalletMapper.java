package com.example.infra.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.entity.Wallet;

import java.util.List;

/**
 * (Wallet)应用服务
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
public interface WalletMapper extends BaseMapper<Wallet> {
    /**
     * 基础查询
     *
     * @param wallet 查询条件
     * @return 返回值
     */
    List<Wallet> selectList(Wallet wallet);
}

