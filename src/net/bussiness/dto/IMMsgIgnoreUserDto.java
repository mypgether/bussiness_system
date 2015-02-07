package net.bussiness.dto;

import java.io.Serializable;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name = "im_ignore_user")
public class IMMsgIgnoreUserDto implements Serializable {
	@Id
	@Column(name = "_id")
	private int id;
	@Column(name = "userId")
	private int userId;

	public IMMsgIgnoreUserDto() {
		super();
	}

	public IMMsgIgnoreUserDto(int id, int userId) {
		super();
		this.id = id;
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "IMMsgIgnoreUserDto [id=" + id + ", userId=" + userId + "]";
	}
}
