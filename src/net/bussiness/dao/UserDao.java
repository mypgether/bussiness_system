package net.bussiness.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class UserDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private DeptDao dept;
	private Integer userId;
	private String userName;
	private String password;
	private byte[] photo;
	private String tel;
	private String email;
	private Integer position;
	private Date joinTime;
	private String description;
	private Set<YwpjDao> ywpjs = new HashSet<YwpjDao>(0);
	private Set<YwsqDao> ywsqsForProposerId = new HashSet<YwsqDao>(0);
	private Set<YwsqDao> ywsqsForApproverId = new HashSet<YwsqDao>(0);
	private Set<ChatmsgDao> chatmsgsForSenderId = new HashSet<ChatmsgDao>(0);
	private Set<ChatmsgDao> chatmsgsForReceiverId = new HashSet<ChatmsgDao>(0);

	// Constructors

	/** default constructor */
	public UserDao() {
	}

	/** full constructor */
	public UserDao(DeptDao dept, Integer userId, String userName,
			String password, byte[] photo, String tel, String email,
			Integer position, Date joinTime, String description,
			Set<YwpjDao> ywpjs, Set<YwsqDao> ywsqsForProposerId,
			Set<YwsqDao> ywsqsForApproverId,
			Set<ChatmsgDao> chatmsgsForSenderId,
			Set<ChatmsgDao> chatmsgsForReceiverId) {
		this.dept = dept;
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.photo = photo;
		this.tel = tel;
		this.email = email;
		this.position = position;
		this.joinTime = joinTime;
		this.description = description;
		this.ywpjs = ywpjs;
		this.ywsqsForProposerId = ywsqsForProposerId;
		this.ywsqsForApproverId = ywsqsForApproverId;
		this.chatmsgsForSenderId = chatmsgsForSenderId;
		this.chatmsgsForReceiverId = chatmsgsForReceiverId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
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

	public Set<YwpjDao> getYwpjs() {
		return this.ywpjs;
	}

	public void setYwpjs(Set<YwpjDao> ywpjs) {
		this.ywpjs = ywpjs;
	}

	public Set<YwsqDao> getYwsqsForProposerId() {
		return this.ywsqsForProposerId;
	}

	public void setYwsqsForProposerId(Set<YwsqDao> ywsqsForProposerId) {
		this.ywsqsForProposerId = ywsqsForProposerId;
	}

	public Set<YwsqDao> getYwsqsForApproverId() {
		return this.ywsqsForApproverId;
	}

	public void setYwsqsForApproverId(Set<YwsqDao> ywsqsForApproverId) {
		this.ywsqsForApproverId = ywsqsForApproverId;
	}

	public Set<ChatmsgDao> getChatmsgsForSenderId() {
		return this.chatmsgsForSenderId;
	}

	public void setChatmsgsForSenderId(Set<ChatmsgDao> chatmsgsForSenderId) {
		this.chatmsgsForSenderId = chatmsgsForSenderId;
	}

	public Set<ChatmsgDao> getChatmsgsForReceiverId() {
		return this.chatmsgsForReceiverId;
	}

	public void setChatmsgsForReceiverId(Set<ChatmsgDao> chatmsgsForReceiverId) {
		this.chatmsgsForReceiverId = chatmsgsForReceiverId;
	}
}