package net.bussiness.tools;

import java.io.File;

import android.content.Context;

import com.ab.http.AbBinaryHttpResponseListener;
import com.ab.http.AbFileHttpResponseListener;
import com.ab.http.AbHttpListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;

public class NetworkUtils {
	private static NetworkUtils web = null;
	private AbHttpUtil mAbHttpUtil = null;
	private Context mContext = null;

	private NetworkUtils(Context context) {
		mContext = context;
		mAbHttpUtil = AbHttpUtil.getInstance(context);
	}

	public static NetworkUtils newInstance(Context context) {
		if (web == null) {
			web = new NetworkUtils(context);
		}
		return web;
	}

	/**
	 * <pre>
	 * Purpose:普通的post请求
	 * @author Myp
	 * Create Time: 2015-1-26 下午4:52:27
	 * @param urlString
	 * @param params
	 * @param abHttpListener
	 * Version: 1.0
	 * </pre>
	 */
	public void post(String urlString, AbRequestParams params,
			final AbHttpListener abHttpListener) {
		mAbHttpUtil.post(urlString, params, new AbStringHttpResponseListener() {
			@Override
			public void onStart() {
				abHttpListener.onStart();
			}

			@Override
			public void onFinish() {
				abHttpListener.onFinish();
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

	/**
	 * <pre>
	 * Purpose:普通的post请求
	 * @author Myp
	 * Create Time: 2015-1-26 下午4:52:27
	 * @param urlString
	 * @param params
	 * @param abHttpListener
	 * Version: 1.0
	 * </pre>
	 */
	public void post(String urlString, AbRequestParams params,
			final AbFileHttpResponseListener abFileHttpResponseListener) {
		mAbHttpUtil.post(urlString, params, new AbFileHttpResponseListener() {

			@Override
			public void onStart() {
				abFileHttpResponseListener.onStart();
			}

			@Override
			public void onFinish() {
				abFileHttpResponseListener.onFinish();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				abFileHttpResponseListener
						.onFailure(statusCode, content, error);
			}

			@Override
			public void onSuccess(int statusCode, File file) {
				abFileHttpResponseListener.onSuccess(statusCode, file);
			}

			@Override
			public void onSuccess(int statusCode) {
				abFileHttpResponseListener.onSuccess(statusCode);
			}
		});
	}

	public void get(String urlString, AbRequestParams params,
			final AbHttpListener abHttpListener) {
		mAbHttpUtil.get(urlString, params, new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				abHttpListener.onSuccess(content);
			}

			@Override
			public void onStart() {
				abHttpListener.onStart();
			}

			@Override
			public void onFinish() {
				abHttpListener.onFinish();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				abHttpListener.onFailure(content);
			}
		});
	}

	public void post(String url, AbRequestParams params,
			final AbBinaryHttpResponseListener abBinaryHttpResponseListener) {
		mAbHttpUtil.post(url, params, new AbBinaryHttpResponseListener() {

			@Override
			public void onStart() {
				abBinaryHttpResponseListener.onStart();
			}

			@Override
			public void onFinish() {
				abBinaryHttpResponseListener.onFinish();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				abBinaryHttpResponseListener.onFailure(statusCode, content,
						error);
			}

			@Override
			public void onSuccess(int statusCode, byte[] content) {
				abBinaryHttpResponseListener.onSuccess(statusCode, content);
			}
		});
	}
}