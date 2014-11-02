package net.bussiness.dao;

<<<<<<< HEAD
import java.util.Arrays;
import java.util.Date;

/**
 * Chatmsg entity. @author MyEclipse Persistence Tools
 */
=======
import java.util.Date;

@SuppressWarnings("serial")
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD
	public ChatmsgDao(UserDao userBySenderId, UserDao userByReceiverId,
			Integer msgType, byte[] msgContent, Date msgTime) {
=======
	public ChatmsgDao(UserDao userBySenderId, UserDao userByReceiverId, Integer msgType,
			byte[] msgContent, Date msgTime) {
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
		this.userBySenderId = userBySenderId;
		this.userByReceiverId = userByReceiverId;
		this.msgType = msgType;
		this.msgContent = msgContent;
		this.msgTime = msgTime;
	}
<<<<<<< HEAD

	// Property accessors
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD

	@Override
	public String toString() {
		return "ChatmsgDao [msgId=" + msgId + ", msgType=" + msgType
				+ ", msgContent=" + Arrays.toString(msgContent) + ", msgTime="
				+ msgTime + "]";
	}

=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
}