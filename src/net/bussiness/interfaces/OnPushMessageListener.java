package net.bussiness.interfaces;

public interface OnPushMessageListener {

	/**
	 * <pre>
	 * Purpose:接收到聊天消息
	 * @author Myp
	 * Create Time: 2015-1-15 下午8:11:09
	 * @param content 消息内容
	 * @param msgType 消息类型,0表示群聊，1表示个人聊天
	 * @param notificationId 通知的ID,可用于删除通知
	 * Version: 1.0
	 * </pre>
	 */
	public void onReceiveIMMessage(String content, int msgType,
			int notificationId);

	/**
	 * <pre>
	 * Purpose:接收到业务申请消息
	 * @author Myp
	 * Create Time: 2015-1-22 下午1:57:14
	 * @param content
	 * @param notificationId 通知的ID,可用于删除通知
	 * Version: 1.0
	 * </pre>
	 */
	public void onReceiveYWSQMessage(String content, int notificationId);

	/**
	 * <pre>
	 * Purpose:接收到业务审批消息
	 * @author Myp
	 * Create Time: 2015-1-22 下午1:57:14
	 * @param content
	 * @param notificationId 通知的ID,可用于删除通知
	 * Version: 1.0
	 * </pre>
	 */
	public void onReceiveYWSPMessage(String content, int notificationId);

	/**
	 * <pre>
	 * Purpose:接收到业务评价消息
	 * @author Myp
	 * Create Time: 2015-1-22 下午1:57:14
	 * @param content
	 * @param notificationId 通知的ID,可用于删除通知
	 * Version: 1.0
	 * </pre>
	 */
	public void onReceiveYWPJMessage(String content, int notificationId);

	/**
	 * <pre>
	 * Purpose:接收到业务详细添加消息
	 * @author Myp
	 * Create Time: 2015-1-22 下午1:57:14
	 * @param content
	 * @param notificationId 通知的ID,可用于删除通知
	 * Version: 1.0
	 * </pre>
	 */
	public void onReceiveYWNRMessage(String content, int notificationId);
}
