package net.bussiness.db;

import net.bussiness.dto.IMMsgIgnoreUserDto;
import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;

public class IMMsgIgnoreUserDao extends AbDBDaoImpl<IMMsgIgnoreUserDto> {

	public IMMsgIgnoreUserDao(Context context) {
		super(new DBInsideHelper(context), IMMsgIgnoreUserDto.class);
	}
}
