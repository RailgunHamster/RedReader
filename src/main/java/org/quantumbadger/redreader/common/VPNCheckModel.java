package org.quantumbadger.redreader.common;

import android.support.annotation.NonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Model层
 * 控制底层数据读写
 */
public final class VPNCheckModel implements Iterable<VPNCheckModel.VPNCheckItem> {
	public final class VPNCheckItem {
		public String name;
		public URI uri;
		public Boolean success;

		VPNCheckItem(String name, URI uri, Boolean success) {
			this.name = name;
			this.uri = uri;
			this.success = success;
		}
	}

	private List<VPNCheckItem> objects = new ArrayList<>();

	public void add(String name, URI uri) {
		this.add(name, uri, null);
	}

	public void add(String name, URI uri, Boolean success) {
		this.objects.add(new VPNCheckItem(name, uri, success));
	}

	public VPNCheckItem get(int i) {
		return objects.get(i);
	}

	public int size() {
		return objects.size();
	}

	public VPNCheckModel() {
		try {
			add("baidu", new URI("www.baidu.com"));
			add("google", new URI("www.google.com"));
			add("reddit", new URI("www.reddit.com"));
		} catch (URISyntaxException syntax) {
			syntax.printStackTrace();
		}
	}

	@NonNull
	@Override
	public Iterator<VPNCheckItem> iterator() {
		return new Iterator<VPNCheckItem>() {
			int size = size();
			int now = 0;

			@Override
			public boolean hasNext() {
				return now < size;
			}

			@Override
			public VPNCheckItem next() {
				return get(now++);
			}
		};
	}

	public void resetCheckList() {
		;
	}

	public void editCheckList(String newList) {
		;
	}
}
