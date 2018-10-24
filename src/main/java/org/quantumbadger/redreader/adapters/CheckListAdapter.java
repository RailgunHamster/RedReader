package org.quantumbadger.redreader.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.quantumbadger.redreader.R;
import org.quantumbadger.redreader.common.VPNCheckModel;

public class CheckListAdapter extends BaseAdapter {
	public class ViewHolder {
		public TextView name;
		public TextView url;
		public ImageView image;
	}

	private final LayoutInflater inflater;
	private final int resourceId;
	private final VPNCheckModel model;

	public CheckListAdapter(Context context, int resourceId, VPNCheckModel model) {
		this.resourceId = resourceId;
		this.inflater = LayoutInflater.from(context);
		this.model = model;
	}

	@Override
	public VPNCheckModel.VPNCheckItem getItem(int position) {
		return model.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return model.size();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		ViewHolder viewHolder;

		// 缓存
		if (convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.name = convertView.findViewById(R.id.check_list_item_name);
			viewHolder.url = convertView.findViewById(R.id.check_list_item_uri);
			viewHolder.image = convertView.findViewById(R.id.check_list_item_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		VPNCheckModel.VPNCheckItem item = model.get(position);

		viewHolder.name.setText(item.name);
		viewHolder.url.setText(item.uri.toString());
		if (item.success == null) {
			viewHolder.image.setImageResource(R.drawable.ic_refresh_light);
		} else if (item.success) {
			viewHolder.image.setImageResource(R.drawable.ic_action_tick_light);
		} else {
			viewHolder.image.setImageResource(R.drawable.ic_action_cross_light);
		}

		return convertView;
	}
}
