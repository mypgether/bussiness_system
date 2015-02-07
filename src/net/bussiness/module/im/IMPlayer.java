package net.bussiness.module.im;

import java.io.File;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class IMPlayer {

	private final String TAG = IMPlayer.class.getName();
	private String path;

	private MediaPlayer mPlayer;
	private static IMPlayer imPlayer = null;
	private boolean isPlaying = false;
	private OnMediaCompletionListener listener = null;

	public static IMPlayer newInstance() {
		if (imPlayer == null) {
			imPlayer = new IMPlayer();
		}
		return imPlayer;
	}

	private IMPlayer() {
		mPlayer = new MediaPlayer();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void play() {
		if (isPlaying) {
			mPlayer.stop();
		}
		isPlaying = false;
		try {
			mPlayer.reset();
			// 设置要播放的文件
			mPlayer.setDataSource(path);
			mPlayer.prepareAsync();
			mPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					// 播放
					mp.start();
					isPlaying = true;
				}
			});
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					isPlaying = false;
					if (listener != null)
						listener.onCompletion();
				}
			});
		} catch (Exception e) {
			isPlaying = false;
			Log.e(TAG, "prepare() failed");
		}
	}

	public void setOnMediaCompletionListener(OnMediaCompletionListener listener) {
		this.listener = listener;
	}

	public interface OnMediaCompletionListener {
		public void onCompletion();
	}
}
