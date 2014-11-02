package net.bussiness.dao;

/**
 * Ywpj entity. @author MyEclipse Persistence Tools
 */
public class YwpjDao implements java.io.Serializable {

	// Fields

	private Integer id;
	private YwsqDao ywsq;
	private Integer remarkerId;
	private double ranker;
	private String remark;

	// Constructors

	/** default constructor */
	public YwpjDao() {
	}

	/** full constructor */
	public YwpjDao(YwsqDao ywsq, Integer remarkerId, double ranker,
			String remark) {
		this.ywsq = ywsq;
		this.remarkerId = remarkerId;
		this.ranker = ranker;
		this.remark = remark;
	}

	// Property accessors
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

	public Integer getRemarkerId() {
		return this.remarkerId;
	}

	public void setRemarkerId(Integer remarkerId) {
		this.remarkerId = remarkerId;
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

	@Override
	public String toString() {
		return "YwpjDao [id=" + id + ", remarkerId=" + remarkerId + ", ranker="
				+ ranker + ", remark=" + remark + "]";
	}

}