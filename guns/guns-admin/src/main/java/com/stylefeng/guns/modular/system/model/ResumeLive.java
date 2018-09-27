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
 * 用户经历管理表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@TableName("sys_resume_live")
@Getter
@Setter
@ToString
public class ResumeLive extends Model<ResumeLive> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 简历ID
     */
    @TableField("resume_id")
    private Integer resumeId;
    /**
     * 公司部门
     */
    private String company;
    /**
     * 职位
     */
    @TableField("position_name")
    private String positionName;
    /**
     * 在职起始时间
     */
    @TableField("start_time")
    private String startTime;
    /**
     * 在职结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 备注
     */
    private String remark;
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
