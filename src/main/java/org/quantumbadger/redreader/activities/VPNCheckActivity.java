package org.quantumbadger.redreader.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.quantumbadger.redreader.R;
import org.quantumbadger.redreader.common.PrefsUtility;

public final class VPNCheckActivity extends BaseActivity {
	public final class VPNCheckModel {
		void resetCheckList() {
			;
		}

		void editCheckList(String newList) {
			;
		}

		String getCheckItem(Integer index) {
			return "";
		}
	}

	private VPNCheckModel model = new VPNCheckModel();

	private TextView process = new TextView(this);
	private TextView percentage = new TextView(this);

	private ListView texts = new ListView(this);

	private Button ret = new Button(this);
	private Button edit = new Button(this);
	private Button retry = new Button(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.pref_appearance_vpn_check);

		ret.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateProcess() {
		;
	}
}
