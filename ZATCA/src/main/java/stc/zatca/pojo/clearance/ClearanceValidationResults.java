package stc.zatca.pojo.clearance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClearanceValidationResults {
	
	@JsonProperty
	private List<ClearanceMessage> infoMessages;
	
	@JsonProperty
	private List<ClearanceMessage> warningMessages;
	
	@JsonProperty
	private List<ClearanceMessage> errorMessages;
	
	@JsonProperty
	private String status;

	public List<ClearanceMessage> getInfoMessages() {
		return infoMessages;
	}

	public void setInfoMessages(List<ClearanceMessage> infoMessages) {
		this.infoMessages = infoMessages;
	}

	public List<ClearanceMessage> getWarningMessages() {
		return warningMessages;
	}

	public void setWarningMessages(List<ClearanceMessage> warningMessages) {
		this.warningMessages = warningMessages;
	}

	public List<ClearanceMessage> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<ClearanceMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
