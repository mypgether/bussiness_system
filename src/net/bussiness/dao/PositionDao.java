package net.bussiness.dao;

@SuppressWarnings("serial")
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
}