package net.bussiness.dto;

import java.io.Serializable;

public class CNoBotherModeDto implements Serializable {
	private boolean isOpen;
	private String startTime;
	private String endTime;

	public CNoBotherModeDto() {
		super();
		this.isOpen = false;
		this.startTime = "9:00";
		this.endTime = "17:00";
	}

	public CNoBotherModeDto(boolean isOpen, String startTime, String endTime) {
		super();
		this.isOpen = isOpen;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "CNoBotherModeDto [isOpen=" + isOpen + ", startTime="
				+ startTime + ", endTime=" + endTime + "]";
	}
}
