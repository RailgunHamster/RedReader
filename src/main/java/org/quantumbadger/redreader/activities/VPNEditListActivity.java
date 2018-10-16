package org.quantumbadger.redreader.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;

import org.quantumbadger.redreader.R;
import org.quantumbadger.redreader.common.PrefsUtility;

/**
 * VPN Edit List Activity
 * 管理vpn list的修改和重置
 */
public final class VPNEditListActivity extends BaseActivity {

	private EditText edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		PrefsUtility.applyTheme(this);

		super.onCreate(savedInstanceState);

		getWindow().setBackgroundDrawable(new ColorDrawable(obtainStyledAttributes(new int[] {R.attr.rrListBackgroundCol}).getColor(0,0)));

		final Intent intent = getIntent();

		if (intent == null) {
			throw new RuntimeException("Nothing to show!");
		}

		this.edit = new EditText(this);

		setTitle(R.string.pref_appearance_vpn_edit_check_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// 添加重置按钮，调用reset。
		final SubMenu refresh = menu.addSubMenu(R.string.options_refresh);
		final MenuItem item = refresh.getItem();
		item.setIcon(R.drawable.ic_refresh_dark);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				VPNEditListActivity.this.reset();
				return true;
			}
		});

		return true;
	}

	/**
	 * 重置vpn list
	 */
	public void reset() {
		finish();
	}
}
