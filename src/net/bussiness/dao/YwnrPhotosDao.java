package net.bussiness.dao;

import java.util.Arrays;

/**
 * YwnrPhotos entity. @author MyEclipse Persistence Tools
 */
public class YwnrPhotosDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwnrDao ywnr;
	private byte[] photo;

	// Constructors

	/** default constructor */
	public YwnrPhotosDao() {
	}

	/** full constructor */
	public YwnrPhotosDao(YwnrDao ywnr, byte[] photo) {
		this.ywnr = ywnr;
		this.photo = photo;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public YwnrDao getYwnr() {
		return this.ywnr;
	}

	public void setYwnr(YwnrDao ywnr) {
		this.ywnr = ywnr;
	}

	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "YwnrPhotosDao [id=" + id + ", photo=" + Arrays.toString(photo)
				+ "]";
	}

}