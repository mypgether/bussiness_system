package net.bussiness.tools;

import android.content.Context;

import com.ab.http.AbHttpListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;

public class NetworkWeb {
<<<<<<< HEAD
	private static NetworkWeb web = null;
	private AbHttpUtil mAbHttpUtil = null;
	private Context mContext = null;

	private NetworkWeb(Context context) {
=======

	private AbHttpUtil mAbHttpUtil = null;
	private Context mContext = null;

	public NetworkWeb(Context context) {
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
		mContext = context;
		mAbHttpUtil = AbHttpUtil.getInstance(context);
	}

	/**
	 * Create a new instance of SettingWeb.
	 */
	public static NetworkWeb newInstance(Context context) {
<<<<<<< HEAD
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
				abHttpListener.onFailure(content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				abHttpListener.onSuccess(content);
			}
		});
	}

=======
		NetworkWeb web = new NetworkWeb(context);
		return web;
	}

>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	public void get(String urlString, AbRequestParams params,
			final AbHttpListener abHttpListener) {
		mAbHttpUtil.get(urlString, params, new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
<<<<<<< HEAD
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
=======
				try {
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
				} catch (Exception e) {
					e.printStackTrace();
					abHttpListener.onFailure(e.getMessage());
				}
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD
=======
				// 将失败错误信息传递回去
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
				abHttpListener.onFailure(content);
			}
		});
	}
}