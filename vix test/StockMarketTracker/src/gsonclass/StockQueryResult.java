package gsonclass;

public class StockQueryResult {
	StockQuery query;

	public StockQuery getQuery() {
		return query;
	}

	@Override
	public String toString() {
		return "Query: \n" + query;
	}
}
