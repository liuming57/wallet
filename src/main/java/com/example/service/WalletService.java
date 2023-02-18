package com.example.service;


import com.example.domain.entity.Wallet;

import java.util.List;

/**
 * (Wallet)应用服务
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
public interface WalletService {

    /**
     * 查询数据
     *
     * @param wallets 查询条件
     * @return 返回值
     */
    List<Wallet> selectList(Wallet wallets);

    /**
     * 保存数据
     *
     * @param wallets 数据
     */
    void saveData(List<Wallet> wallets);

}

