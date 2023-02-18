package com.example.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * (OptionVersion)实体类
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class OptionVersion {
    private static final long serialVersionUID = 181666577092787807L;

    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_BANK_ID = "bankId";
    public static final String FIELD_OPTION_ID = "optionId";
    public static final String FIELD_OPTION_MONEY = "optionMoney";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_TIME = "creationTime";
    public static final String FIELD_LAST_UPDATE_BY = "lastUpdateBy";

    public static final String FIELD_LAST_UPDATE_TIME = "lastUpdateTime";
    public static final String FIELD_OBJECT_VERSION = "objectVersion";

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long bankId;

    private Long optionId;

    private BigDecimal optionMoney;

    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private Date creationTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long lastUpdateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long objectVersion;
}

