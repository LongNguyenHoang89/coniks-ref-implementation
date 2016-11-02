package fr.loria.coast;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Hex;

import com.google.gson.Gson;

public class EthereumUtils {

	HttpClient _httpClient;
	String _rpcServer;
	Gson _gson = new Gson();
	String _baseAddress = "0x3bfdcbd42cfefebf051bca1fb7b38d40d36b1f9e";
	String _trusternityContractAddress = "0xf57a32337b8Fb8b92741A53064c6a6b06954e84E";
	Crypto _cryptoUtil;

	public EthereumUtils(String URL) {
		_httpClient = HttpClientBuilder.create().build();
		_rpcServer = URL;
		_cryptoUtil = new Crypto();
	}

	/**
	 * Send JSON request to RPC server
	 * 
	 * @param command
	 * @return
	 */
	public String SendRequest(String command) {		
		System.out.println(command);
		
		try {
			HttpPost request = new HttpPost(_rpcServer);
			StringEntity params = new StringEntity(command);
			request.setEntity(params);

			HttpResponse response = _httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");

			return responseString;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	public String getCoinBaseAddress(String id) {
		RpcRequest request = new RpcRequest();
		request.method = "eth_coinbase";
		request.id = id;
		String response = SendRequest(_gson.toJson(request));
		return response;
	}

	/**
	 * Send STR to Trusternity contract
	 * 
	 * @param domain
	 * @param STR
	 * @see https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI
	 * @return
	 */
	public String publishSTR(String id, String domain, String STR) {
		ByteBuffer buffer = ByteBuffer.allocate(196);
		// append methodId - first 4 bytes
		// "0xdf2b7b83"
		buffer.put(getMethodId("Publish(string,string)"));
		// its will be 4-32-32-data(32-32)-data(32-32)
		// append location of the data part of the first paramenter
		ByteBuffer loc1 = ByteBuffer.allocate(32).putInt(28, 64);
		buffer.put(loc1);
		// append location of the data part of the second paramenter
		ByteBuffer loc2 = ByteBuffer.allocate(32).putInt(28, 128);
		buffer.put(loc2);
		// append the data part of the first argument
		// starts with the length of the bytes of the utf-8 encoded string
		byte[] domainBytes = domain.getBytes(StandardCharsets.UTF_8);
		ByteBuffer lengthData1 = ByteBuffer.allocate(32).putInt(28, domainBytes.length);
		buffer.put(lengthData1);
		// continue with the utf-8 string		
		ByteBuffer data1 = ByteBuffer.allocate(32).put(domainBytes);
		buffer.put(data1.array());
		// append the data part of the second argument
		// starts with the length of the bytes of the utf-8 encoded string
		byte[] strBytes = STR.getBytes(StandardCharsets.UTF_8);
		ByteBuffer lengthData2 = ByteBuffer.allocate(32).putInt(28, strBytes.length);
		buffer.put(lengthData2);
		// continue with the utf-8 string		
		ByteBuffer data2 = ByteBuffer.allocate(32).put(strBytes);		
		buffer.put(data2.array());

		RpcRequest request = new RpcRequest();
		request.method = "eth_sendTransaction";
		request.id = id;
		
		Transaction transObject = new Transaction();
		transObject.from = _baseAddress;
		transObject.to = _trusternityContractAddress;
		transObject.data = "0x" + Hex.toHexString(buffer.array());	
		//String requestParams = _gson.toJson(transObject);				
		request.params = new Transaction[]{transObject};
				
		String response = SendRequest(_gson.toJson(request));
		return response;		
	}

	public byte[] getMethodId(String methodSignature) {
		byte[] method = new byte[4];
		byte[] temp = _cryptoUtil.sha3(methodSignature);
		System.arraycopy(temp, 0, method, 0, 4);
		return method;
	}

}
