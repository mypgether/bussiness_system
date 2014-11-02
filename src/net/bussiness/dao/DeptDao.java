package net.bussiness.dao;

import java.util.Date;
<<<<<<< HEAD

/**
 * Dept entity. @author MyEclipse Persistence Tools
 */
=======
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
public class DeptDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private String deptName;
	private Integer createrId;
	private Date createTime;
	private String description;
<<<<<<< HEAD
=======
	private Set<PositionDao> positions = new HashSet<PositionDao>(0);
	private Set<UserDao> users = new HashSet<UserDao>(0);
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f

	// Constructors

	/** default constructor */
	public DeptDao() {
	}

	/** full constructor */
	public DeptDao(String deptName, Integer createrId, Date createTime,
<<<<<<< HEAD
			String description) {
=======
			String description, Set<PositionDao> positions, Set<UserDao> users) {
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
		this.deptName = deptName;
		this.createrId = createrId;
		this.createTime = createTime;
		this.description = description;
<<<<<<< HEAD
	}

	// Property accessors
=======
		this.positions = positions;
		this.users = users;
	}

>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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

<<<<<<< HEAD
	@Override
	public String toString() {
		return "DeptDao [id=" + id + ", deptName=" + deptName + ", createrId="
				+ createrId + ", createTime=" + createTime + ", description="
				+ description + "]";
	}

=======
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
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
}