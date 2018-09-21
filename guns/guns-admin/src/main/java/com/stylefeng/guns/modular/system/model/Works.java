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
 * 作品管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_works")
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public String getSupplementaryMaterial() {
        return supplementaryMaterial;
    }

    public void setSupplementaryMaterial(String supplementaryMaterial) {
        this.supplementaryMaterial = supplementaryMaterial;
    }

    public String getSeasoning() {
        return seasoning;
    }

    public void setSeasoning(String seasoning) {
        this.seasoning = seasoning;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Works{" +
        "id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", images=" + images +
        ", mainIngredient=" + mainIngredient +
        ", supplementaryMaterial=" + supplementaryMaterial +
        ", seasoning=" + seasoning +
        ", practice=" + practice +
        ", remark=" + remark +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", columnId=" + columnId +
        "}";
    }
}
