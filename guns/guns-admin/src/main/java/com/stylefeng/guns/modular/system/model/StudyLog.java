package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 学习记录表
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-26
 */
@TableName("sys_study_log")
public class StudyLog extends Model<StudyLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 学习记录表自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 栏目id
     */
    @TableField("column_id")
    private Integer columnId;
    /**
     * 作品id
     */
    @TableField("works_id")
    private Integer worksId;
    /**
     * 手机号
     */
    private Integer phone;


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

    public Integer getWorksId() {
        return worksId;
    }

    public void setWorksId(Integer worksId) {
        this.worksId = worksId;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "StudyLog{" +
        "id=" + id +
        ", columnId=" + columnId +
        ", worksId=" + worksId +
        ", phone=" + phone +
        "}";
    }
}
