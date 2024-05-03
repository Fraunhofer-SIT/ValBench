package de.fraunhofer.sit.sse.valbench.modellingtests;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class CryptoAPITests {

	@ValueComputationTestCase
	public static String test2() throws Exception {

		String payload = "0297b5eb43e3b81f9c737b353c3ade45";
		Cipher aesCipher = Cipher.getInstance("AES");
		byte[] b = "ABCDEFGHABCDEFGH".getBytes("UTF-8");
		SecretKeySpec wr = new SecretKeySpec(b, "AES");
		aesCipher.init(Cipher.ENCRYPT_MODE, wr);
		byte[] output = new byte[48];
		int x = aesCipher.doFinal(payload.getBytes(), 0, payload.getBytes().length, output);
		aesCipher.init(Cipher.DECRYPT_MODE, wr);
		String decryptedPayload = new String(aesCipher.doFinal(output), "UTF-8");
		return x + "_" + decryptedPayload;
	}

}
