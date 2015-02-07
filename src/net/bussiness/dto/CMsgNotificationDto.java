package net.bussiness.dto;

import java.io.Serializable;

public class CMsgNotificationDto implements Serializable {
	private boolean isImPersonOpen;
	private boolean isImGroupOpen;
	private boolean isYwsqOpen;
	private boolean isYwspOpen;
	private boolean isYwpjOpen;
	private boolean isYwnrOpen;

	public CMsgNotificationDto() {
		super();
		this.isImPersonOpen = true;
		this.isImGroupOpen = true;
		this.isYwsqOpen = true;
		this.isYwspOpen = true;
		this.isYwpjOpen = true;
		this.isYwnrOpen = true;
	}

	public CMsgNotificationDto(boolean isImPersonOpen, boolean isImGroupOpen,
			boolean isYwsqOpen, boolean isYwspOpen, boolean isYwpjOpen,
			boolean isYwnrOpen) {
		super();
		this.isImPersonOpen = isImPersonOpen;
		this.isImGroupOpen = isImGroupOpen;
		this.isYwsqOpen = isYwsqOpen;
		this.isYwspOpen = isYwspOpen;
		this.isYwpjOpen = isYwpjOpen;
		this.isYwnrOpen = isYwnrOpen;
	}

	public boolean isImPersonOpen() {
		return isImPersonOpen;
	}

	public void setImPersonOpen(boolean isImPersonOpen) {
		this.isImPersonOpen = isImPersonOpen;
	}

	public boolean isImGroupOpen() {
		return isImGroupOpen;
	}

	public void setImGroupOpen(boolean isImGroupOpen) {
		this.isImGroupOpen = isImGroupOpen;
	}

	public boolean isYwsqOpen() {
		return isYwsqOpen;
	}

	public void setYwsqOpen(boolean isYwsqOpen) {
		this.isYwsqOpen = isYwsqOpen;
	}

	public boolean isYwspOpen() {
		return isYwspOpen;
	}

	public void setYwspOpen(boolean isYwspOpen) {
		this.isYwspOpen = isYwspOpen;
	}

	public boolean isYwpjOpen() {
		return isYwpjOpen;
	}

	public void setYwpjOpen(boolean isYwpjOpen) {
		this.isYwpjOpen = isYwpjOpen;
	}

	public boolean isYwnrOpen() {
		return isYwnrOpen;
	}

	public void setYwnrOpen(boolean isYwnrOpen) {
		this.isYwnrOpen = isYwnrOpen;
	}

	@Override
	public String toString() {
		return "CMsgNotificationDto [isImPersonOpen=" + isImPersonOpen
				+ ", isImGroupOpen=" + isImGroupOpen + ", isYwsqOpen="
				+ isYwsqOpen + ", isYwspOpen=" + isYwspOpen + ", isYwpjOpen="
				+ isYwpjOpen + ", isYwnrOpen=" + isYwnrOpen + "]";
	}
}
