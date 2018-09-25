package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * 用户简历管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */

@Getter
@Setter
@ToString
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
    private Integer applyStatus;
    /**
     * 性别（0：未知，1：男，2：女）
     */
    private Integer sex;
    /**
     * 会员生日
     */
    private String birthday;
    /**
     * 开始工作日期（工龄）
     */
    @TableField("work_time")
    private String workTime;
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
