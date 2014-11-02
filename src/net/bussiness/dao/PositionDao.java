package net.bussiness.dao;

/**
 * Position entity. @author MyEclipse Persistence Tools
 */
public class PositionDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private DeptDao dept;
	private String description;

	// Constructors

	/** default constructor */
	public PositionDao() {
	}

	/** full constructor */
	public PositionDao(DeptDao dept, String description) {
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

	public DeptDao getDept() {
		return this.dept;
	}

	public void setDept(DeptDao dept) {
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
		return "PositionDao [id=" + id + ", dept=" + dept + ", description="
				+ description + "]";
	}

}