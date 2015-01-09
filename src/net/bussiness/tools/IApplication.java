package net.bussiness.tools;

import net.bussiness.dto.UserDto;

import com.baidu.frontia.FrontiaApplication;

public class IApplication extends FrontiaApplication {
	private UserDto currentUser;

	public UserDto getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserDto currentUser) {
		this.currentUser = currentUser;
	}
}
