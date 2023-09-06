package stc.zatca.pojo.complianceInvoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplianceInvoiceMessage {
	
	@JsonProperty
	private String type;
	
	@JsonProperty
	private String code;
	
	@JsonProperty
	private String category;
	
	@JsonProperty
	private String message;
	
	@JsonProperty
	private String status;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	
	
}
