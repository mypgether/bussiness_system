package net.bussiness.global;

public class ConstServer {
	// 内网
	private static StringBuffer SERVER_ROOT = new StringBuffer(
			"http://192.168.191.1:8080/bussiness/");

	// nat123
	// private static StringBuffer SERVER_ROOT = new StringBuffer(
	// "http://gether.nat123.net:43169/bussiness/");

	// 花生壳
	// private static StringBuffer SERVER_ROOT = new StringBuffer(
	// "http://gether.vicp.cc:29979/bussiness/");

	/*
	 * 登录模块开始
	 */
	public static String LOGIN() {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("login").toString();
	}

	/*
	 * 登录模块结束
	 */

	/*
	 * 用户模块开始
	 */
	public static StringBuffer GET_USER_ROOT() {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("user/");
	}

	public static String USER_ADD() {
		return GET_USER_ROOT().append("add").toString();
	}

	public static String USER_DELETE() {
		return GET_USER_ROOT().append("delete").toString();
	}

	public static String USER_UPDATE() {
		return GET_USER_ROOT().append("update").toString();
	}

	public static String USER_LOAD(int userId) {
		return GET_USER_ROOT().append(userId).append("/load").toString();
	}

	public static String USER_FINDPWD(int userId) {
		return GET_USER_ROOT().append(userId).append("/findPsd").toString();
	}

	public static String USER_FINDUSERWITHPC() {
		return GET_USER_ROOT().append("findUserWithPage").toString();
	}

	public static String USER_DOWNLOADLOGO(int userId, String fileName) {
		return GET_USER_ROOT().append(userId).append("/downloadLogo/")
				.append(fileName).toString();
	}

	public static String USER_UPLOADLOGO(int userId) {
		return GET_USER_ROOT().append(userId).append("/uploadLogo").toString();
	}

	/*
	 * 用户模块结束
	 */

	/*
	 * 实时聊天模块
	 */
	/*
	 * 个人聊天
	 */
	public static StringBuffer GET_IM_PERSON_ROOT() {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("im/person/");
	}

	public static StringBuffer GET_IM_PERSON_ROOT(int userId) {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("im/person/").append(userId).append("/");
	}

	public static String IM_PERSON_ADD(int userId) {
		return GET_IM_PERSON_ROOT(userId).append("add").toString();
	}

	public static String IM_PERSON_DELETE(int userId) {
		return GET_IM_PERSON_ROOT(userId).append("delete").toString();
	}

	public static String IM_PERSON_CLEAR(int senderId, int receiverId) {
		return GET_IM_PERSON_ROOT(senderId).append(receiverId).append("/clear")
				.toString();
	}

	public static String IM_PERSON_FINDIMSWITHPC(int userId) {
		return GET_IM_PERSON_ROOT(userId).append("findPersonIMsWithPC")
				.toString();
	}

	public static String IM_PERSON_DOWNLOADPHOTO(int userBySenderId,
			int userByReceiverId, String fileName) {
		return GET_IM_PERSON_ROOT().append(userBySenderId).append("/")
				.append(userByReceiverId).append("/downloadPhoto/")
				.append(fileName).toString();
	}

	public static String IM_PERSON_UPLOADPHOTO(int userBySenderId,
			int userByReceiverId, int msgId) {
		return GET_IM_PERSON_ROOT().append(userBySenderId).append("/")
				.append(userByReceiverId).append("/").append(msgId)
				.append("/uploadPhoto/").toString();
	}

	public static String IM_PERSON_DOWNLOADRECORD(int userBySenderId,
			int userByReceiverId, String fileName) {
		return GET_IM_PERSON_ROOT().append(userBySenderId).append("/")
				.append(userByReceiverId).append("/downloadRecord/")
				.append(fileName).toString();
	}

	public static String IM_PERSON_UPLOADRECORD(int userBySenderId,
			int userByReceiverId, int msgId) {
		return GET_IM_PERSON_ROOT().append(userBySenderId).append("/")
				.append(userByReceiverId).append("/").append(msgId)
				.append("/uploadRecord/").toString();
	}

	/*
	 * 群组聊天
	 */
	public static StringBuffer GET_IM_GROUP_ROOT() {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("im/group/");
	}

	public static StringBuffer GET_IM_GROUP_ROOT(int userId) {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("im/group/").append(userId).append("/");
	}

	public static String IM_GROUP_ADD(int userId) {
		return GET_IM_GROUP_ROOT(userId).append("add").toString();
	}

	public static String IM_GROUP_DELETE(int userId) {
		return GET_IM_GROUP_ROOT(userId).append("delete").toString();
	}

	public static String IM_GROUP_CLEAR(int senderId) {
		return GET_IM_GROUP_ROOT(senderId).append("clear").toString();
	}

	public static String IM_GROUP_FINDIMSWITHPC(int userId, int groupId) {
		return GET_IM_GROUP_ROOT(userId).append("/").append(groupId)
				.append("/findGroupIMsWithPC").toString();
	}

	public static String IM_GROUP_DOWNLOADPHOTO(int userBySenderId,
			String fileName) {
		return GET_IM_GROUP_ROOT().append(userBySenderId)
				.append("/downloadPhoto/").append(fileName).toString();
	}

	public static String IM_GROUP_UPLOADPHOTO(int userBySenderId, int msgId) {
		return GET_IM_GROUP_ROOT().append(userBySenderId).append("/")
				.append(msgId).append("/uploadPhoto/").toString();
	}

	public static String IM_GROUP_DOWNLOADRECORD(int userBySenderId,
			String fileName) {
		return GET_IM_GROUP_ROOT().append(userBySenderId)
				.append("/downloadRecord/").append(fileName).toString();
	}

