package org.araport.image.network.download;

import java.text.NumberFormat;

public class DownLoadStats {

	public volatile Counter ERROR_COUNT = new Counter();
	public volatile static Counter SUCCESS_COUNT = new Counter();
	public volatile static Counter TOTAL_COUNT = new Counter();

	private static class DownLoadStatsHolder {
		public static final DownLoadStats INSTANCE = new DownLoadStats();
	}

	public static DownLoadStats getInstance() {
		return DownLoadStatsHolder.INSTANCE;
	}

	public static String getCurrentStatistics() {
		StringBuilder result = new StringBuilder("Current Partition Statistics: " + "\n");

		result.append("Total Files downloaded " + SUCCESS_COUNT.getValue() + " of Total Files "
				+ TOTAL_COUNT.getValue() + " Completed: "
				+ getPercent(SUCCESS_COUNT.getValue(), TOTAL_COUNT.getValue()) + "\n");

		return result.toString();

	}

	public static String getPercent(int count, int totalCount) {
		double percent = 0;
		String result = "";

		if (totalCount != 0) {
			percent = (new Double(count) / totalCount);
		}

		NumberFormat percentFormatter = NumberFormat.getPercentInstance();
		result = percentFormatter.format(percent);
		return result;

	}
}
