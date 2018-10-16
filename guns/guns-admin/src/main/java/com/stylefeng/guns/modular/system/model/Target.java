package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员等级评价指标体系
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-16
 */
@TableName("sys_target")
public class Target extends Model<Target> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 等级
     */
    private String grade;
    /**
     * 分值
     */
    private String score;
    /**
     * 父ID(顶级为0)
     */
    private String pid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Target{" +
        "id=" + id +
        ", name=" + name +
        ", grade=" + grade +
        ", score=" + score +
        ", pid=" + pid +
        "}";
    }
}
