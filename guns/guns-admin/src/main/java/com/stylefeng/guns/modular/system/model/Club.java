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
 * 星厨俱乐部
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_club")
@Getter
@Setter
@ToString
public class Club extends Model<Club> {

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
     * 等级ID
     */
    @TableField("grade_id")
    private Integer gradeId;
    /**
     * 实名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;
    /**
     * 加入状态（0：未审查，1：考察，2：通过，3：拒绝）
     */
    private Integer status;
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
