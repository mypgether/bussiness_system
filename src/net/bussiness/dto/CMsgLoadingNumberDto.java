package net.bussiness.dto;

import java.io.Serializable;

public class CMsgLoadingNumberDto implements Serializable {
	// 1 3 5 10 20 30 40 50
	private int ywspNumber;
	// 1 3 5 10 20 30 40 50
	private int ywnrNumber;
	// 5 10 15 20 25 30 40 50
	private int imChatMsgNumber;

	public CMsgLoadingNumberDto() {
		super();
		this.ywspNumber = 3;
		this.ywnrNumber = 3;
		this.imChatMsgNumber = 10;
	}

	public CMsgLoadingNumberDto(int ywspNumber, int ywnrNumber,
			int imChatMsgNumber) {
		super();
		this.ywspNumber = ywspNumber;
		this.ywnrNumber = ywnrNumber;
		this.imChatMsgNumber = imChatMsgNumber;
	}

	public int getYwspNumber() {
		return ywspNumber;
	}

	public void setYwspNumber(int ywspNumber) {
		this.ywspNumber = ywspNumber;
	}

	public int getYwnrNumber() {
		return ywnrNumber;
	}

	public void setYwnrNumber(int ywnrNumber) {
		this.ywnrNumber = ywnrNumber;
	}

	public int getImChatMsgNumber() {
		return imChatMsgNumber;
	}

	public void setImChatMsgNumber(int imChatMsgNumber) {
		this.imChatMsgNumber = imChatMsgNumber;
	}

	@Override
	public String toString() {
		return "CMsgLoadingNumberDto [ywspNumber=" + ywspNumber
				+ ", ywnrNumber=" + ywnrNumber + ", imChatMsgNumber="
				+ imChatMsgNumber + "]";
	}
}
