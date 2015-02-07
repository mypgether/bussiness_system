package net.bussiness.dto;

/**
 * YwnrPhotos entity. @author MyEclipse Persistence Tools
 */
public class YwnrPhotosDto implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwnrDto ywnr;
	private String photoPath;

	// Constructors

	/** default constructor */
	public YwnrPhotosDto() {
	}

	/** full constructor */
	public YwnrPhotosDto(YwnrDto ywnr, String photoPath) {
		this.ywnr = ywnr;
		this.photoPath = photoPath;
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

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	@Override
	public String toString() {
		return "YwnrPhotosDto [id=" + id + ", photoPath=" + photoPath + "]";
	}
}