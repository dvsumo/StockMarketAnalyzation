package gsonclass;

import java.util.Collections;
import java.util.List;

public class HistoryResults {

	List<DailyData> quote;

	public List<DailyData> getQuote() {
		return quote;
	}

	// this is needed because the array is most recent first
	public void reverseArray() {
		Collections.reverse(quote);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (DailyData daily : quote) {
			builder.append(daily);
			builder.append("\n\n");
		}

		return "quote: \n\n" + builder.toString();
	}
}
