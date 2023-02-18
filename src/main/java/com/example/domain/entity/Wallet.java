package com.example.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (Wallet)实体类
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Wallet implements Serializable {
    private static final long serialVersionUID = 181666577092787807L;

    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_MONEY = "money";
    public static final String FIELD_DELETE_FLAG = "deleteFlag";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_TIME = "creationTime";
    public static final String FIELD_LAST_UPDATE_BY = "lastUpdateBy";
    public static final String FIELD_LAST_UPDATE_TIME = "lastUpdateTime";
    public static final String FIELD_OBJECT_VERSION = "objectVersion";

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private BigDecimal money;

    @TableField(value = "0")
    private Boolean deleteFlag;

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

    @TableField(exist = false)
    @NotNull
    private Long optionId;
}

