package net.bussiness.module.setting;

import net.bussiness.activities.R;
import net.bussiness.global.IApplication;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;

public class MsgLoadingNumberActivity extends AbActivity {
	private Spinner s1;
	private Spinner s2;
	private Spinner s3;
	private ArrayAdapter<String> a1;
	private ArrayAdapter<String> a2;
	private String[] str1 = { "5", "10", "15", "20", "25", "30", "40", "50" };
	private String[] str2 = { "1", "3", "5", "10", "20", "30", "40", "50" };

	private IApplication iApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.setting_lodingnumber);
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("设置每页加载条目数");
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		iApp = (IApplication) getApplication();
		initView();
	}

	public void initView() {
		s1 = (Spinner) findViewById(R.id.Spinner1);
		s2 = (Spinner) findViewById(R.id.Spinner2);
		s3 = (Spinner) findViewById(R.id.Spinner3);
		a1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, str1);
		a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		a2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, str2);
		a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(a1);
		s2.setAdapter(a2);
		s3.setAdapter(a2);

		for (int i = 0; i < str1.length; i++)
			if (str1[i].equals(""
					+ iApp.mConfig.getmCMsgLoadingNumberDto()
							.getImChatMsgNumber()))
				s1.setSelection(i);

		for (int i = 0; i < str2.length; i++) {
			if (str2[i].equals(""
					+ iApp.mConfig.getmCMsgLoadingNumberDto().getYwspNumber()))
				s2.setSelection(i);
			if (str2[i].equals(""
					+ iApp.mConfig.getmCMsgLoadingNumberDto().getYwnrNumber()))
				s3.setSelection(i);
		}
	}

	@Override
	public void finish() {
		iApp.mConfig.getmCMsgLoadingNumberDto().setImChatMsgNumber(
				Integer.parseInt("" + s1.getSelectedItem()));
		iApp.mConfig.getmCMsgLoadingNumberDto().setYwspNumber(
				Integer.parseInt("" + s2.getSelectedItem()));
		iApp.mConfig.getmCMsgLoadingNumberDto().setYwnrNumber(
				Integer.parseInt("" + s3.getSelectedItem()));
		super.finish();
	}
}
