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
 *
资讯管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
@TableName("sys_information")
@Getter
@Setter
@ToString
public class Information extends Model<Information> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 栏目类型ID为资讯的ID
     */
    @TableField("column_id")
    private Integer columnId;
    /**
     * 标题
     */
    private String title;
    /**
     * 缩略图
     */
    private String thumb;
    /**
     * 图集
     */
    private String images;
    /**
     * 视频链接地址
     */
    private String url;
    /**
     * 描述
     */
    private String description;
    /**
     * 城市ID（0：表示全国）
     */
    @TableField("city_id")
    private Integer cityId;
    /**
     * 来源ID（0：官方，1：个人）
     */
    @TableField("source_id")
    private Integer sourceId;
    /**
     * 当是官方时为管理员的ID，是个人时，表示用户的ID
     */
    private Integer uid;
    /**
     * 发布IP
     */
    @TableField("publish_ip")
    private String publishIp;
    /**
     * 内容
     */
    private String content;
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

    @TableField("tag_id")
    private String tagId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
