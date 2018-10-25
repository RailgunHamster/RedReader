package org.quantumbadger.redreader.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.quantumbadger.redreader.R;
import org.quantumbadger.redreader.adapters.CheckListAdapter;
import org.quantumbadger.redreader.common.PrefsUtility;
import org.quantumbadger.redreader.common.VPNCheckModel;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;

/**
 * VPN Check Activity
 * 主要为展示一个进度条和多个网址及状态。
 * 以及提供了重新运行和跳转到修改页面的按钮。
 */
public final class VPNCheckActivity extends BaseActivity {

	public static VPNCheckModel model;

	private Double processNum = 0.0;
	private TextView process;

	private ListView texts;
	private CheckListAdapter adapter;

	private Boolean update_lock = false;
	private Handler uiHandler = new Handler();

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
		// this.percentage = findViewById(R.id.percentage);

		// init texts
		this.texts = findViewById(R.id.check_list_view);
		adapter = new CheckListAdapter(this, R.layout.check_list_item, model);
		this.texts.setAdapter(adapter);
		// 暂时关闭点击
		this.texts.setClickable(false);

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
	 * 显示过后再做处理
	 */
	@Override
	protected void onStart() {
		super.onStart();
		check();
	}

	/**
	 * 进行check，并更新到list view
	 */
	private void check() {
		if (model == null) {
			return;
		}

		update_lock = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				int size = model.size();
				for (int i = 0; i < size; i++) {
					VPNCheckModel.VPNCheckItem item = model.get(i);

					try {
						InetAddress inetAddress = InetAddress.getByName(item.uri.toString());

						item.success = inetAddress.isReachable(100);
					} catch (IOException io) {
						// 无法连接
						item.success = false;
					}
					processNum = ((i + 1) * 100) / (double) size;
					// ui update
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							updateProcess();
							adapter.notifyDataSetChanged();
						}
					});
				}
				update_lock = false;
			}
		}).start();
	}

	/**
	 * 重新计算并显示
	 */
	public synchronized void refresh() {
		if (update_lock) {
			return;
		}

		processNum = 0.0;
		for (VPNCheckModel.VPNCheckItem item : model) {
			item.success = null;
		}
		// ui
		updateProcess();
		adapter.notifyDataSetChanged();
		// run again
		check();
	}

	private void updateProcess() {
		this.process.setText(
				String.format(Locale.getDefault(), "%.0f", processNum)
		);
	}
}
