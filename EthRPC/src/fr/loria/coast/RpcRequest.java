package fr.loria.coast;

public class RpcRequest {
	public String jsonrpc;
	public String method;
	public Object[] params;
	public String id;		
	
	public RpcRequest(){
		jsonrpc = "2.0";		
	}
}
