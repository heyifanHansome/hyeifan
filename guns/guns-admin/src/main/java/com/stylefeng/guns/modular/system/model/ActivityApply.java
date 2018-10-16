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
 * 活动报名管理
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-16
 */
@Getter
@Setter
@ToString
@TableName("sys_activity_apply")
public class ActivityApply extends Model<ActivityApply> {

    private static final long serialVersionUID = 1L;

    /**
     * 报名表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 被报名的活动ID
     */
    @TableField("activity_id")
    private String activityId;
    /**
     * 报名用户ID,逗号分隔
     */
    @TableField("user_api_id")
    private String userApiId;
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
