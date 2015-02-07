package net.bussiness.dto;

import java.util.Date;

/**
 * Ywnr entity. @author MyEclipse Persistence Tools
 */
public class YwnrDto implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwsqDto ywsq;
	private Date nrTime;
	private String nrLocation;
	private Integer fee;
	private String description;

	// Constructors

	/** default constructor */
	public YwnrDto() {
	}

	/** full constructor */
	public YwnrDto(YwsqDto ywsq, Date nrTime, String nrLocation, Integer fee,
			String description) {
		this.ywsq = ywsq;
		this.nrTime = nrTime;
		this.nrLocation = nrLocation;
		this.fee = fee;
		this.description = description;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public YwsqDto getYwsq() {
		return ywsq;
	}

	public void setYwsq(YwsqDto ywsq) {
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

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "YwnrDto [id=" + id + ", nrTime=" + nrTime + ", nrLocation="
				+ nrLocation + ", fee=" + fee + ", description=" + description
				+ "]";
	}

}