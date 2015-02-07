package net.bussiness.global;

import net.bussiness.activities.R;
import net.bussiness.db.ChatUserDao;
import net.bussiness.db.IMMsgIgnoreUserDao;
import net.bussiness.dto.ConfigDto;
import net.bussiness.dto.UserDto;
import net.bussiness.tools.DataManagerUtils;
import net.bussiness.tools.JacksonUtils;

import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbStorageQuery;
import com.ab.image.AbImageLoader;
import com.ab.util.AbSharedUtil;
import com.baidu.frontia.FrontiaApplication;

public class IApplication extends FrontiaApplication {
	public UserDto mUser = null;
	/*
	 * 用户身份，1表示领导,2表示普通用户
	 */
	public int mUserIdentity = 2;
	// 图片下载器
	public AbImageLoader mAbImageLoader = null;

	public AbSqliteStorage mAbSqliteStorage = null;
	public ChatUserDao mUserDao = null;
	public IMMsgIgnoreUserDao mIMMsgIgnoreUserDao = null;
	public ConfigDto mConfig = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mUser = getUserfromSharedPreference();
		mUserIdentity = getUserIndentityfromSharedPreference();
		// 图片下载器
		mAbImageLoader = new AbImageLoader(this);
		mAbImageLoader.setMaxWidth(0);
		mAbImageLoader.setMaxHeight(0);
		mAbImageLoader.setLoadingImage(R.drawable.image_loading);
		mAbImageLoader.setErrorImage(R.drawable.image_error);
		mAbImageLoader.setEmptyImage(R.drawable.image_empty);

		mAbSqliteStorage = AbSqliteStorage.getInstance(this);
		mUserDao = new ChatUserDao(this);
		mIMMsgIgnoreUserDao = new IMMsgIgnoreUserDao(this);

		String json = getConfigfromSharedPreference();
		if (json == null || "".equals(json)) {
			mConfig = new ConfigDto();
		} else {
			mConfig = (ConfigDto) JacksonUtils.json2Bean(json, ConfigDto.class);
		}
	}

	public UserDto getUserfromSharedPreference() {
		UserDto currentUser = null;
		int userId = AbSharedUtil.getInt(getApplicationContext(), "userId");
		if (userId != 0) {
			currentUser = new UserDto();
			currentUser.setUserId(userId);
			currentUser.setPassword(AbSharedUtil.getString(
					getApplicationContext(), "pwd"));
		}
		return currentUser;
	}

	public int getUserIndentityfromSharedPreference() {
		int userIndentity = AbSharedUtil.getInt(getApplicationContext(),
				"userIndentity");
		return userIndentity;
	}

	public String getConfigfromSharedPreference() {
		String config = AbSharedUtil.getString(getApplicationContext(),
				"config");
		return config;
	}

	public void save2SharedPreference(UserDto currentUser, int userIndentity) {
		AbSharedUtil.putInt(getApplicationContext(), "userId",
				currentUser.getUserId());
		AbSharedUtil.putString(getApplicationContext(), "pwd",
				currentUser.getPassword());
		AbSharedUtil.putInt(getApplicationContext(), "userIndentity",
				userIndentity);
	}

	public void saveConfig2SharedPreference(String config) {
		AbSharedUtil.putString(getApplicationContext(), "config", config);
	}

	public void clearSharedPreference() {
		AbSharedUtil.putInt(getApplicationContext(), "userId", 0);
		AbSharedUtil.putString(getApplicationContext(), "pwd", "");
		AbSharedUtil.putInt(getApplicationContext(), "userIndentity", 0);
		AbSharedUtil.putBoolean(getApplicationContext(), "isBlockGroupMsg",
				false);
		AbSharedUtil.putString(getApplicationContext(), "config", "");
	}

	public void clearDatebase() {
		mAbSqliteStorage.deleteData(new AbStorageQuery(), mUserDao, null);
		mAbSqliteStorage.deleteData(new AbStorageQuery(), mIMMsgIgnoreUserDao,
				null);
	}

	public void switchAccount() {
		this.mUser = null;
		this.mUserIdentity = 0;
		DataManagerUtils.cleanApplicationData(getApplicationContext());
	}
}
