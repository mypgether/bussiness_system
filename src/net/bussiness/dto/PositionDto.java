package net.bussiness.dto;

/**
 * Position entity. @author MyEclipse Persistence Tools
 */
public class PositionDto implements java.io.Serializable {

	// Fields

	private Integer id;
	private DeptDto dept;
	private String description;

	// Constructors

	/** default constructor */
	public PositionDto() {
	}

	/** full constructor */
	public PositionDto(DeptDto dept, String description) {
		this.dept = dept;
		this.description = description;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DeptDto getDept() {
		return this.dept;
	}

	public void setDept(DeptDto dept) {
		this.dept = dept;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "PositionDto [id=" + id + ", dept=" + dept + ", description="
				+ description + "]";
	}

}