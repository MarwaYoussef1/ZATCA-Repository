package stc.zatca.pojo.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportingRequest {
	
	@JsonProperty
	private String invoiceHash;
	
	@JsonProperty
	private String uuid;
	
	@JsonProperty
	private String invoice;

	public String getInvoiceHash() {
		return invoiceHash;
	}

	public void setInvoiceHash(String invoiceHash) {
		this.invoiceHash = invoiceHash;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	
	

	
	
}
