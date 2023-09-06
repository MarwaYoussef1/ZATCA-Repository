package stc.zatca.pojo.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportingResponse {
	
	@JsonProperty
	private ReportingValidationResults validationResults;
	
	
	@JsonProperty
	private String reportingStatus;
	
	@JsonProperty
	private String clearedInvoice;
	
	

	public ReportingValidationResults getValidationResults() {
		return validationResults;
	}

	public void setValidationResults(ReportingValidationResults validationResults) {
		this.validationResults = validationResults;
	}

	
	


	public String getReportingStatus() {
		return reportingStatus;
	}

	public void setReportingStatus(String reportingStatus) {
		this.reportingStatus = reportingStatus;
	}

	public String getClearedInvoice() {
		return clearedInvoice;
	}

	public void setClearedInvoice(String clearedInvoice) {
		this.clearedInvoice = clearedInvoice;
	}

	

	
}
