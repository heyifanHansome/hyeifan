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
 * 用户简历管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_user_resume")
public class UserResume extends Model<UserResume> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 状态（0：离职-随时到岗，1：在职-暂不考虑，2：在职-考虑机会，3：在职-月内到岗）
     */
    @TableField("apply_status")
    private String applyStatus;
    /**
     * 性别（0：未知，1：男，2：女）
     */
    private String sex;
    /**
     * 会员生日
     */
    private Integer birthday;
    /**
     * 开始工作日期（工龄）
     */
    @TableField("work_time")
    private Date workTime;
    /**
     * 微信号（选填）
     */
    @TableField("wechat_id")
    private Integer wechatId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱（选填）
     */
    private String email;
    /**
     * 优势
     */
    private String advantage;
    /**
     * 无序列表（薪资要求、期望城市）
     */
    private String info;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public Date getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Date workTime) {
        this.workTime = workTime;
    }

    public Integer getWechatId() {
        return wechatId;
    }

    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
        return "UserResume{" +
        "id=" + id +
        ", userId=" + userId +
        ", name=" + name +
        ", applyStatus=" + applyStatus +
        ", sex=" + sex +
        ", birthday=" + birthday +
        ", workTime=" + workTime +
        ", wechatId=" + wechatId +
        ", phone=" + phone +
        ", email=" + email +
        ", advantage=" + advantage +
        ", info=" + info +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