	public static String IM_GROUP_UPLOADRECORD(int userBySenderId, int msgId) {
		return GET_IM_GROUP_ROOT().append(userBySenderId).append("/")
				.append(msgId).append("/uploadRecord/").toString();
	}

	/*
	 * 实时聊天模块结束
	 */

	/*
	 * 业务申请模块
	 */
	public static StringBuffer GET_YWSQ_ROOT(int userId) {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("ywsq/").append(userId).append("/");
	}

	public static String YWSQ_ADD(int userId) {
		return GET_YWSQ_ROOT(userId).append("add").toString();
	}

	public static String YWSQ_DELETE(int userId) {
		return GET_YWSQ_ROOT(userId).append("delete").toString();
	}

	public static String YWSQ_UPDATE(int userId) {
		return GET_YWSQ_ROOT(userId).append("update").toString();
	}

	// 查询自己的申请
	public static String YWSQ_FINDPROPOSE_WITHPC(int userId) {
		return GET_YWSQ_ROOT(userId).append("findProposeYwsqsWithPC")
				.toString();
	}

	// 领导查询自己的待审批
	public static String YWSQ_FINDAPPROVEING_WITHPC(int userId, int deptId) {
		return GET_YWSQ_ROOT(userId).append("findApproveYwsqsApprovingWithPC/")
				.append(deptId).toString();
	}

	// 领导查询自己的已审批
	public static String YWSQ_FINDAPPROVE_WITHPC(int userId) {
		return GET_YWSQ_ROOT(userId).append("findApproveYwsqsWithPC")
				.toString();
	}

	/*
	 * 业务申请模块结束
	 */

	/*
	 * 业务内容模块开始
	 */
	public static StringBuffer GET_YWNR_ROOT(int userId, int ywId) {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("ywnr/").append(userId).append("/").append(ywId)
				.append("/");
	}

	public static String YWNR_ADD(int userId, int ywId) {
		return GET_YWNR_ROOT(userId, ywId).append("add").toString();
	}

	public static String YWNR_DELETE(int userId, int ywId) {
		return GET_YWNR_ROOT(userId, ywId).append("delete").toString();
	}

	public static String YWNR_UPDATE(int userId, int ywId) {
		return GET_YWNR_ROOT(userId, ywId).append("update").toString();
	}

	public static String YWNR_FINDWITHPC(int userId, int ywId) {
		return GET_YWNR_ROOT(userId, ywId).append("findYwnrsWithPC").toString();
	}

	/*
	 * 业务内容模块结束
	 */

	/*
	 * 业务内容图片模块开始
	 */
	public static StringBuffer GET_YWNR_PHOTO_ROOT(int userId, int ywId,
			int ywnrId) {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("ywnrPhotos/").append(userId).append("/").append(ywId)
				.append("/").append(ywnrId).append("/");
	}

	public static String YWNR_PHOTO_DOWNLOADPHOTO(int userId, int ywId,
			int ywnrId, String photoPath) {
		return GET_YWNR_PHOTO_ROOT(userId, ywId, ywnrId)
				.append("downloadPhoto").append("/").append(photoPath)
				.toString();
	}

	public static String YWNR_PHOTO_UPLOADPHOTO(int userId, int ywId, int ywnrId) {
		return GET_YWNR_PHOTO_ROOT(userId, ywId, ywnrId).append("uploadPhoto")
				.toString();
	}

	public static String YWNR_PHOTO_FINDWITHPC(int userId, int ywId, int ywnrId) {
		return GET_YWNR_PHOTO_ROOT(userId, ywId, ywnrId).append(
				"findYwnrPhotosWithPC").toString();
	}

	/*
	 * 业务内容图片模块结束
	 */

	/*
	 * 业务内容评价模块开始
	 */
	public static StringBuffer GET_YWNR_PJ_ROOT(int userId, int ywId, int ywnrId) {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("ywnrPj/").append(userId).append("/").append(ywId)
				.append("/").append(ywnrId).append("/");
	}

	public static String YWNR_PJ_ADD(int userId, int ywId, int ywnrId) {
		return GET_YWNR_PJ_ROOT(userId, ywId, ywnrId).append("add").toString();
	}

	public static String YWNR_PJ_DELETE(int userId, int ywId, int ywnrId) {
		return GET_YWNR_PJ_ROOT(userId, ywId, ywnrId).append("delete")
				.toString();
	}

	public static String YWNR_PJ_UPDATE(int userId, int ywId, int ywnrId) {
		return GET_YWNR_PJ_ROOT(userId, ywId, ywnrId).append("update")
				.toString();
	}

	public static String YWNR_PJ_FINDWITHPC(int userId, int ywId, int ywnrId) {
		return GET_YWNR_PJ_ROOT(userId, ywId, ywnrId)
				.append("findYwnrPjWithPC").toString();
	}

	/*
	 * 业务内容评价模块结束
	 */

	/*
	 * 部门模块开始
	 */
	public static StringBuffer GET_DEPT_ROOT() {
		StringBuffer sb = new StringBuffer(SERVER_ROOT.toString());
		return sb.append("dept/");
	}

	public static String DEPT_ADD() {
		return GET_DEPT_ROOT().append("add").toString();
	}

	public static String DEPT_DELETE() {
		return GET_DEPT_ROOT().append("delete").toString();
	}

	public static String DEPT_UPDATE() {
		return GET_DEPT_ROOT().append("update").toString();
	}

	public static String DEPT_FINDDEPTCREATER(int createrId) {
		return GET_DEPT_ROOT().append(createrId).append("/findDeptCreater")
				.toString();
	}

	public static String DEPT_FINDDEPTWITHPC() {
		return GET_DEPT_ROOT().append("findDeptWithPage").toString();
	}
	/*
	 * 部门模块结束
	 */
}