package net.bussiness.dto;

import java.io.Serializable;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name = "im_chatuser")
public class ChatUserDto implements Serializable {
	@Id
	@Column(name = "_id")
	private int id;
	@Column(name = "senderId")
	private int senderId;
	@Column(name = "senderName")
	private String senderName;
	@Column(name = "lastChatTime")
	private String lastChatTime;
	@Column(name = "lastChatContent")
	private String lastChatContent;
	@Column(name = "senderLogoPath")
	private String senderLogoPath;

	public ChatUserDto() {
		super();
	}

	public ChatUserDto(int id, int senderId, String senderName,
			String lastChatTime, String lastChatContent, String senderLogoPath) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.senderName = senderName;
		this.lastChatTime = lastChatTime;
		this.lastChatContent = lastChatContent;
		this.senderLogoPath = senderLogoPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getLastChatContent() {
		return lastChatContent;
	}

	public void setLastChatContent(String lastChatContent) {
		this.lastChatContent = lastChatContent;
	}

	public String getLastChatTime() {
		return lastChatTime;
	}

	public void setLastChatTime(String lastChatTime) {
		this.lastChatTime = lastChatTime;
	}

	public String getSenderLogoPath() {
		return senderLogoPath;
	}

	public void setSenderLogoPath(String senderLogoPath) {
		this.senderLogoPath = senderLogoPath;
	}

	@Override
	public String toString() {
		return "ChatUserDto [id=" + id + ", senderId=" + senderId
				+ ", senderName=" + senderName + ", lastChatTime="
				+ lastChatTime + ", lastChatContent=" + lastChatContent
				+ ", senderLogoPath=" + senderLogoPath + "]";
	}
}
