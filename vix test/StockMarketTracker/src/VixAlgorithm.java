import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gsonclass.DailyData;

public class VixAlgorithm {

	private final static double FEE = 7.95;
	private final static double BUY_LIMIT = 13;
	private final static double SELL_THRESHOLD = 15;
	private int buys;
	private int sells;
	private List<DailyData> dailyDatas;
	private double funds;
	private int currentHoldings;
	private double lastSellPrice = 0;

	public VixAlgorithm(List<DailyData> dailyDatas) {
		this.dailyDatas = new ArrayList<DailyData>(dailyDatas);
		Collections.reverse(dailyDatas);
		this.buys = 0;
		this.sells = 0;
		this.funds = 1000;
		this.currentHoldings = 0;
	}

	public VixAlgorithm(List<DailyData> dailyDatas, double funds, int currentHoldings) {
		this.dailyDatas = new ArrayList<DailyData>(dailyDatas);
		Collections.reverse(dailyDatas);
		this.buys = 0;
		this.sells = 0;
		this.funds = funds;
		this.currentHoldings = currentHoldings;
		System.out.println("Starting new cycle with " + currentHoldings + " options and funds " + funds);
	}

	// assumes day trading license for now
	public void testBuyLT13SellAbove() {
		for (DailyData data : dailyDatas) {
			double worstCaseBuy = getWorstBuy(data.getOpen(), data.getClose());
			double worstCaseSell = getWorstSell(data.getOpen(), data.getClose());
			if (worstCaseBuy <= BUY_LIMIT) {
				if (funds > FEE + worstCaseBuy) {
					int toBuy = (int) (funds / worstCaseBuy);
					if (toBuy <= 16) {
						return; // we wouldn't make any money because of fee
					}
					// guess we will buy some stuff!
					currentHoldings += toBuy;
					funds -= toBuy * worstCaseBuy - FEE;
					buys++;
				}
			} else if (worstCaseSell >= SELL_THRESHOLD) {
				if (currentHoldings > 0) {
					funds += currentHoldings * worstCaseSell - FEE;
					funds = Math.floor(funds * 100) / 100;
					currentHoldings = 0;
					sells++;
					lastSellPrice = worstCaseSell;
				}
			}
		}
	}

	public double getResults() {
		funds = Math.floor(funds * 100) / 100;
		System.out.println("We made " + buys + " buys and " + sells + " sells resulting in portfolio of " + funds
				+ " and " + currentHoldings + " options remaining");
		return funds;
	}

	public void getFinalResults() {
		if (lastSellPrice > 0) {
			funds += currentHoldings * lastSellPrice;
			currentHoldings = 0;
		}
		funds = Math.floor(funds * 100) / 100;
		System.out.println("We ended with " + funds + " funds and " + currentHoldings + " options");
	}

	public int getCurrentHoldings() {
		return this.currentHoldings;
	}

	private double getWorstSell(double open, double close) {
		double average = (open + close) / 2;

		if (open < BUY_LIMIT) {
			if (average < BUY_LIMIT) {
				return close;
			} else {
				if (average < close) {
					return average;
				} else {
					return close;
				}
			}
		} else {
			if (average < BUY_LIMIT) {
				return open;
			} else {
				if (open < average) {
					return open;
				} else if (close < BUY_LIMIT) {
					return average;
				} else {
					return close;
				}
			}
		}
	}

	// this is a worst case buying opportunity function
	private double getWorstBuy(double open, double close) {
		double average = (open + close) / 2;

		if (open > BUY_LIMIT) {
			if (close > BUY_LIMIT) {
				return open; // doesn't matter, we aren't buying
			} else {
				if (average < close) {
					return close;
				} else if (average > BUY_LIMIT) {
					return close;
				} else {
					return average;
				}
			}
		} else {
			if (close < BUY_LIMIT) {
				if (average < close) {
					return close;
				} else {
					return open;
				}
			} else if (average > open) {
				return average;
			} else {
				return close;
			}
		}
	}
}
