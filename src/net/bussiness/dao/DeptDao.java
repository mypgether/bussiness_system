package net.bussiness.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class DeptDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private String deptName;
	private Integer createrId;
	private Date createTime;
	private String description;
	private Set<PositionDao> positions = new HashSet<PositionDao>(0);
	private Set<UserDao> users = new HashSet<UserDao>(0);

	// Constructors

	/** default constructor */
	public DeptDao() {
	}

	/** full constructor */
	public DeptDao(String deptName, Integer createrId, Date createTime,
			String description, Set<PositionDao> positions, Set<UserDao> users) {
		this.deptName = deptName;
		this.createrId = createrId;
		this.createTime = createTime;
		this.description = description;
		this.positions = positions;
		this.users = users;
	}

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
		return this.createrId;
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

	public Set<PositionDao> getPositions() {
		return this.positions;
	}

	public void setPositions(Set<PositionDao> positions) {
		this.positions = positions;
	}

	public Set<UserDao> getUsers() {
		return this.users;
	}

	public void setUsers(Set<UserDao> users) {
		this.users = users;
	}
}