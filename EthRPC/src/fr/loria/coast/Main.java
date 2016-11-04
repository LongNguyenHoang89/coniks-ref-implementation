package fr.loria.coast;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {

	public static void main(String[] args) {	
		Security.addProvider(new BouncyCastleProvider());		
		EthereumUtils utils = new EthereumUtils("http://localhost:8545");
		//System.out.println(utils.publishSTR("1", "abc", "abc"));
		System.out.println(utils.getCoinBaseAddress("2"));
		
	}	
}

