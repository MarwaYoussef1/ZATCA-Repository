package stc.zatca.pojo.reporting;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportingValidationResults {
	
	@JsonProperty
	private List<ReportingMessage> infoMessages;
	
	@JsonProperty
	private List<ReportingMessage> warningMessages;
	
	@JsonProperty
	private List<ReportingMessage> errorMessages;
	
	@JsonProperty
	private String status;

	public List<ReportingMessage> getInfoMessages() {
		return infoMessages;
	}

	public void setInfoMessages(List<ReportingMessage> infoMessages) {
		this.infoMessages = infoMessages;
	}

	public List<ReportingMessage> getWarningMessages() {
		return warningMessages;
	}

	public void setWarningMessages(List<ReportingMessage> warningMessages) {
		this.warningMessages = warningMessages;
	}

	public List<ReportingMessage> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<ReportingMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
