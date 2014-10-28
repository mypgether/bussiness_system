package net.bussiness.tools;

import android.content.Context;

import com.ab.http.AbHttpListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;

public class NetworkWeb {

	private AbHttpUtil mAbHttpUtil = null;
	private Context mContext = null;

	public NetworkWeb(Context context) {
		mContext = context;
		mAbHttpUtil = AbHttpUtil.getInstance(context);
	}

	/**
	 * Create a new instance of SettingWeb.
	 */
	public static NetworkWeb newInstance(Context context) {
		NetworkWeb web = new NetworkWeb(context);
		return web;
	}

	public void get(String urlString, AbRequestParams params,
			final AbHttpListener abHttpListener) {
		mAbHttpUtil.get(urlString, params, new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					// ģ������
					// AbResult result = new AbResult(content);
					// if (result.getResultCode() > 0) {
					// // �ɹ�
					// // ArticleListResult mArticleListResult =
					// // (ArticleListResult) AbJsonUtil
					// // .fromJson(content, ArticleListResult.class);
					// // List<Article> articleList = mArticleListResult
					// // .getItems();
					// // ��������ݻ�ȥ
					// abHttpListener.onSuccess(content);
					// } else {
					// // ��������Ϣ���ݻ�ȥ
					// abHttpListener.onFailure(result.getResultMessage());
					// }
					abHttpListener.onSuccess(content);
				} catch (Exception e) {
					e.printStackTrace();
					abHttpListener.onFailure(e.getMessage());
				}
			}

			@Override
			public void onStart() {
				// ��ʼ��״̬���ݻ�ȥ
			}

			@Override
			public void onFinish() {
				// ��ɵ�״̬���ݻ�ȥ
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				// ��ʧ�ܴ�����Ϣ���ݻ�ȥ
				abHttpListener.onFailure(content);
			}
		});
	}
}