package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 系统设置表
 * </p>
 *
 * @author lijun
 * @since 2018-10-15
 */
@Getter
@Setter
@ToString
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
    /**
     * 阿里云appkey
     */
    private String ali_appkey;
    /**
     * 阿里云appSecret
     */
    private String ali_appsecret;
    /**
     * 阿里云实名认证接口appcode
     */
    private String ali_sm_appcode;
    /**
     * 云片网(短信)appkey
     */
    private String yp_appkey;
    /**
     * 高德地图key
     */
    private String gd_key;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
