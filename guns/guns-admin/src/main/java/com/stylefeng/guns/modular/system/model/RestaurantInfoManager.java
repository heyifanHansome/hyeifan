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
 * 餐厅信息管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_restaurant_info_manager")
@Getter
@Setter
@ToString
public class RestaurantInfoManager extends Model<RestaurantInfoManager> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 餐厅名
     */
    private String restaurant;
    /**
     * 餐厅缩略图
     */
    private String thumb;
    /**
     * 餐厅图集(无序列表)
     */
    private String images;
    /**
     * 餐厅所在城市
     */
    @TableField("city_id")
    private String cityId;
    /**
     * 餐厅详细地址
     */
    private String address;
    /**
     * 餐厅经度
     */
    private String longitude;
    /**
     * 餐厅纬度
     */
    private String latitude;
    /**
     * 营业时间
     */
    @TableField("business_hours")
    private String businessHours;
    /**
     * 加入状态（0：未审查，1：考察，2：通过，3：拒绝）
     */
    private String status;
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

    private String object_name;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
