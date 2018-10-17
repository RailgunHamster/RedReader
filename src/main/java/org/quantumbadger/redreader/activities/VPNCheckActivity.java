package org.quantumbadger.redreader.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.quantumbadger.redreader.R;
import org.quantumbadger.redreader.common.General;
import org.quantumbadger.redreader.common.PrefsUtility;

/**
 * VPN Check Activity
 * 主要为展示一个进度条和多个网址及状态。
 * 以及提供了重新运行和跳转到修改页面的按钮。
 */
public final class VPNCheckActivity extends BaseActivity {
	/**
	 * Model层
	 * 控制底层数据读写
	 */
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

	public static VPNCheckModel model;

	private TextView process;
	private TextView percentage;

	private ListView texts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		PrefsUtility.applyTheme(this);

		super.onCreate(savedInstanceState);

		this.getWindow().setBackgroundDrawable(new ColorDrawable(obtainStyledAttributes(new int[] {R.attr.rrListBackgroundCol}).getColor(0,0)));

		final Intent intent = getIntent();

		if (intent == null) {
			throw new RuntimeException("Nothing to show!");
		}

		if (VPNCheckActivity.model == null) {
			VPNCheckActivity.model = new VPNCheckModel();
		}

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.process, null);
		RelativeLayout.LayoutParams layoutParams =
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT
				);
		this.addContentView(view, layoutParams);

		// init process
		this.process = findViewById(R.id.process);

		// init percentage
		this.percentage = findViewById(R.id.percentage);

		// init texts
		this.texts = new ListView(this);

		setTitle(R.string.pref_appearance_vpn_check);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// 添加重新运行按钮，调用refresh重新运行并显示。
		final SubMenu refresh = menu.addSubMenu(R.string.options_refresh);
		final MenuItem item = refresh.getItem();
		item.setIcon(R.drawable.ic_refresh_dark);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				VPNCheckActivity.this.refresh();
				return true;
			}
		});

		// 添加跳转到修改页面选项
		menu.add(this.getString(R.string.options_edit)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				final Intent intent = new Intent(VPNCheckActivity.this, VPNEditListActivity.class);
				VPNCheckActivity.this.startActivityForResult(intent, 1);
				return true;
			}
		});

		return true;
	}

	/**
	 * 重新计算并显示
	 */
	public void refresh() {
		General.recreateActivityNoAnimation(this);
	}

	private void updateProcess() {
		;
	}
}
