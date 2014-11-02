package net.bussiness.dao;

import java.util.Date;

<<<<<<< HEAD
/**
 * Ywnr entity. @author MyEclipse Persistence Tools
 */
=======
@SuppressWarnings("serial")
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
public class YwnrDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwsqDao ywsq;
	private Date nrTime;
	private String nrLocation;
<<<<<<< HEAD
=======
	private byte[] nrPhoto;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	private String description;

	// Constructors

	/** default constructor */
	public YwnrDao() {
	}

	/** full constructor */
	public YwnrDao(YwsqDao ywsq, Date nrTime, String nrLocation,
<<<<<<< HEAD
			String description) {
		this.ywsq = ywsq;
		this.nrTime = nrTime;
		this.nrLocation = nrLocation;
=======
			byte[] nrPhoto, String description) {
		this.ywsq = ywsq;
		this.nrTime = nrTime;
		this.nrLocation = nrLocation;
		this.nrPhoto = nrPhoto;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD
		return ywsq;
=======
		return this.ywsq;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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

<<<<<<< HEAD
=======
	public byte[] getNrPhoto() {
		return this.nrPhoto;
	}

	public void setNrPhoto(byte[] nrPhoto) {
		this.nrPhoto = nrPhoto;
	}

>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
<<<<<<< HEAD

	@Override
	public String toString() {
		return "YwnrDao [id=" + id + ", nrTime=" + nrTime + ", nrLocation="
				+ nrLocation + ", description=" + description + "]";
	}

=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
}