package net.bussiness.tools;

public class ConstServer {
	// private static StringBuffer SERVER_ROOT = new StringBuffer(
	// "http://192.168.191.1:8080/bussiness/");

	// private static StringBuffer SERVER_ROOT = new StringBuffer(
	// "http://192.168.2.127:8080/bussiness/");
	private static StringBuffer SERVER_ROOT = new StringBuffer(
			"http://10.0.2.2::8080/bussiness/");

	/*
	 * 登录模块
	 */
	public static String LOGIN() {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("login").toString();
	}

	/*
	 * 业务申请模块
	 */
	public static StringBuffer GET_YWSQ_ROOT(String userId) {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("ywsq/").append(userId).append("/");
	}

	public static String YWSQ_ADD(String userId) {
		return GET_YWSQ_ROOT(userId).append("add").toString();
	}

	public static String YWSQ_DELETE(String userId) {
		return GET_YWSQ_ROOT(userId).append("delete").toString();
	}

	public static String YWSQ_UPDATE(String userId) {
		return GET_YWSQ_ROOT(userId).append("update").toString();
	}

	public static String YWSQ_FINDALL() {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("ywsq/findYwsqsWithPage").toString();
	}

	// 查询自己的申请
	public static String YWSQ_FINDPROPOSE_WITHPC(String userId) {
		return GET_YWSQ_ROOT(userId).append("findProposeYwsqsWithPC")
				.toString();
	}

	// 查询自己的审批
	public static String YWSQ_FINDAPPROVE_WITHPC(String userId) {
		return GET_YWSQ_ROOT(userId).append("findApproveYwsqsWithPC")
				.toString();
	}
}
