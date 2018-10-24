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
 * 首页顶部厨师分组推荐
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-24
 */
@Getter
@Setter
@ToString
@TableName("sys_index_up_group_user")
public class IndexUpGroupUser extends Model<IndexUpGroupUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 城市ID(0:全国)
     */
    @TableField("city_id")
    private String cityId;
    /**
     * 用户ID(用逗号隔开,顺序很重要)
     */
    @TableField("user_api_id")
    private String userApiId;
    /**
     * 提交时间
     */
    @TableField("submit_time")
    private Date submitTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
