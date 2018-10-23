package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 首页跳转版块
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-23
 */
@TableName("sys_jump_page")
public class JumpPage extends Model<JumpPage> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 背景图
     */
    private String picture;
    /**
     * 阿里云OSS删除的key
     */
    @TableField("object_name")
    private String objectName;
    /**
     * 排序值
     */
    private String orders;
    /**
     * 跳转页面代号(详情见接口文档)
     */
    private String code;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "JumpPage{" +
        "id=" + id +
        ", picture=" + picture +
        ", objectName=" + objectName +
        ", orders=" + orders +
        ", code=" + code +
        "}";
    }
}
