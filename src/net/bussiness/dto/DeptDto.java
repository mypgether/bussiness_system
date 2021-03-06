package net.bussiness.dto;

import java.util.Date;

/**
 * Dept entity. @author MyEclipse Persistence Tools
 */
public class DeptDto implements java.io.Serializable {

	// Fields

	private Integer id;
	private String deptName;
	private Integer createrId;
	private Date createTime;
	private String description;

	// Constructors

	/** default constructor */
	public DeptDto() {
	}

	/** full constructor */
	public DeptDto(String deptName, Integer createrId, Date createTime,
			String description) {
		this.deptName = deptName;
		this.createrId = createrId;
		this.createTime = createTime;
		this.description = description;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getCreaterId() {
		return createrId;
	}

	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "DeptDto [id=" + id + ", deptName=" + deptName + ", createTime="
				+ createTime + ", description=" + description + "]";
	}

}