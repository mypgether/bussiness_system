package net.bussiness.tools;

import android.content.Context;

import com.ab.http.AbHttpListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;

public class NetworkWeb {
	private static NetworkWeb web = null;
	private AbHttpUtil mAbHttpUtil = null;
	private Context mContext = null;

	private NetworkWeb(Context context) {
		mContext = context;
		mAbHttpUtil = AbHttpUtil.getInstance(context);
	}

	/**
	 * Create a new instance of SettingWeb.
	 */
	public static NetworkWeb newInstance(Context context) {
		if (web == null) {
			web = new NetworkWeb(context);
		}
		return web;
	}

	public void post(String urlString, AbRequestParams params,
			final AbHttpListener abHttpListener) {
		mAbHttpUtil.post(urlString, params, new AbStringHttpResponseListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				System.out.println("code=" + statusCode + " "
						+ error.toString());
				abHttpListener.onFailure(content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				abHttpListener.onSuccess(content);
			}
		});
	}

	public void get(String urlString, AbRequestParams params,
			final AbHttpListener abHttpListener) {
		mAbHttpUtil.get(urlString, params, new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				// try {
				// 模拟数据
				// AbResult result = new AbResult(content);
				// if (result.getResultCode() > 0) {
				// // 成功
				// // ArticleListResult mArticleListResult =
				// // (ArticleListResult) AbJsonUtil
				// // .fromJson(content, ArticleListResult.class);
				// // List<Article> articleList = mArticleListResult
				// // .getItems();
				// // 将结果传递回去
				// abHttpListener.onSuccess(content);
				// } else {
				// // 将错误信息传递回去
				// abHttpListener.onFailure(result.getResultMessage());
				// }
				abHttpListener.onSuccess(content);
				// } catch (Exception e) {
				// e.printStackTrace();
				// abHttpListener.onFailure(e.getMessage());
				// }
			}

			@Override
			public void onStart() {
				// 开始的状态传递回去
			}

			@Override
			public void onFinish() {
				// 完成的状态传递回去
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				abHttpListener.onFailure(content);
			}
		});
	}
}