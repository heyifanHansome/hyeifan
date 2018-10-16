package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 标签管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_tag")
@ToString
@Getter
@Setter
public class Tag extends Model<Tag> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 栏目类型ID（当为0时表示通用标签）
     */
    @TableField("column_id")
    private String columnId;
    /**
     * 标签名
     */
    private String name;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    private String picture;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
