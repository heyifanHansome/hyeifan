package com.stylefeng.guns.modular.system.model;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 前台用户表
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-10
 */
@TableName("sys_user_api")
@Getter
@Setter
@ToString
public class UserApi extends Model<UserApi> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户自增主键id
     */
    private Integer id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * md5密码盐
     */
    private String salt;
    /**
     * 名字
     */
    private String name;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 性别（1：男 2：女）
     */
    private String sex;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 电话
     */
    private String phone;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 更新时间
     */
    private Date updatetime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
