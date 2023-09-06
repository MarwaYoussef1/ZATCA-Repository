package stc.zatca.pojo.complianceInvoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplianceInvoiceResponse {
	
	@JsonProperty
	private ComplianceInvoiceValidationResults validationResults;
	
	@JsonProperty
	private String reportingStatus;
	
	
	@JsonProperty
	private String clearanceStatus;
	
	@JsonProperty
	private String qrSellertStatus;
	
	@JsonProperty
	private String qrBuyertStatus;

	public ComplianceInvoiceValidationResults getValidationResults() {
		return validationResults;
	}

	public void setValidationResults(ComplianceInvoiceValidationResults validationResults) {
		this.validationResults = validationResults;
	}

	public String getReportingStatus() {
		return reportingStatus;
	}

	public void setReportingStatus(String reportingStatus) {
		this.reportingStatus = reportingStatus;
	}

	

	public String getClearanceStatus() {
		return clearanceStatus;
	}

	public void setClearanceStatus(String clearanceStatus) {
		this.clearanceStatus = clearanceStatus;
	}

	public String getQrSellertStatus() {
		return qrSellertStatus;
	}

	public void setQrSellertStatus(String qrSellertStatus) {
		this.qrSellertStatus = qrSellertStatus;
	}

	public String getQrBuyertStatus() {
		return qrBuyertStatus;
	}

	public void setQrBuyertStatus(String qrBuyertStatus) {
		this.qrBuyertStatus = qrBuyertStatus;
	}


	
}
