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
	private static void update(String currencyCode) {
		try {
			String jsonString = getJsonAsString();
			JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
			conversionRate = jsonObject.get(currencyCode).getAsJsonObject().get("last").getAsBigDecimal();
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage()); // Error with JSON fetch
		}
	}
	
	/** Updates stored conversion rate with current conversion rate of SupportedCurrency.
	 * @param currency SupportedCurrency to get conversion rate of. */
	public static void update(SupportedCurrency currency) {
		update(getCurrencyCode(currency));
	}
	
	/** Updates stored conversion rate with current conversion rate of USD. */
	public static void update() {
		update(SupportedCurrency.America);
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
	
	/** Countries which are supported via the current blockchain.info API. */
	public enum SupportedCurrency {
		/** Represents the United States Dollar. */
		America,
		/** Represents the Japanese Yen. */
		Japan,
		/** Represents the Chinese Renminbi. */
		China,
		/** Represents the Singaporean Dollar. */
		Singapore,
		/** Represents the Hong Kong Dollar. */
		HongKong,
		/** Represents the Canadian Dollar. */
		Canada,
		/** Represents the New Zealand Dollar. */
		NewZealand,
		/** Represents the Australian Dollar.  */
		Australia,
		/** Represents the Chilean Peso. */
		Chile,
		/** Represents the Pound Sterling. */
		UnitedKingdom,
		/** Represents the Danish Krone. */
		Denmark,
		/** Represents the Swedish Krona. */
		Sweden,
		/** Represents the Icelandic Króna. */
		Iceland,
		/** Represents the Swiss Franc. */
		Switzerland,
		/** Represents the Brazilian Real. */
		Brazil,
		/** Represents the Euro. */
		Europe,
		/** Represents the Russian Ruble. */
		Russia,
		/** Represents the Polish Zloty. */
		Poland,
		/** Represents the Thai Baht. */
		Thailand,
		/** Represents the Korean Republic Won. */
		SouthKorea,
		/** Represents the New Taiwan Dollar. */
		Taiwan,
		/** Represents the Indian Rupee. */
		India
	}
	
	/** Returns the currency code of the given SupportedCurrency.
	 * @param currency SupportedCurrency to use.
	 * @return Currency code as String. */
	public static String getCurrencyCode(SupportedCurrency currency) {
		switch(currency) {
		case America:
			return "USD";
		case Japan:
			return "JPY";
		case China:
			return "CNY";
		case Singapore:
			return "SGD";
		case HongKong:
			return "HKD";
		case Canada:
			return "CAD";
		case NewZealand:
			return "NZD";
		case Australia:
			return "AUD";
		case Chile:
			return "CLP";
		case UnitedKingdom:
			return "GBP";
		case Denmark:
			return "DKK";
		case Sweden:
			return "SEK";
		case Iceland:
			return "ISK";
		case Switzerland:
			return "CHF";
		case Brazil:
			return "BRL";
		case Europe:
			return "EUR";
		case Russia:
			return "RUB";
		case Poland:
			return "PLN";
		case Thailand:
			return "THB";
		case SouthKorea:
			return "KRW";
		case Taiwan:
			return "TWD";
		case India:
			return "INR";
		default:
			return null;
		}
	}
}