package Utils;

public class Utils {

	public static String buildQuery(String startDate, String endDate) {
		String baseUrl = "https://query.yahooapis.com/v1/public/yql?q=";
		String query = ("select * from yahoo.finance.historicaldata where symbol = \"^VIX\" and startDate = '"
				+ startDate + "' and endDate = '" + endDate + "'")
						// This is non optimal, but this was teh quickest way to
						// adhere
						// it to yql standards
						.replace(" ", "%20").replace("\"", "%22").replace("'", "%27");
		String fullUrlStr = baseUrl + query + "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

		return fullUrlStr;
	}
}
