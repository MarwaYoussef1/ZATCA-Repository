package stc.zatca.pojo.complianceInvoice;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplianceInvoiceValidationResults {
	
	@JsonProperty
	private List<ComplianceInvoiceMessage> infoMessages;
	
	@JsonProperty
	private List<ComplianceInvoiceMessage> warningMessages;
	
	@JsonProperty
	private List<ComplianceInvoiceMessage> errorMessages;
	
	@JsonProperty
	private String status;

	public List<ComplianceInvoiceMessage> getInfoMessages() {
		return infoMessages;
	}

	public void setInfoMessages(List<ComplianceInvoiceMessage> infoMessages) {
		this.infoMessages = infoMessages;
	}

	public List<ComplianceInvoiceMessage> getWarningMessages() {
		return warningMessages;
	}

	public void setWarningMessages(List<ComplianceInvoiceMessage> warningMessages) {
		this.warningMessages = warningMessages;
	}

	public List<ComplianceInvoiceMessage> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<ComplianceInvoiceMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
