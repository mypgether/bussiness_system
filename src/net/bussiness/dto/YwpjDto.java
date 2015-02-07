package net.bussiness.dto;

/**
 * Ywpj entity. @author MyEclipse Persistence Tools
 */
public class YwpjDto implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwnrDto ywnr;
	private UserDto userByRemarkerId;
	private byte[] remark;

	// Constructors

	/** default constructor */
	public YwpjDto() {
	}

	/** full constructor */
	public YwpjDto(YwnrDto ywnr, UserDto userByRemarkerId, byte[] remark) {
		this.ywnr = ywnr;
		this.userByRemarkerId = userByRemarkerId;
		this.remark = remark;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public YwnrDto getYwnr() {
		return this.ywnr;
	}

	public void setYwnr(YwnrDto ywnr) {
		this.ywnr = ywnr;
	}

	public UserDto getUserByRemarkerId() {
		return userByRemarkerId;
	}

	public void setUserByRemarkerId(UserDto userByRemarkerId) {
		this.userByRemarkerId = userByRemarkerId;
	}

	public byte[] getRemark() {
		return this.remark;
	}

	public void setRemark(byte[] remark) {
		this.remark = remark;
	}
}