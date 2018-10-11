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
 * 栏目分类表column_type（如：作品、资讯（匠厨专访、活动报道、热门话题）、活动（交流会、比赛、拜师会）、课堂、招聘这些分类）
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
@Getter
@Setter
@ToString
@TableName("sys_column_type")
public class ColumnType extends Model<ColumnType> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 父级栏目ID(0表示一级栏目)
     */
    @TableField("menu_id")
    private String menuId;
    /**
     * 排序
     */
    private String orders;
    /**
     * 栏目类型名
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



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
