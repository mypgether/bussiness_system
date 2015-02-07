package net.bussiness.dto;

import java.util.Arrays;
import java.util.Date;

/**
 * Groupmsg entity. @author MyEclipse Persistence Tools
 */
public class GroupmsgDto implements java.io.Serializable {

	// Fields

	private Integer msgId;
	private DeptDto group;
	private UserDto userBySenderId;
	private Integer msgType;
	private byte[] msgContent;
	private Date msgTime;

	// Constructors

	/** default constructor */
	public GroupmsgDto() {
	}

	/** full constructor */
	public GroupmsgDto(DeptDto group, UserDto userBySenderId, Integer msgType,
			byte[] msgContent, Date msgTime) {
		this.group = group;
		this.userBySenderId = userBySenderId;
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

	public DeptDto getGroup() {
		return group;
	}

	public void setGroup(DeptDto group) {
		this.group = group;
	}

	public UserDto getUserBySenderId() {
		return this.userBySenderId;
	}

	public void setUserBySenderId(UserDto userBySenderId) {
		this.userBySenderId = userBySenderId;
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

	@Override
	public String toString() {
		return "GroupmsgDto [msgId=" + msgId + ", msgType=" + msgType
				+ ", msgContent=" + Arrays.toString(msgContent) + ", msgTime="
				+ msgTime + "]";
	}

}