package net.bussiness.tools;

import net.bussiness.dao.UserDao;
import android.app.Application;

public class IApplication extends Application {
	private UserDao currentUser;

	public UserDao getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserDao currentUser) {
		this.currentUser = currentUser;
	}
}
