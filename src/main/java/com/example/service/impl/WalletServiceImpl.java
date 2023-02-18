package com.example.service.impl;


import com.example.domain.entity.Wallet;
import com.example.domain.repository.WalletRepository;
import com.example.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Wallet)应用服务
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public List<Wallet> selectList(Wallet wallet) {
        return walletRepository.selectList(wallet);
    }

    @Override
    public void saveData(List<Wallet> wallets) {
        List<Wallet> insertList = wallets.stream().filter(line -> line.getId() == null).collect(Collectors.toList());
        List<Wallet> updateList = wallets.stream().filter(line -> line.getId() != null).collect(Collectors.toList());
        walletRepository.batchUpdate(updateList);
        walletRepository.batchInsert(insertList);
    }
}

