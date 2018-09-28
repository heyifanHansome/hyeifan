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
 * 图片表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-26
 */
@Getter
@Setter
@ToString
@TableName("sys_picture")
public class Picture extends Model<Picture> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */

    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;
    /**
     *  创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 外键id
     */
    @TableField("base_id")
    private String baseId;
    /**
     * 图片名称
     */
    private String picturename;
    /**
     * 相对路径
     */
    private String relativepath;
    /**
     * 服务器路径
     */
    private String serverpath;
    /**
     * 绝对路径
     */
    private String absolutepath;
    /**
     * 类型
     */
    private String type;
    /**
     * 后缀
     */
    private String suffixname;


    @TableField("oss_object_name")
    private String ossObjectName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
