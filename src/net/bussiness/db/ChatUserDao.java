package net.bussiness.db;

import net.bussiness.db.DBInsideHelper;
import net.bussiness.dto.ChatUserDto;
import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;

public class ChatUserDao extends AbDBDaoImpl<ChatUserDto> {
	public ChatUserDao(Context context) {
		super(new DBInsideHelper(context), ChatUserDto.class);
	}
}
