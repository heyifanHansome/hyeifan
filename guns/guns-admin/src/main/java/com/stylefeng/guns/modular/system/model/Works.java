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
 * 作品管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_works")
@ToString
@Getter
@Setter
public class Works extends Model<Works> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 类型（0：图文，1：视频）
     */
    private String type;
    /**
     * 资源（无序列表）就是图片或视频地址组成的无序列表
     */

    private String images;
    /**
     * 主料（无序列表）
     */
    @TableField("main_ingredient")
    private String mainIngredient;
    /**
     * 辅料（无序列表）
     */
    @TableField("supplementary_material")
    private String supplementaryMaterial;
    /**
     * 调料（无序列表）
     */
    private String seasoning;
    /**
     * 做法
     */
    private String practice;
    /**
     * 做法（无序列表）
     */
    private String remark;
    /**
     * 加入状态（0：未审查，1：通过，2：拒绝）
     */
    private String status;
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
    /**
     * 栏目类型ID为作品的ID
     */
    @TableField("column_id")
    private Integer columnId;

    @TableField("base_id")
    private String baseId;

    private String video;

    @TableField("user_id")
    private String userId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
