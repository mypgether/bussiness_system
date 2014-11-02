package net.bussiness.dao;

import java.util.Date;

/**
 * Ywsq entity. @author MyEclipse Persistence Tools
 */
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

	// Constructors

	/** default constructor */
	public YwsqDao() {
	}

	/** full constructor */
	public YwsqDao(UserDao userByProposerId, UserDao userByApproverId,
			String location, Date timestamp, String reason, Date applyTime,
			Integer approveState, Date approveTime, String approveReason) {
		this.userByProposerId = userByProposerId;
		this.userByApproverId = userByApproverId;
		this.location = location;
		this.timestamp = timestamp;
		this.reason = reason;
		this.applyTime = applyTime;
		this.approveState = approveState;
		this.approveTime = approveTime;
		this.approveReason = approveReason;
	}

	// Property accessors
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

	@Override
	public String toString() {
		return "YwsqDao [ywId=" + ywId + ", location=" + location
				+ ", timestamp=" + timestamp + ", reason=" + reason
				+ ", applyTime=" + applyTime + ", approveState=" + approveState
				+ ", approveTime=" + approveTime + ", approveReason="
				+ approveReason + "]";
	}

}