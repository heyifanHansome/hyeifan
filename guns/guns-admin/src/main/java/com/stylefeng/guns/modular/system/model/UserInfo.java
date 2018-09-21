package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户详情表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_user_info")
public class UserInfo extends Model<UserInfo> {

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
     * 密钥
     */
    @TableField("api_token")
    private String apiToken;
    /**
     * 积分
     */
    private Integer credits;
    /**
     * 钱
     */
    private BigDecimal money;
    /**
     * 登陆IP
     */
    @TableField("login_ip")
    private String loginIp;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 结束时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 实名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 身份证
     */
    @TableField("id_card")
    private String idCard;
    /**
     * 城市ID，当为0时表示是全国
     */
    @TableField("city_id")
    private Integer cityId;
    /**
     * 是否加入俱乐部(0：否，1：是)
     */
    @TableField("join_club")
    private Integer joinClub;
    /**
     * 是否开启预约(0：否，1：是)
     */
    private Integer appointment;
    /**
     * 是否开启收徒(0：否，1：是)
     */
    private Integer enlightening;


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

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getJoinClub() {
        return joinClub;
    }

    public void setJoinClub(Integer joinClub) {
        this.joinClub = joinClub;
    }

    public Integer getAppointment() {
        return appointment;
    }

    public void setAppointment(Integer appointment) {
        this.appointment = appointment;
    }

    public Integer getEnlightening() {
        return enlightening;
    }

    public void setEnlightening(Integer enlightening) {
        this.enlightening = enlightening;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
        "id=" + id +
        ", userId=" + userId +
        ", apiToken=" + apiToken +
        ", credits=" + credits +
        ", money=" + money +
        ", loginIp=" + loginIp +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", realName=" + realName +
        ", idCard=" + idCard +
        ", cityId=" + cityId +
        ", joinClub=" + joinClub +
        ", appointment=" + appointment +
        ", enlightening=" + enlightening +
        "}";
    }
}
