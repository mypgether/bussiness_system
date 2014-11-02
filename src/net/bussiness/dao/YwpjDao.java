package net.bussiness.dao;

<<<<<<< HEAD
/**
 * Ywpj entity. @author MyEclipse Persistence Tools
 */
=======
@SuppressWarnings("serial")
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
public class YwpjDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwsqDao ywsq;
<<<<<<< HEAD
	private Integer remarkerId;
=======
	private UserDao user;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	private double ranker;
	private String remark;

	// Constructors

	/** default constructor */
	public YwpjDao() {
	}

	/** full constructor */
<<<<<<< HEAD
	public YwpjDao(YwsqDao ywsq, Integer remarkerId, double ranker,
			String remark) {
		this.ywsq = ywsq;
		this.remarkerId = remarkerId;
=======
	public YwpjDao(YwsqDao ywsq, UserDao user, double ranker, String remark) {
		this.ywsq = ywsq;
		this.user = user;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
		this.ranker = ranker;
		this.remark = remark;
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

	public YwsqDao getYwsq() {
		return this.ywsq;
	}

	public void setYwsq(YwsqDao ywsq) {
		this.ywsq = ywsq;
	}

<<<<<<< HEAD
	public Integer getRemarkerId() {
		return this.remarkerId;
	}

	public void setRemarkerId(Integer remarkerId) {
		this.remarkerId = remarkerId;
=======
	public UserDao getUser() {
		return this.user;
	}

	public void setUser(UserDao user) {
		this.user = user;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	}

	public double getRanker() {
		return this.ranker;
	}

	public void setRanker(double ranker) {
		this.ranker = ranker;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
<<<<<<< HEAD

	@Override
	public String toString() {
		return "YwpjDao [id=" + id + ", remarkerId=" + remarkerId + ", ranker="
				+ ranker + ", remark=" + remark + "]";
	}

=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
}