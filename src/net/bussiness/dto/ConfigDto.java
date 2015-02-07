package net.bussiness.dto;

import java.io.Serializable;

public class ConfigDto implements Serializable {
	private CMsgNotificationDto mCMsgNotificationDto;
	private CMsgLoadingNumberDto mCMsgLoadingNumberDto;
	private CNoBotherModeDto mCNoBotherModeDto;
	private boolean isBlockGroupMsg;

	public ConfigDto() {
		super();
		this.mCMsgNotificationDto = new CMsgNotificationDto();
		this.mCMsgLoadingNumberDto = new CMsgLoadingNumberDto();
		this.mCNoBotherModeDto = new CNoBotherModeDto();
		this.isBlockGroupMsg = false;
	}

	public ConfigDto(CMsgNotificationDto mCMsgNotificationDto,
			CMsgLoadingNumberDto mCMsgLoadingNumberDto,
			CNoBotherModeDto mCNoBotherModeDto, boolean isBlockGroupMsg) {
		super();
		this.mCMsgNotificationDto = mCMsgNotificationDto;
		this.mCMsgLoadingNumberDto = mCMsgLoadingNumberDto;
		this.mCNoBotherModeDto = mCNoBotherModeDto;
		this.isBlockGroupMsg = isBlockGroupMsg;
	}

	public CMsgNotificationDto getmCMsgNotificationDto() {
		return mCMsgNotificationDto;
	}

	public void setmCMsgNotificationDto(CMsgNotificationDto mCMsgNotificationDto) {
		this.mCMsgNotificationDto = mCMsgNotificationDto;
	}

	public CMsgLoadingNumberDto getmCMsgLoadingNumberDto() {
		return mCMsgLoadingNumberDto;
	}

	public void setmCMsgLoadingNumberDto(
			CMsgLoadingNumberDto mCMsgLoadingNumberDto) {
		this.mCMsgLoadingNumberDto = mCMsgLoadingNumberDto;
	}

	public CNoBotherModeDto getmCNoBotherModeDto() {
		return mCNoBotherModeDto;
	}

	public void setmCNoBotherModeDto(CNoBotherModeDto mCNoBotherModeDto) {
		this.mCNoBotherModeDto = mCNoBotherModeDto;
	}

	public boolean isBlockGroupMsg() {
		return isBlockGroupMsg;
	}

	public void setBlockGroupMsg(boolean isBlockGroupMsg) {
		this.isBlockGroupMsg = isBlockGroupMsg;
	}

	@Override
	public String toString() {
		return "ConfigDto [mCMsgNotificationDto=" + mCMsgNotificationDto
				+ ", mCMsgLoadingNumberDto=" + mCMsgLoadingNumberDto
				+ ", mCNoBotherModeDto=" + mCNoBotherModeDto
				+ ", isBlockGroupMsg=" + isBlockGroupMsg + "]";
	}
}
