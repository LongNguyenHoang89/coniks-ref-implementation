package fr.loria.coast;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.jcajce.provider.digest.Keccak.DigestKeccak;

public class Crypto {
	public byte[] sha3(String input) {
		DigestKeccak sha3 = new DigestKeccak(256);
		sha3.update(input.getBytes(StandardCharsets.US_ASCII));
		return sha3.digest();
	}
}
