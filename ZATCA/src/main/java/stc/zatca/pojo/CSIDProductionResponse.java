package stc.zatca.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CSIDProductionResponse {
	
	@JsonProperty
	private String requestID;
	
	@JsonProperty
	private String dispositionMessage;
	
	@JsonProperty
	private String binarySecurityToken;
	
	@JsonProperty
	private String secret;
	
	@JsonProperty
	private String  tokenType;

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getDispositionMessage() {
		return dispositionMessage;
	}

	public void setDispositionMessage(String dispositionMessage) {
		this.dispositionMessage = dispositionMessage;
	}

	public String getBinarySecurityToken() {
		return binarySecurityToken;
	}

	public void setBinarySecurityToken(String binarySecurityToken) {
		this.binarySecurityToken = binarySecurityToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	 
	
	
}
