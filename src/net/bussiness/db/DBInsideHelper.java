package net.bussiness.db;

import net.bussiness.dto.ChatUserDto;
import net.bussiness.dto.IMMsgIgnoreUserDto;
import android.content.Context;

import com.ab.db.orm.AbDBHelper;

/**
 * 
 * 名称：DBInsideHelper.java 描述：手机data/data下面的数据库
 * 
 * @date：2013-7-31 下午3:50:18
 * @version v1.0
 */
public class DBInsideHelper extends AbDBHelper {
	// 数据库名
	private static final String DBNAME = "bussiness_sys.db";

	// 当前数据库的版本
	private static final int DBVERSION = 1;
	// 要初始化的表
	private static final Class<?>[] clazz = { ChatUserDto.class,
			IMMsgIgnoreUserDto.class };

	public DBInsideHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}

}
