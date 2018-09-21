package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
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
    private Integer cityId;
    /**
     * 来源ID（0：官方，1：个人）
     */
    @TableField("source_id")
    private Integer sourceId;
    /**
     * 当是官方时为管理员的ID，是个人时，表示用户的ID
     */
    private Integer uid;
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPublishIp() {
        return publishIp;
    }

    public void setPublishIp(String publishIp) {
        this.publishIp = publishIp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Activity{" +
        "id=" + id +
        ", columnId=" + columnId +
        ", title=" + title +
        ", thumb=" + thumb +
        ", description=" + description +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", cityId=" + cityId +
        ", sourceId=" + sourceId +
        ", uid=" + uid +
        ", publishIp=" + publishIp +
        ", content=" + content +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
