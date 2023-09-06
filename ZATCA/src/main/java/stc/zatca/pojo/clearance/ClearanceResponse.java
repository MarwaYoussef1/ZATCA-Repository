package stc.zatca.pojo.clearance;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClearanceResponse {
	
	@JsonProperty
	private ClearanceValidationResults validationResults;
	
	
	@JsonProperty
	private String clearanceStatus;
	
	@JsonProperty
	private String clearedInvoice;
	
	

	public ClearanceValidationResults getValidationResults() {
		return validationResults;
	}

	public void setValidationResults(ClearanceValidationResults validationResults) {
		this.validationResults = validationResults;
	}

	
	

	public String getClearanceStatus() {
		return clearanceStatus;
	}

	public void setClearanceStatus(String clearanceStatus) {
		this.clearanceStatus = clearanceStatus;
	}

	public String getClearedInvoice() {
		return clearedInvoice;
	}

	public void setClearedInvoice(String clearedInvoice) {
		this.clearedInvoice = clearedInvoice;
	}

	

	
}
