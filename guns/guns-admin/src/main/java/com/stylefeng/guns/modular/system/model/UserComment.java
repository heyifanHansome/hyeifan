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
 * 用户评论表
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-12
 */
@Getter
@Setter
@ToString
@TableName("sys_user_comment")
public class UserComment extends Model<UserComment> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键（评论ID）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 栏目类型ID（当为作品类型时，works_id表示作品ID, 当为资讯类型时，works_id表示资讯ID）
     */
    @TableField("column_id")
    private String columnId;
    @TableField("column_name")
    private String columnName;
    /**
     * 作品（资讯、课堂、活动）ID
     */
    @TableField("item_id")
    private String itemId;
    @TableField("item_title")
    private String itemTitle;
    /**
     * 评论内容
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
     * 该条记录所回复的评论ID,没有则填0
     */
    @TableField("comment_id")
    private String commentId;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
