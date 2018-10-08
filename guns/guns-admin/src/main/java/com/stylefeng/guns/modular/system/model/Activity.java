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
 * 活动管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
@TableName("sys_activity")
@Getter
@Setter
@ToString
public class Activity extends Model<Activity> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 栏目类型ID为活动的ID
     */
    @TableField("column_id")
    private Integer columnId;
    /**
     * 活动名
     */
    private String title;
    /**
     * 缩略图
     */
    private String thumb;
    /**
     * 描述（本期看点）
     */
    private String description;
    /**
     * 开始时间(在活动开始之前都可报名，通过这个来判断即将开始、进行中、已结束等状态)
     */
    @TableField("start_time")
    private Date startTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 城市ID（0：表示全国）
     */
    @TableField("city_id")
    private String cityId;
    /**
     * 来源ID（0：官方，1：个人）
     */
    @TableField("source_id")
    private Integer sourceId;
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
     * 内容（活动内容）
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
    @TableField("update_time")
    private Date updateTime;

    private String object_name;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
