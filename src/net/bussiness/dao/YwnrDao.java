package net.bussiness.dao;

import java.util.Date;

/**
 * Ywnr entity. @author MyEclipse Persistence Tools
 */
public class YwnrDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwsqDao ywsq;
	private Date nrTime;
	private String nrLocation;
	private String description;

	// Constructors

	/** default constructor */
	public YwnrDao() {
	}

	/** full constructor */
	public YwnrDao(YwsqDao ywsq, Date nrTime, String nrLocation,
			String description) {
		this.ywsq = ywsq;
		this.nrTime = nrTime;
		this.nrLocation = nrLocation;
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
		return ywsq;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "YwnrDao [id=" + id + ", nrTime=" + nrTime + ", nrLocation="
				+ nrLocation + ", description=" + description + "]";
	}

}