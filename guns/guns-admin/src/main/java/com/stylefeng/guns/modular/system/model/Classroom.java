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
 * 星厨课堂
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_classroom")
@Getter
@Setter
@ToString
public class Classroom extends Model<Classroom> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 栏目类型ID为课堂的ID
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
     * 视频链接地址（视频类课堂会存在）
     */
    private String url;
    /**
     * 描述（本期看点）
     */
    private String description;
    /**
     * 开播时间
     */
    @TableField("start_time")
    private Date startTime;
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
     * 内容（文章类课堂会存在）
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
    /**
     * 视频集合
     */
    private String video;

    /**
     * 图片集合
     */
    private String images;

    @TableField("tag_id")
    private String tagId;
    /**
     * 封面图片
     */
    private String coverphoto;
    /**
     * 在学人数
     */
    @TableField("number_learning")
    private String numberLearning;

    /**
     * 海报生成标题
     */
    @TableField("posters_title")
    private String postersTitle;

    @TableField("user_description")
    private String userDescription;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
