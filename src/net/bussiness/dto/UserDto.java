package net.bussiness.dto;

import java.util.Date;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
public class UserDto implements java.io.Serializable {

	// Fields
	private Integer id;
	private PositionDto position;
	private DeptDto dept;
	private Integer userId;
	private String pushUserId;
	private String pushChannelId;
	private String userName;
	private String password;
	private String photoPath;
	private String tel;
	private String email;
	private Date joinTime;
	private String description;

	// Constructors

	/** default constructor */
	public UserDto() {
	}

	/** full constructor */
	public UserDto(PositionDto position, DeptDto dept, Integer userId,
			String pushUserId, String pushChannelId, String userName,
			String password, String photoPath, String tel, String email,
			Date joinTime, String description) {
		this.position = position;
		this.dept = dept;
		this.userId = userId;
		this.pushUserId = pushUserId;
		this.pushChannelId = pushChannelId;
		this.userName = userName;
		this.password = password;
		this.photoPath = photoPath;
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

	public PositionDto getPosition() {
		return this.position;
	}

	public void setPosition(PositionDto position) {
		this.position = position;
	}

	public DeptDto getDept() {
		return this.dept;
	}

	public void setDept(DeptDto dept) {
		this.dept = dept;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPushUserId() {
		return pushUserId;
	}

	public void setPushUserId(String pushUserId) {
		this.pushUserId = pushUserId;
	}

	public String getPushChannelId() {
		return pushChannelId;
	}

	public void setPushChannelId(String pushChannelId) {
		this.pushChannelId = pushChannelId;
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

	public String getPhotoPath() {
		return this.photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
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
		return "UserDto [id=" + id + ", userId=" + userId + ", pushUserId="
				+ pushUserId + ", pushChannelId=" + pushChannelId
				+ ", userName=" + userName + ", password=" + password
				+ ", photoPath=" + photoPath + ", tel=" + tel + ", email="
				+ email + ", joinTime=" + joinTime + ", description="
				+ description + "]";
	}
}