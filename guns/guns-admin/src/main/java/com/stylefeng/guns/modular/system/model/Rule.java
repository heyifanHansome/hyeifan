package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 收徒详情
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-30
 */

@Getter
@Setter
@ToString
@TableName("sys_rule")
public class Rule extends Model<Rule> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 规则
     */
    private String rule;
    /**
     * html5
     */
    private String content;


    @TableField("create_time")
    private Date createTime;


    @TableField("update_time")
    private Date updateTime;

    @TableField("create_by")
    private Date createBy;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
