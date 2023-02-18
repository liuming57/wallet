package com.example.service.impl;

import com.example.domain.entity.OptionVersion;
import com.example.domain.repository.OptionVersionRepository;
import com.example.service.OptionVersionService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (OptionVersion)应用服务
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
@Service
public class OptionVersionServiceImpl implements OptionVersionService {
    @Autowired
    private OptionVersionRepository optionVersionRepository;

    @Override
    public List<OptionVersion> selectList(OptionVersion optionVersion) {
        return optionVersionRepository.selectList(optionVersion);
    }

    @Override
    public void saveData(List<OptionVersion> optionVersions) {
        List<OptionVersion> insertList = optionVersions.stream().filter(line -> line.getId() == null).collect(Collectors.toList());
        List<OptionVersion> updateList = optionVersions.stream().filter(line -> line.getId() != null).collect(Collectors.toList());

    }
}

