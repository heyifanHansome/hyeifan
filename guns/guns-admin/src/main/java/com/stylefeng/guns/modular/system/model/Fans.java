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
 * 粉丝表
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-29
 */
@TableName("sys_fans")
@Getter
@Setter
@ToString
public class Fans extends Model<Fans> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 	用户
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 粉丝
     */
    private Integer follower;
    /**
     * 关注状态:是否取消关注等
     */
    private String status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更行时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 是否是徒弟(0.不是,1.是)
     */
    @TableField("is_disciple")
    private Integer isDisciple;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
