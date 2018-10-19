package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户会员等级认证管理
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-17
 */
@TableName("sys_user_target")
public class UserTarget extends Model<UserTarget> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    private String uid;
    /**
     * 评级指标的JSONArray内容
     */
    private String target;
    /**
     * 会员等级1-6级(<8)1级(8-23)2级(24-31)3级(32-63)4级(64-95)5级(96-∞)6级
     */
    private String lv;
    /**
     * 是否审核(1-3级自动审核,4-6级线下审核)0:否,1:是
     */
    private String check;
    /**
     * 材料补充,格式:[{"name":"xxxx","data":["x.jpg","y.png"]}]
     */
    private String replenish;
    private Date createtime;
    private Date updatetime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getReplenish() {
        return replenish;
    }

    public void setReplenish(String replenish) {
        this.replenish = replenish;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserTarget{" +
        "id=" + id +
        ", uid=" + uid +
        ", target=" + target +
        ", lv=" + lv +
        ", check=" + check +
        ", replenish=" + replenish +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        "}";
    }
}
