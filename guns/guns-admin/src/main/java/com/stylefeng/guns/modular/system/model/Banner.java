package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 广告(后期跳转h5页面时,还要记录这个接口,或者这个广告被访问了多少次)
 * </p>
 *
 * @author 李俊
 * @since 2018-10-22
 */
@TableName("sys_banner")
@Getter
@Setter
@ToString
public class Banner extends Model<Banner> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 阿里云OSS图片URL
     */
    private String picture;
    /**
     * 阿里云OSS删除时用到的key
     */
    private String object_name;
    /**
     * 广告跳转的超链接
     */
    private String href;
    /**
     * 标签ID
     */
    @TableField("tag_id")
    private String tagId;

    private String item_id;
    private String type;
    private Timestamp submit_time;
    private String is_ok;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
