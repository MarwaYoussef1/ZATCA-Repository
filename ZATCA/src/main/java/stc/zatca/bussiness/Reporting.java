package stc.zatca.bussiness;

import java.util.Properties;

import com.shaft.api.RestActions;

import stc.zatca.bussiness.Commands.InvoiceType;
import stc.zatca.pojo.reporting.ReportingResponse;
import stc.zatca.services.ReportingService;

public class Reporting {

	private RestActions apiObj;
	public Properties properties;
	public Commands commands;
	
	
	
	public Reporting(RestActions apiObj, Properties properties) {

		this.properties = properties;
		this.apiObj = apiObj;
		this.commands=new Commands(properties);

	}

	public ReportingResponse invoiceReporting(String inputType, String token,String secret,String invoiceFileName) {
		ReportingService reportingService = new ReportingService(apiObj, properties);
		ReportingResponse reportingResponseObj=null;
		String invoiceSignedXml = null;
		String invoiceRequestBody = null;
		InvoiceType type = InvoiceType.valueOf(inputType.toUpperCase());
		//Step1 sign the invoice		
		invoiceSignedXml = commands.signInvoice(invoiceFileName, type);
			//Step2 validate the invoice
			boolean validInvoice = commands.validateInvoice(invoiceSignedXml, type);
			if (validInvoice) {
			//Step3 invoiceRequest
			invoiceRequestBody = commands.invoiceRequest(invoiceSignedXml, type);
			//Step4 call Reporting Service 
			reportingResponseObj= reportingService.InvoiceReporting(token, secret, invoiceRequestBody);
		}

		return reportingResponseObj;

	}
	
	


}
