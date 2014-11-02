package net.bussiness.dao;

<<<<<<< HEAD
import java.util.Arrays;
import java.util.Date;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
public class UserDao implements java.io.Serializable {

	// Fields
	private Integer id;
	private PositionDao position;
=======
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class UserDao implements java.io.Serializable {

	// Fields

	private Integer id;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	private DeptDao dept;
	private Integer userId;
	private String userName;
	private String password;
	private byte[] photo;
	private String tel;
	private String email;
<<<<<<< HEAD
	private Date joinTime;
	private String description;
=======
	private Integer position;
	private Date joinTime;
	private String description;
	private Set<YwpjDao> ywpjs = new HashSet<YwpjDao>(0);
	private Set<YwsqDao> ywsqsForProposerId = new HashSet<YwsqDao>(0);
	private Set<YwsqDao> ywsqsForApproverId = new HashSet<YwsqDao>(0);
	private Set<ChatmsgDao> chatmsgsForSenderId = new HashSet<ChatmsgDao>(0);
	private Set<ChatmsgDao> chatmsgsForReceiverId = new HashSet<ChatmsgDao>(0);
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f

	// Constructors

	/** default constructor */
	public UserDao() {
	}

	/** full constructor */
<<<<<<< HEAD
	public UserDao(PositionDao position, DeptDao dept, Integer userId,
			String userName, String password, byte[] photo, String tel,
			String email, Date joinTime, String description) {
		this.position = position;
=======
	public UserDao(DeptDao dept, Integer userId, String userName,
			String password, byte[] photo, String tel, String email,
			Integer position, Date joinTime, String description,
			Set<YwpjDao> ywpjs, Set<YwsqDao> ywsqsForProposerId,
			Set<YwsqDao> ywsqsForApproverId,
			Set<ChatmsgDao> chatmsgsForSenderId,
			Set<ChatmsgDao> chatmsgsForReceiverId) {
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
		this.dept = dept;
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.photo = photo;
		this.tel = tel;
		this.email = email;
<<<<<<< HEAD
		this.joinTime = joinTime;
		this.description = description;
	}

	// Property accessors
=======
		this.position = position;
		this.joinTime = joinTime;
		this.description = description;
		this.ywpjs = ywpjs;
		this.ywsqsForProposerId = ywsqsForProposerId;
		this.ywsqsForApproverId = ywsqsForApproverId;
		this.chatmsgsForSenderId = chatmsgsForSenderId;
		this.chatmsgsForReceiverId = chatmsgsForReceiverId;
	}

>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

<<<<<<< HEAD
	public PositionDao getPosition() {
		return this.position;
	}

	public void setPosition(PositionDao position) {
		this.position = position;
	}

=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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

<<<<<<< HEAD
=======
	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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

<<<<<<< HEAD
	@Override
	public String toString() {
		return "UserDao [id=" + id + ", userId=" + userId + ", userName="
				+ userName + ", password=" + password + ", photo="
				+ Arrays.toString(photo) + ", tel=" + tel + ", email=" + email
				+ ", joinTime=" + joinTime + ", description=" + description
				+ "]";
	}

=======
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
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
}