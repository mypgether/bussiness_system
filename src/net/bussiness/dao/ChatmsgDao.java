package net.bussiness.dao;

import java.util.Date;

@SuppressWarnings("serial")
public class ChatmsgDao implements java.io.Serializable {

	// Fields

	private Integer msgId;
	private UserDao userBySenderId;
	private UserDao userByReceiverId;
	private Integer msgType;
	private byte[] msgContent;
	private Date msgTime;

	// Constructors

	/** default constructor */
	public ChatmsgDao() {
	}

	/** full constructor */
	public ChatmsgDao(UserDao userBySenderId, UserDao userByReceiverId, Integer msgType,
			byte[] msgContent, Date msgTime) {
		this.userBySenderId = userBySenderId;
		this.userByReceiverId = userByReceiverId;
		this.msgType = msgType;
		this.msgContent = msgContent;
		this.msgTime = msgTime;
	}
	public Integer getMsgId() {
		return this.msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public UserDao getUserBySenderId() {
		return this.userBySenderId;
	}

	public void setUserBySenderId(UserDao userBySenderId) {
		this.userBySenderId = userBySenderId;
	}

	public UserDao getUserByReceiverId() {
		return this.userByReceiverId;
	}

	public void setUserByReceiverId(UserDao userByReceiverId) {
		this.userByReceiverId = userByReceiverId;
	}

	public Integer getMsgType() {
		return this.msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public byte[] getMsgContent() {
		return this.msgContent;
	}

	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
	}

	public Date getMsgTime() {
		return this.msgTime;
	}

	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime;
	}
}