package org.quantumbadger.redreader.common;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class VPNCheckModelTest {

	private List<String> tests;
	private List<List<VPNCheckModel.VPNCheckItem>> expecteds;

	@Before
	public void setUp() throws Exception {
		System.out.println("setup");

		tests = new ArrayList<>();
		expecteds = new ArrayList<>();
		List<VPNCheckModel.VPNCheckItem> newList;
		// 1
		tests.add("bilibili www.bilibili.com\nbing www.bing.com\n");
		newList = new ArrayList<>();
		newList.add(new VPNCheckModel.VPNCheckItem(
				"bilibili", new URI("www.bilibili.com")
		));
		newList.add(new VPNCheckModel.VPNCheckItem(
				"bing", new URI("www.bing.com")
		));
		expecteds.add(newList);
		// 2
		tests.add("bilibili www.bilibili.com");
		newList = new ArrayList<>();
		newList.add(new VPNCheckModel.VPNCheckItem(
				"bilibili", new URI("www.bilibili.com")
		));
		expecteds.add(newList);
		// 3
		tests.add("   bilibili    www.bilibili.com   ");
		expecteds.add(newList);
		// 4
		tests.add("bilibili\n");
		expecteds.add(null);

		System.out.println("already");
	}

	@Test
	public void stringToCheckItems() {
		assertNotNull(tests);
		assertNotNull(expecteds);

		assertEquals(expecteds.size(), tests.size());

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < expecteds.size(); i++) {
			String str = tests.get(i);
			List<VPNCheckModel.VPNCheckItem> items = expecteds.get(i);

			sb.delete(0, sb.length());

			if (items != null) {
				for (int j = 0; j < items.size(); j++) {
					sb.append("\t");
					sb.append(items.get(j));
					sb.append("\n");
				}
			} else {
				sb.append("\tnull\n");
			}

			System.out.println(String.format(
					Locale.getDefault(),
					"stringToCheckItem(%s):\n%s",
					str.replaceAll("\n", "\\\\n"),
					sb.toString()
			));

			List<VPNCheckModel.VPNCheckItem> list =
					VPNCheckModel.stringToCheckItems(str);

			if (items == null) {
				assertNull(list);
				continue;
			}

			assertNotNull(list);

			assertEquals(items.size(), list.size());

			for (int j = 0; j < list.size(); j++) {
				assertEquals(
						items.get(j),
						list.get(j)
				);
			}
		}
	}
}
