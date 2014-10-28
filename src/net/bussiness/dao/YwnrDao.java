package net.bussiness.dao;

import java.util.Date;

@SuppressWarnings("serial")
public class YwnrDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwsqDao ywsq;
	private Date nrTime;
	private String nrLocation;
	private byte[] nrPhoto;
	private String description;

	// Constructors

	/** default constructor */
	public YwnrDao() {
	}

	/** full constructor */
	public YwnrDao(YwsqDao ywsq, Date nrTime, String nrLocation,
			byte[] nrPhoto, String description) {
		this.ywsq = ywsq;
		this.nrTime = nrTime;
		this.nrLocation = nrLocation;
		this.nrPhoto = nrPhoto;
		this.description = description;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public YwsqDao getYwsq() {
		return this.ywsq;
	}

	public void setYwsq(YwsqDao ywsq) {
		this.ywsq = ywsq;
	}

	public Date getNrTime() {
		return this.nrTime;
	}

	public void setNrTime(Date nrTime) {
		this.nrTime = nrTime;
	}

	public String getNrLocation() {
		return this.nrLocation;
	}

	public void setNrLocation(String nrLocation) {
		this.nrLocation = nrLocation;
	}

	public byte[] getNrPhoto() {
		return this.nrPhoto;
	}

	public void setNrPhoto(byte[] nrPhoto) {
		this.nrPhoto = nrPhoto;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}