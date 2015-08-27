package gsonclass;

public class StockQuery {
	String created;
	int count;
	String lang;
	HistoryResults results;

	public String getCreated() {
		return created;
	}

	public int getCount() {
		return count;
	}

	public String getLang() {
		return lang;
	}

	public HistoryResults getResults() {
		return results;
	}

	@Override
	public String toString() {
		return "created at: " + created + "\ncount: " + count + "\nlang: " + lang + "\nresults: " + results;
	}
}
