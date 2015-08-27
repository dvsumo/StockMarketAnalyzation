package gsonclass;

public class DailyData {

	double High;
	double Low;
	int Volume;
	String Symbol;
	double Adj_Close;
	double Close;
	String Date;
	double Open;

	public double getHigh() {
		return High;
	}

	public double getLow() {
		return Low;
	}

	public int getVolume() {
		return Volume;
	}

	public String getSymbol() {
		return Symbol;
	}

	public double getAdj_Close() {
		return Adj_Close;
	}

	public double getClose() {
		return Close;
	}

	public String getDate() {
		return Date;
	}

	public double getOpen() {
		return Open;
	}

	@Override
	public String toString() {
		return "double: " + High + "\nlow: " + Low + "\nvolume: " + Volume + "\nSymbol: " + Symbol + "\nadj_close: "
				+ Adj_Close + "\nclose: " + Close + "\nDate: " + Date + "\nopen: " + Open;
	}
}
