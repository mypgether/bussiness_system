package net.bussiness.dao;

@SuppressWarnings("serial")
public class YwpjDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwsqDao ywsq;
	private UserDao user;
	private double ranker;
	private String remark;

	// Constructors

	/** default constructor */
	public YwpjDao() {
	}

	/** full constructor */
	public YwpjDao(YwsqDao ywsq, UserDao user, double ranker, String remark) {
		this.ywsq = ywsq;
		this.user = user;
		this.ranker = ranker;
		this.remark = remark;
	}

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

	public UserDao getUser() {
		return this.user;
	}

	public void setUser(UserDao user) {
		this.user = user;
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
}