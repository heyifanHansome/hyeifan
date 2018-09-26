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
 * 图片表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-26
 */
@TableName("sys_picture")
public class Picture extends Model<Picture> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *  创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人 
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 外键id
     */
    @TableField("base_id")
    private String baseId;
    /**
     * 图片名称
     */
    private String picturename;
    /**
     * 相对路径
     */
    private String relativepath;
    /**
     * 服务器路径
     */
    private String serverpath;
    /**
     * 绝对路径
     */
    private String absolutepath;
    /**
     * 类型
     */
    private String type;
    /**
     * 后缀
     */
    private String suffixname;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }

    public String getRelativepath() {
        return relativepath;
    }

    public void setRelativepath(String relativepath) {
        this.relativepath = relativepath;
    }

    public String getServerpath() {
        return serverpath;
    }

    public void setServerpath(String serverpath) {
        this.serverpath = serverpath;
    }

    public String getAbsolutepath() {
        return absolutepath;
    }

    public void setAbsolutepath(String absolutepath) {
        this.absolutepath = absolutepath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuffixname() {
        return suffixname;
    }

    public void setSuffixname(String suffixname) {
        this.suffixname = suffixname;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Picture{" +
        "id=" + id +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", baseId=" + baseId +
        ", picturename=" + picturename +
        ", relativepath=" + relativepath +
        ", serverpath=" + serverpath +
        ", absolutepath=" + absolutepath +
        ", type=" + type +
        ", suffixname=" + suffixname +
        "}";
    }
}
