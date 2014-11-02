package net.bussiness.dao;

<<<<<<< HEAD
/**
 * Position entity. @author MyEclipse Persistence Tools
 */
=======
@SuppressWarnings("serial")
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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

<<<<<<< HEAD
	// Property accessors
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD

	@Override
	public String toString() {
		return "PositionDao [id=" + id + ", dept=" + dept + ", description="
				+ description + "]";
	}

=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
}