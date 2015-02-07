package net.bussiness.module.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

public class EmotionParser {
	private Context mContext;
	private Pattern mPattern;
	private HashMap<String, Integer> mEmotionToRes;
	private List<Integer> mEmotionList;
	public static final int EMOTION_COUNT = 50;

	public EmotionParser(Context context) {
		mContext = context;
		mEmotionList = buildEmotionList();
		mEmotionToRes = buildEmotionToRes();
		mPattern = buildPattern();

	}

	public List<Integer> getmEmotionList() {
		return mEmotionList;
	}

	public void setmEmotionList(List<Integer> mEmotionList) {
		this.mEmotionList = mEmotionList;
	}

	private List<Integer> buildEmotionList() {
		List<Integer> tempList = new ArrayList<Integer>();
		for (int i = 1; i <= 50; i++)
			tempList.add(mContext.getResources()
					.getIdentifier("image_emoticon" + i, "drawable",
							mContext.getPackageName()));
		return tempList;
	}

	private HashMap<String, Integer> buildEmotionToRes() {
		HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>();
		for (int i = 0; i < EMOTION_COUNT; i++) {
			smileyToRes.put(
					"[emotion" + i + "]",
					mContext.getResources().getIdentifier(
							"image_emoticon" + (i + 1), "drawable",
							mContext.getPackageName()));
		}
		return smileyToRes;
	}

	// 构建正则表达式
	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder();
		patternString.append('(');
		for (int i = 0; i < EMOTION_COUNT; i++) {
			patternString.append(Pattern.quote("[emotion" + i + "]"));
			patternString.append('|');
		}
		patternString.replace(patternString.length() - 1,
				patternString.length(), ")");

		return Pattern.compile(patternString.toString());
	}

	// 根据图片替换成文本
	public SpannableString encode(int emotionPosition) {
		Drawable drawable = mContext.getResources().getDrawable(
				mEmotionList.get(emotionPosition));
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		String appendText = "[emotion" + emotionPosition + "]";
		SpannableString spannable = new SpannableString(appendText);
		// 要让图片替代指定的文字就要用ImageSpan
		ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
		spannable.setSpan(span, 0, appendText.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	// 根据文本替换成图片
	public SpannableStringBuilder decode(String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Matcher matcher = mPattern.matcher(content);
		while (matcher.find()) {
			int resId = mEmotionToRes.get(matcher.group());
			builder.setSpan(new ImageSpan(mContext, resId), matcher.start(),
					matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
}