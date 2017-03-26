/**
 * 
 */
package groupSPV;

import groupSPV.controller.WalletController;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.*;
import org.bitcoinj.wallet.Wallet.BalanceType;
import org.bitcoinj.wallet.listeners.AbstractWalletEventListener;

import groupSPV.controller.WalletController;

import org.bitcoinj.store.*;
import org.bitcoinj.net.discovery.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Frankie Fasola
 *
 */
public class Test {
	static boolean testBitcoins = false;
	
	
	public static void main( String[] args ) throws BlockStoreException, AddressFormatException,
	InterruptedException, ExecutionException, InsufficientMoneyException {
		
		if(testBitcoins) {
			NetworkParameters params = TestNet3Params.get();		
			//WalletController wc = new WalletController(params);
			//String privateKey = "cT6RjKhArCVqZW7kEf1vN7vZYkWDtY5VNQ8Kv2VnoQC78AiSpuxU";
			//String key = "cPvVdgyEwePBQa2rUWL9qUQVpgM9qRjKDj4VrhK3ukrMmCDgXrwV";
			//wc.addExistingKeys(params, privateKey, key);
			System.out.println("Added Keys");
			//List<ECKey> keys = wc.getKeys();
			//for (ECKey keym : keys)
				//System.out.println(keym.toString());
			//wc.addNewKey(params);
			//wc.getWallet().getImportedKeys();
	//		DumpedPrivateKey key = DumpedPrivateKey.fromBase58(params, "cT6RjKhArCVqZW7kEf1vN7vZYkWDtY5VNQ8Kv2VnoQC78AiSpuxU");
	//		wallet.importKey(key.getKey());
//			BlockChain chain = new BlockChain(params, wc.getWallet(),
//			new MemoryBlockStore(params));
//			PeerGroup peerGroup = new PeerGroup(params, chain);
//			peerGroup.addPeerDiscovery(new DnsDiscovery(params));
//			peerGroup.addWallet(wc.getWallet());
//			peerGroup.start();
//			peerGroup.downloadBlockChain();			
			//System.out.println("Wallet balance: " + wc.getWallet().getBalance());
			//String address = "mrJMoMskRKXm3YxyR6koBbggqb2KcfTsRY";
			//wc.sendBitcoin(params, peerGroup, address, ".5");
			//System.out.println("Wallet Balance: " + wc.getWallet().getBalance());
		}
	}

	/*    NetworkParameters params = TestNet3Params.get();
	    Wallet wallet = new Wallet(params);
	    ECKey key = new ECKey();
	    System.out.println("Public address: " +
	        key.toAddress(params).toString());
	    System.out.println("Private key: " +
	        key.getPrivateKeyEncoded(params).toString());
	    wallet.addKey(key);
	    File file = new File("my-blockchain");
	    SPVBlockStore store = new SPVBlockStore(params, file);
	    BlockChain chain = new BlockChain(params, wallet, store);
	    PeerGroup peerGroup = new PeerGroup(params, chain);
	    peerGroup.addPeerDiscovery(new DnsDiscovery(params));
	    peerGroup.addWallet(wallet);
	    peerGroup.start();
	    peerGroup.downloadBlockChain();
	    wallet.addChangeEventListener(new AbstractWalletEventListener()
	        {
	            public void onCoinsReceived(Wallet wallet,
	                  Transaction tx, BigInteger prevBalance,
	                  BigInteger newBalance)
	            {
	                System.out.println("Hello Money! Balance: "
	                    + newBalance + " satoshis");
	            }
	        });
	     while(true){}
	} */

	//Address destinationAddress = Address.fromBase58(params, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");

}
