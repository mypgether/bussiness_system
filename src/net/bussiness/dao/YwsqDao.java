package net.bussiness.dao;

import java.util.Date;
<<<<<<< HEAD

/**
 * Ywsq entity. @author MyEclipse Persistence Tools
 */
=======
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
public class YwsqDao implements java.io.Serializable {

	// Fields

	private Integer ywId;
	private UserDao userByProposerId;
	private UserDao userByApproverId;
	private String location;
	private Date timestamp;
	private String reason;
	private Date applyTime;
	private Integer approveState;
	private Date approveTime;
	private String approveReason;
<<<<<<< HEAD
=======
	private Set<YwnrDao> ywnrs = new HashSet<YwnrDao>(0);
	private Set<YwpjDao> ywpjs = new HashSet<YwpjDao>(0);
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f

	// Constructors

	/** default constructor */
	public YwsqDao() {
	}

	/** full constructor */
	public YwsqDao(UserDao userByProposerId, UserDao userByApproverId,
			String location, Date timestamp, String reason, Date applyTime,
<<<<<<< HEAD
			Integer approveState, Date approveTime, String approveReason) {
=======
			Integer approveState, Date approveTime, String approveReason,
			Set<YwnrDao> ywnrs, Set<YwpjDao> ywpjs) {
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
		this.userByProposerId = userByProposerId;
		this.userByApproverId = userByApproverId;
		this.location = location;
		this.timestamp = timestamp;
		this.reason = reason;
		this.applyTime = applyTime;
		this.approveState = approveState;
		this.approveTime = approveTime;
		this.approveReason = approveReason;
<<<<<<< HEAD
	}

	// Property accessors
=======
		this.ywnrs = ywnrs;
		this.ywpjs = ywpjs;
	}

>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	public Integer getYwId() {
		return this.ywId;
	}

	public void setYwId(Integer ywId) {
		this.ywId = ywId;
	}

	public UserDao getUserByProposerId() {
		return this.userByProposerId;
	}

	public void setUserByProposerId(UserDao userByProposerId) {
		this.userByProposerId = userByProposerId;
	}

	public UserDao getUserByApproverId() {
		return this.userByApproverId;
	}

	public void setUserByApproverId(UserDao userByApproverId) {
		this.userByApproverId = userByApproverId;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Integer getApproveState() {
		return this.approveState;
	}

	public void setApproveState(Integer approveState) {
		this.approveState = approveState;
	}

	public Date getApproveTime() {
		return this.approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public String getApproveReason() {
		return this.approveReason;
	}

	public void setApproveReason(String approveReason) {
		this.approveReason = approveReason;
	}

<<<<<<< HEAD
	@Override
	public String toString() {
		return "YwsqDao [ywId=" + ywId + ", location=" + location
				+ ", timestamp=" + timestamp + ", reason=" + reason
				+ ", applyTime=" + applyTime + ", approveState=" + approveState
				+ ", approveTime=" + approveTime + ", approveReason="
				+ approveReason + "]";
	}

=======
	public Set<YwnrDao> getYwnrs() {
		return this.ywnrs;
	}

	public void setYwnrs(Set<YwnrDao> ywnrs) {
		this.ywnrs = ywnrs;
	}

	public Set<YwpjDao> getYwpjs() {
		return this.ywpjs;
	}

	public void setYwpjs(Set<YwpjDao> ywpjs) {
		this.ywpjs = ywpjs;
	}
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
}