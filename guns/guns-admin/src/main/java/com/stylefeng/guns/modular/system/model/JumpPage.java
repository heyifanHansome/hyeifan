package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 首页跳转版块
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-23
 */
@TableName("sys_jump_page")
@Getter
@Setter
@ToString
public class JumpPage extends Model<JumpPage> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 背景图
     */
    private String picture;
    /**
     * 阿里云OSS删除的key
     */
    private String object_name;
    /**
     * 排序值
     */
    private String orders;
    /**
     * 跳转页面代号(详情见接口文档)
     */
    private String code;
    private String enable;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
