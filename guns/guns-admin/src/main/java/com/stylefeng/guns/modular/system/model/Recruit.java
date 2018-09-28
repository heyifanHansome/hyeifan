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
 * 招聘管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_recruit")
@Getter
@Setter
@ToString
public class Recruit extends Model<Recruit> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 栏目类型ID为招聘的ID
     */
    @TableField("column_id")
    private String columnId;
    /**
     * 招聘名
     */
    private String title;
    /**
     * 缩略图
     */
    private String thumb;
    /**
     * 描述
     */
    private String description;
    /**
     * 城市ID（0：表示全国）
     */
    @TableField("city_id")
    private String cityId;
    /**
     * 工作详细地址
     */
    private String address;
    /**
     * 来源ID（0：官方，1：个人）
     */
    @TableField("source_id")
    private String sourceId;
    /**
     * 当是官方时为管理员的ID，是个人时，表示用户的ID
     */
    private String uid;
    /**
     * 发布IP
     */
    @TableField("publish_ip")
    private String publishIp;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;
    /**
     * 阿里云存储对象名称
     */
    private String object_name;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
