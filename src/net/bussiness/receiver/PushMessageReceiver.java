package net.bussiness.receiver;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import net.bussiness.dto.CMsgNotificationDto;
import net.bussiness.dto.CNoBotherModeDto;
import net.bussiness.dto.ConfigDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.Constants;
import net.bussiness.global.IApplication;
import net.bussiness.interfaces.OnPushMessageListener;
import net.bussiness.tools.AppUtils;
import net.bussiness.tools.BDPushUtils;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.NotificationUtils;
import net.bussiness.tools.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbToastUtil;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;

/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 
 * 返回值中的errorCode，解释如下：
 * 
 * <pre>
 * 0 - Success 
 * 10001 - Network Problem 
 * 30600 - Internal Server Error 
 * 30601 - Method Not Allowed 
 * 30602 - Request Params Not Valid
 * 30603 - Authentication Failed 
 * 30604 - Quota Use Up Payment Required 
 * 30605 - Data Required Not Found 
 * 30606 - Request Time Expires Timeout 
 * 30607 - Channel Token Timeout 
 * 30608 - Bind Relation Not Found 
 * 30609 - Bind Number Too Many
 * </pre>
 * 
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 * 
 */
public class PushMessageReceiver extends FrontiaPushMessageReceiver {
	/** TAG to Log */
	public static final String TAG = "TAG";
	public static Activity mActivity = null;
	public static OnPushMessageListener listener = null;

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode非0时为null
	 * @param userId
	 *            应用user id。errorCode非0时为null
	 * @param channelId
	 *            应用channel id。errorCode非0时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		Log.d(TAG, responseString);
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			BDPushUtils.setBind(context, true);
			updateUserPushInfo(context, userId, channelId);
		} else {
			AbToastUtil.showToast(context, "绑定账号出错，请务必重新登录！");
		}
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		String messageString = "透传消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		Log.d(TAG, messageString);

		String title = AppUtils.getApplicationName(context);
		String nfContent = "";
		int notificationId = new Random().nextInt(1000000000);
		// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
		boolean isOpen = false;
		ConfigDto config = ((IApplication) mActivity.getApplication()).mConfig;
		CMsgNotificationDto notificationDto = config.getmCMsgNotificationDto();
		if (!TextUtils.isEmpty(message)) {
			JSONObject messageJson = null;
			try {
				messageJson = new JSONObject(message);
				int msgType = 0;
				if (!messageJson.isNull("msgType")) {
					msgType = messageJson.getInt("msgType");
				}
				String content = null;
				if (!messageJson.isNull("content")) {
					content = messageJson.getString("content");
				}
				listener = (OnPushMessageListener) mActivity;
				if (null != listener) {
					switch (msgType) {
					case Constants.BD_PUSHTYPE_IM_PERSON:
						isOpen = notificationDto.isImPersonOpen();
						listener.onReceiveIMMessage(content, 1, notificationId);
						nfContent = "您有一条聊天消息！";
						break;
					case Constants.BD_PUSHTYPE_IM_GROUP:
						isOpen = notificationDto.isImGroupOpen();
						listener.onReceiveIMMessage(content, 0, notificationId);
						nfContent = "您有一条群聊消息！";
						break;
					case Constants.BD_PUSHTYPE_YWSQ:
						isOpen = notificationDto.isYwsqOpen();
						listener.onReceiveYWSQMessage(content, notificationId);
						nfContent = "您有一条业务申请消息！";
						break;
					case Constants.BD_PUSHTYPE_YWSP:
						isOpen = notificationDto.isYwspOpen();
						listener.onReceiveYWSPMessage(content, notificationId);
						nfContent = "您有一条业务审批消息！";
						break;
					case Constants.BD_PUSHTYPE_YWPJ:
						isOpen = notificationDto.isYwpjOpen();
						listener.onReceiveYWPJMessage(content, notificationId);
						nfContent = "您有一条业务评价回复！";
						break;
					case Constants.BD_PUSHTYPE_YWNR:
						isOpen = notificationDto.isYwnrOpen();
						listener.onReceiveYWNRMessage(content, notificationId);
						nfContent = "您有一条新的业务内容增加！";
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		CNoBotherModeDto noBotherModeDto = config.getmCNoBotherModeDto();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String nowTime = TimeUtils.getCurrentTimeInString(dateFormat);
		System.out.println("nowTime=" + nowTime);
		if (noBotherModeDto.isOpen()
				&& noBotherModeDto.getStartTime().compareTo(
						noBotherModeDto.getEndTime()) <= 0
				&& nowTime.compareTo(noBotherModeDto.getStartTime()) >= 0
				&& nowTime.compareTo(noBotherModeDto.getEndTime()) <= 0) {
			return;
		}
		if (isOpen) {
			NotificationUtils.showNotification(context, title, nfContent,
					notificationId);
		}
	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Log.d(TAG, notifyString);
	}

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param successTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Log.d(TAG, responseString);
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param successTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Log.d(TAG, responseString);
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		// updateContent(context, responseString);
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示列举tag成功；非0表示失败。
	 * @param tags
	 *            当前应用设置的所有tag。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags="
				+ tags;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		// updateContent(context, responseString);
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Log.d(TAG, responseString);

		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			BDPushUtils.setBind(context, false);
		}
	}

	private void updateUserPushInfo(final Context context,
			final String pushUserId, final String pushChannelId) {
		NetworkUtils web = NetworkUtils.newInstance(context);
		AbRequestParams params = new AbRequestParams();
		final IApplication iApp = (IApplication) mActivity.getApplication();
		iApp.mUser.setPushUserId(pushUserId);
		iApp.mUser.setPushChannelId(pushChannelId);
		params.put("userDto", JacksonUtils.bean2Json(iApp.mUser));
		web.post(ConstServer.USER_UPDATE(), params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				AbToastUtil.showToast(context, "已连接到服务器！");
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(context, "绑定账号出错，请务必重新登录！");
			}
		});
	}
}
