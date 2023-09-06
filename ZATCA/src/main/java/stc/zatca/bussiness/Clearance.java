package stc.zatca.bussiness;

import java.util.Properties;

import com.shaft.api.RestActions;

import stc.zatca.bussiness.Commands.InvoiceType;
import stc.zatca.pojo.clearance.ClearanceResponse;
import stc.zatca.services.ClearanceService;

public class Clearance {

	private RestActions apiObj;
	public Properties properties;
	public Commands commands;
	
	
	
	public Clearance(RestActions apiObj, Properties properties) {

		this.properties = properties;
		this.apiObj = apiObj;
		this.commands=new Commands(properties);

	}

	public ClearanceResponse invoiceClearance(String inputType, String token,String secret,String invoiceFileName) {
		ClearanceService clearanceService = new ClearanceService(apiObj, properties);
		ClearanceResponse clearanceResponseObj=null;
		String invoiceSignedXml = null;
		String invoiceRequestBody = null;
		InvoiceType type = InvoiceType.valueOf(inputType.toUpperCase());
		//Step1 validate the invoice
		boolean validInvoice = commands.validateInvoice(invoiceFileName, type);
		if (validInvoice) {
			//Step2 Sign the invoice
			invoiceSignedXml = commands.signInvoice(invoiceFileName, type);
			//Step3 invoiceRequest
			invoiceRequestBody = commands.invoiceRequest(invoiceSignedXml, type);
			//Step4 call Clearance Service 
			clearanceResponseObj= clearanceService.InvoiceClearance(token, secret, invoiceRequestBody);
		}

		return clearanceResponseObj;

	}
	
	


}
