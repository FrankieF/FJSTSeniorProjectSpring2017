package groupSPV.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import groupSPV.model.BigCoin;

/** Currency conversion from BTC to USD/EUR/etc via blockchain.info API.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class ConversionRate {

	/** Latest conversion rate. */
	private static BigDecimal conversionRate = BigDecimal.ZERO;
	
	/** Blockchain.info API URL. */
	private static final String blockchainURL = "https://blockchain.info/ticker";
	
	/** Currency code for update(currencyCode) method. */
	public static final String USD = "USD", EURO = "EUR";
	
	/** Returns the stored conversion rate.
	 * @return BigDecimal. */
	public static BigDecimal getConversionRate() {
		return conversionRate;
	}
	
	/** Converts a BigCoin to currency.
	 * @param coin BigCoin object.
	 * @return BigDecimal. */
	public static BigDecimal convert(BigCoin coin) {
		return coin.getBitcoin().multiply(conversionRate);
	}
	
	/** Updates stored conversion rate with current conversion rate of given currency code.
	 * @param currencyCode Currency code to get conversion rate of. */
	public static void update(String currencyCode) {
		try {
			String jsonString = getJsonAsString();
			JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
			conversionRate = jsonObject.get(currencyCode).getAsJsonObject().get("last").getAsBigDecimal();
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage()); // Error with JSON fetch
		}
	}
	
	/** Updates stored conversion rate with current conversion rate of USD. */
	public static void update() {
		update(USD);
	}
	
	/** Handles HTTP request, returns JSON as String.
	 * @return JSON as string.
	 * @throws IOException Thrown if any errors occur with server or connection. */
	private static String getJsonAsString() throws IOException {
		StringBuilder sb = new StringBuilder();
		URL url = new URL(blockchainURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		return sb.toString();
	}
}