import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gsonclass.DailyData;

public class VixAlgorithm {
	private enum Action {
		BUY, SELL
	};

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
		Collections.reverse(this.dailyDatas);
		this.buys = 0;
		this.sells = 0;
		this.funds = 1000;
		this.currentHoldings = 0;
	}

	public VixAlgorithm(List<DailyData> dailyDatas, double funds, int currentHoldings) {
		this.dailyDatas = new ArrayList<DailyData>(dailyDatas);
		Collections.reverse(this.dailyDatas);
		this.buys = 0;
		this.sells = 0;
		this.funds = funds;
		this.currentHoldings = currentHoldings;
		System.out.println("Starting new cycle with " + currentHoldings + " options and funds " + funds);
	}
	
	public void testAbsWorstCase()
	{
		for(DailyData data : dailyDatas)
		{
			if((data.getOpen() < BUY_LIMIT || data.getClose() < BUY_LIMIT))
			{
				int toBuy = (int) ((funds - 2*FEE) / BUY_LIMIT);
				if (toBuy <= (2*FEE)/(SELL_THRESHOLD - BUY_LIMIT)) {
					// we wouldn't make any money because of fee
				}
				else
				{
					// guess we will buy some stuff!
					currentHoldings += toBuy;
					funds -= toBuy * BUY_LIMIT - FEE;
					funds = Math.floor(funds * 100) / 100;
					buys++;
					printIterationResult(data.getOpen(), data.getClose(), BUY_LIMIT, toBuy, Action.BUY, data.getDate());
				}
			}
			if((data.getOpen() > SELL_THRESHOLD || data.getClose() > SELL_THRESHOLD) && currentHoldings > 0)
			{
				funds += currentHoldings * SELL_THRESHOLD - FEE;
				funds = Math.floor(funds * 100) / 100;
				printIterationResult(data.getOpen(), data.getClose(), SELL_THRESHOLD, currentHoldings, Action.SELL, data.getDate());
				currentHoldings = 0;
				sells++;
				lastSellPrice = SELL_THRESHOLD;
			}
		}
	}

	// assumes day trading license for now
	public void testBuyLT13SellAbove() {
		for (DailyData data : dailyDatas) {
			double worstCaseBuy = getWorstBuy(data.getOpen(), data.getClose());
			double worstCaseSell = getWorstSell(data.getOpen(), data.getClose());
			if (worstCaseBuy <= BUY_LIMIT && funds > (FEE*2 + worstCaseBuy)) {
				int toBuy = (int) ((funds - 2*FEE) / worstCaseBuy);
				if (toBuy <= (2*FEE)/(SELL_THRESHOLD - BUY_LIMIT)) {
					return; // we wouldn't make any money because of fee
				}
				// guess we will buy some stuff!
				currentHoldings += toBuy;
				funds -= toBuy * worstCaseBuy - FEE;
				funds = Math.floor(funds * 100) / 100;
				buys++;
				printIterationResult(data.getOpen(), data.getClose(), worstCaseBuy, toBuy, Action.BUY, data.getDate());
			} else if (worstCaseSell >= SELL_THRESHOLD && currentHoldings > 0) {
				funds += currentHoldings * worstCaseSell - FEE;
				funds = Math.floor(funds * 100) / 100;
				printIterationResult(data.getOpen(), data.getClose(), worstCaseBuy, currentHoldings, Action.SELL, data.getDate());
				currentHoldings = 0;
				sells++;
				lastSellPrice = worstCaseSell;
			}
		}
	}
	
	private void printIterationResult(double open, double close, double actionPrice, int shares, Action action, String date)
	{
		switch(action)
		{
			case BUY:
				System.out.println("Bought " + shares + " shares for " + actionPrice + " price with funds: " + funds);
				System.out.println("\tDate: " + date + "\n\tOpen: " + open + "\n\tClose: " + close);
				break;
			case SELL:
				System.out.println("Sold " + shares + " shares for " + actionPrice + " price for funds: " + funds);
				System.out.println("\tDate: " + date + "\n\tOpen: " + open + "\n\tClose: " + close);
				break;
		}
	}

	public double getResults() {
		funds = Math.floor(funds * 100) / 100;
		System.out.println("We made " + buys + " buys and " + sells + " sells resulting in portfolio of " + funds
				+ " and " + currentHoldings + " options remaining\n");
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
