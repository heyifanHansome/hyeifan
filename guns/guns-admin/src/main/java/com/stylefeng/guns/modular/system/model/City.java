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
 * 城市管理表city
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_city")
@Getter
@Setter
@ToString
public class City extends Model<City> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 城市分类ID
     */
    @TableField("type_id")
    private Integer typeId;
    /**
     * 城市名
     */
    private String name;
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
    private String lngx;
    private String laty;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
