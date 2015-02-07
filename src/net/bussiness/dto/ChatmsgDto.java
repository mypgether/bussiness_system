package net.bussiness.dto;

import java.util.Arrays;
import java.util.Date;

/**
 * Chatmsg entity. @author MyEclipse Persistence Tools
 */
public class ChatmsgDto implements java.io.Serializable {

	// Fields

	private Integer msgId;
	private UserDto userBySenderId;
	private UserDto userByReceiverId;
	/*
	 * 1表示文字消息，2表示图片消息，3表示语音消息
	 */
	private Integer msgType;
	private byte[] msgContent;
	private Date msgTime;

	// Constructors

	/** default constructor */
	public ChatmsgDto() {
	}

	/** full constructor */
	public ChatmsgDto(UserDto userBySenderId, UserDto userByReceiverId,
			Integer msgType, byte[] msgContent, Date msgTime) {
		this.userBySenderId = userBySenderId;
		this.userByReceiverId = userByReceiverId;
		this.msgType = msgType;
		this.msgContent = msgContent;
		this.msgTime = msgTime;
	}

	// Property accessors
	public Integer getMsgId() {
		return this.msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public UserDto getUserBySenderId() {
		return this.userBySenderId;
	}

	public void setUserBySenderId(UserDto userBySenderId) {
		this.userBySenderId = userBySenderId;
	}

	public UserDto getUserByReceiverId() {
		return this.userByReceiverId;
	}

	public void setUserByReceiverId(UserDto userByReceiverId) {
		this.userByReceiverId = userByReceiverId;
	}

	/**
	 * <pre>
	 * @author Myp
	 * Create Time: 2015-1-16 上午11:42:57
	 * @return 1表示文字消息，2表示图片消息，3表示语音消息
	 * </pre>
	 */
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

	@Override
	public String toString() {
		return "ChatmsgDto [msgId=" + msgId + ", msgType=" + msgType
				+ ", msgContent=" + Arrays.toString(msgContent) + ", msgTime="
				+ msgTime + "]";
	}

}