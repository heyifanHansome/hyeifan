package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 系统设置表
 * </p>
 *
 * @author lijun
 * @since 2018-10-15
 */
@TableName("sys_setting")
public class Setting extends Model<Setting> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 阿里云实名认证的appcode
     */
    @TableField("ali_safrv_cert_check_appcode")
    private String aliSafrvCertCheckAppcode;
    /**
     * 阿里云云存储endpoint
     */
    @TableField("ali_oss_endpoint")
    private String aliOssEndpoint;
    /**
     * 阿里云云存储bucket
     */
    @TableField("ali_oss_bucket")
    private String aliOssBucket;
    /**
     * 阿里云云存储access_key
     */
    @TableField("ali_oss_access_key")
    private String aliOssAccessKey;
    /**
     * 阿里云云存储access_id
     */
    @TableField("ali_oss_access_id")
    private String aliOssAccessId;
    /**
     * 阿里云云存储图片存储路径
     */
    @TableField("ali_oss_img_path")
    private String aliOssImgPath;
    /**
     * 阿里云云存储文件存储路径
     */
    @TableField("ali_oss_file_path")
    private String aliOssFilePath;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAliSafrvCertCheckAppcode() {
        return aliSafrvCertCheckAppcode;
    }

    public void setAliSafrvCertCheckAppcode(String aliSafrvCertCheckAppcode) {
        this.aliSafrvCertCheckAppcode = aliSafrvCertCheckAppcode;
    }

    public String getAliOssEndpoint() {
        return aliOssEndpoint;
    }

    public void setAliOssEndpoint(String aliOssEndpoint) {
        this.aliOssEndpoint = aliOssEndpoint;
    }

    public String getAliOssBucket() {
        return aliOssBucket;
    }

    public void setAliOssBucket(String aliOssBucket) {
        this.aliOssBucket = aliOssBucket;
    }

    public String getAliOssAccessKey() {
        return aliOssAccessKey;
    }

    public void setAliOssAccessKey(String aliOssAccessKey) {
        this.aliOssAccessKey = aliOssAccessKey;
    }

    public String getAliOssAccessId() {
        return aliOssAccessId;
    }

    public void setAliOssAccessId(String aliOssAccessId) {
        this.aliOssAccessId = aliOssAccessId;
    }

    public String getAliOssImgPath() {
        return aliOssImgPath;
    }

    public void setAliOssImgPath(String aliOssImgPath) {
        this.aliOssImgPath = aliOssImgPath;
    }

    public String getAliOssFilePath() {
        return aliOssFilePath;
    }

    public void setAliOssFilePath(String aliOssFilePath) {
        this.aliOssFilePath = aliOssFilePath;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Setting{" +
        "id=" + id +
        ", aliSafrvCertCheckAppcode=" + aliSafrvCertCheckAppcode +
        ", aliOssEndpoint=" + aliOssEndpoint +
        ", aliOssBucket=" + aliOssBucket +
        ", aliOssAccessKey=" + aliOssAccessKey +
        ", aliOssAccessId=" + aliOssAccessId +
        ", aliOssImgPath=" + aliOssImgPath +
        ", aliOssFilePath=" + aliOssFilePath +
        "}";
    }
}
