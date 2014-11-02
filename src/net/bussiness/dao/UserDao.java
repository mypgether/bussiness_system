package net.bussiness.dao;

import java.util.Arrays;
import java.util.Date;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
public class UserDao implements java.io.Serializable {

	// Fields
	private Integer id;
	private PositionDao position;
	private DeptDao dept;
	private Integer userId;
	private String userName;
	private String password;
	private byte[] photo;
	private String tel;
	private String email;
	private Date joinTime;
	private String description;

	// Constructors

	/** default constructor */
	public UserDao() {
	}

	/** full constructor */
	public UserDao(PositionDao position, DeptDao dept, Integer userId,
			String userName, String password, byte[] photo, String tel,
			String email, Date joinTime, String description) {
		this.position = position;
		this.dept = dept;
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.photo = photo;
		this.tel = tel;
		this.email = email;
		this.joinTime = joinTime;
		this.description = description;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PositionDao getPosition() {
		return this.position;
	}

	public void setPosition(PositionDao position) {
		this.position = position;
	}

	public DeptDao getDept() {
		return this.dept;
	}

	public void setDept(DeptDao dept) {
		this.dept = dept;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getJoinTime() {
		return this.joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UserDao [id=" + id + ", userId=" + userId + ", userName="
				+ userName + ", password=" + password + ", photo="
				+ Arrays.toString(photo) + ", tel=" + tel + ", email=" + email
				+ ", joinTime=" + joinTime + ", description=" + description
				+ "]";
	}

}