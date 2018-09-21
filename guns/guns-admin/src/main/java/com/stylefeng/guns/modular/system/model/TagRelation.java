package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 标签关联表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_tag_relation")
public class TagRelation extends Model<TagRelation> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 栏目类型ID（当为0时表示通用标签）
     */
    @TableField("column_id")
    private Integer columnId;
    /**
     * 公共标签ID（单选）
     */
    @TableField("common_type_id")
    private String commonTypeId;
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
     * 关联ID（可能一样，来源作品表、课堂表、活动表、资讯表等）
     */
    @TableField("relation_id")
    private Integer relationId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public String getCommonTypeId() {
        return commonTypeId;
    }

    public void setCommonTypeId(String commonTypeId) {
        this.commonTypeId = commonTypeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TagRelation{" +
        "id=" + id +
        ", columnId=" + columnId +
        ", commonTypeId=" + commonTypeId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", relation Id=" + relationId +
        "}";
    }
}
