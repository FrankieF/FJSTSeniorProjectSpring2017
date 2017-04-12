package groupSPV.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;

/** Bitcoin Coin implementation with BigDecimal.
 * 
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class BigCoin implements Comparable<BigCoin>, Comparator<BigCoin> {

	/** Represents total amount of Satoshis in the current BigCoin. */
	private BigDecimal satoshi;
	
	/** Ratio to convert Satoshis to Bitcoins. */
	private static final BigDecimal satoshiToBTC = new BigDecimal("100000000");
	
	/** Standard Bitcoin currency code. */
	private static final String bitcoinCode = "BTC";
	
	/** Zero representation in BigCoin. */
	public static final BigCoin ZERO = new BigCoin(BigDecimal.ZERO);
	
	/** Creates a BigCoin with a Coin. Coin object is not altered.
	 * @param coin Coin to create BigCoin with. */
	public BigCoin(Coin coin) {
		satoshi = new BigDecimal(coin.getValue());
	}
	
	/** Creates a BigCoin with a Long. Long represents number of Satoshis.
	 * @param longValue Long value */
	public BigCoin(Long longValue) {
		satoshi = new BigDecimal(longValue);
	}
	
	/** Creates a BigCoin with a BigDecimal. BigDecimal represents number of Satoshis.
	 * @param bigDecimal BigDecimal object. */
	private BigCoin(BigDecimal bigDecimal) {
		satoshi = bigDecimal.add(BigDecimal.ZERO);
	}
	
	/** Get the total number of Satoshis.
	 * @return BigDecimal */
	public BigDecimal getSatoshi() {
		return satoshi;
	}
	
	/** Get the total number of Bitcoins.
	 * @return BigDecimal */
	public BigDecimal getBitcoin() {
		return satoshi.divide(satoshiToBTC);
	}
	
	/** Add a BigCoin, returns new BigCoin object.
	 * @param coin BigCoin to add.
	 * @return new BigCoin. */
	public BigCoin add(BigCoin coin) {
		return new BigCoin(satoshi.add(coin.getSatoshi()));
	}
	
	/** Subtract a BigCoin, returns new BigCoin object.
	 * @param coin BigCoin to subtract.
	 * @return new BigCoin. */
	public BigCoin subtract(BigCoin coin) {
		return new BigCoin(satoshi.subtract(coin.getSatoshi()));
	}
	
	/** Multiply a BigCoin, returns new BigCoin object.
	 * @param coin BigCoin to multiply.
	 * @return new BigCoin. */
	public BigCoin multiply(BigCoin coin) {
		return new BigCoin(satoshi.multiply(coin.getSatoshi()));
	}
	
	/** Divide a BigCoin, returns new BigCoin object.
	 * @param coin BigCoin to divide.
	 * @return new BigCoin. */
	public BigCoin divide(BigCoin coin) {
		return new BigCoin(satoshi.divide(coin.getSatoshi()));
	}

	@Override
	public int compareTo(BigCoin o) {
		return satoshi.compareTo(o.getSatoshi());
	}

	@Override
	public int compare(BigCoin o1, BigCoin o2) {
		return o1.compareTo(o2);
	}
	
	@Override
	public boolean equals(Object obj) {
		BigCoin coin = (BigCoin) obj;
		return compareTo(coin) == 0;
	}
	
	@Override
	public String toString() {
		return getBitcoin() + " " + bitcoinCode;
	}
	
	/** Get the total BigCoin of all transaction outputs in a list.
	 * @param transactions Transactions to total.
	 * @return BigCoin total. */
	public static BigCoin totalTransactions(List<Transaction> transactions) {
		BigCoin total = BigCoin.ZERO;
		for(Transaction transaction : transactions)
			total = total.add(new BigCoin(transaction.getOutputSum()));
		return total;
	}
}