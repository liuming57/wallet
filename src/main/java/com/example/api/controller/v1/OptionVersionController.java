package com.example.api.controller.v1;

import com.example.domain.entity.OptionVersion;
import com.example.domain.repository.OptionVersionRepository;
import com.example.utils.Results;
import com.example.service.OptionVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (OptionVersion)表控制层
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */

@RestController("optionVersionSiteController.v1")
@RequestMapping("/v1/option-versions")
public class OptionVersionController {

    @Autowired
    private OptionVersionRepository optionVersionRepository;

    @Autowired
    private OptionVersionService optionVersionService;

    @GetMapping
    public ResponseEntity<List<OptionVersion>> list(OptionVersion optionVersion) {
        List<OptionVersion> list = optionVersionService.selectList(optionVersion);
        return Results.success(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionVersion> detail(@PathVariable Long id) {
        OptionVersion optionVersion = optionVersionRepository.selectByPrimary(id);
        return Results.success(optionVersion);
    }

    @PostMapping
    public ResponseEntity<List<OptionVersion>> save(@RequestBody List<OptionVersion> optionVersions) {
        optionVersionService.saveData(optionVersions);
        return Results.success(optionVersions);
    }

    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<OptionVersion> optionVersions) {
        return Results.success();
    }

}

