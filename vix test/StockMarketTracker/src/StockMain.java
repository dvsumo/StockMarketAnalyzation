import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Utils.Utils;
import gsonclass.StockQueryResult;

public class StockMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Gson gson = new GsonBuilder().create();
		StockQueryResult result = null;
		List<String> queries = new ArrayList<String>();
		queries.add(Utils.buildQuery("2013-01-01", "2013-12-31"));
		queries.add(Utils.buildQuery("2014-01-01", "2014-12-31"));
		queries.add(Utils.buildQuery("2015-01-01", "2015-12-31"));

		double funds = 10000;
		int currentHoldings = 0;
		VixAlgorithm vix = null;

		for (String query : queries) {
			try {
				// String fullUrlStr = baseUrl + query +
				// "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
				URL url = new URL(query);
				InputStream is = url.openStream();

				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				is.close();

				result = gson.fromJson(response.toString(), StockQueryResult.class);

//				System.out.println("\nSending 'GET' request to URL: " + url);
				// System.out.println("\nGot " + result);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (result != null) // do logic here
			{
				vix = new VixAlgorithm(result.getQuery().getResults().getQuote(), funds, currentHoldings);
//				vix.testBuyLT13SellAbove();
				vix.testAbsWorstCase();
				funds = vix.getResults();
				currentHoldings = vix.getCurrentHoldings();
			}
		}
		if (vix != null) {
			vix.getFinalResults();
		}
	}

}
