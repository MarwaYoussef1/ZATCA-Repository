package stc.zatca.bussiness;

import java.io.File;
import java.util.Base64;
import java.util.Collection;
import java.util.Properties;

import com.shaft.api.RestActions;
import com.shaft.cli.FileActions;

import stc.zatca.bussiness.Commands.InvoiceType;
import stc.zatca.pojo.reporting.ReportingResponse;
import stc.zatca.services.ReportingService;
import stc.zatca.utils.Utils;

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
		String binaryTokenDecoded=null;
		FileActions fileActions = new FileActions();
		InvoiceType type = InvoiceType.valueOf(inputType.toUpperCase());
	    binaryTokenDecoded=new String(Base64.getDecoder().decode(token));
		
		//Step4 copy the decoded token in cert.pem & copy the key to priv-key.pem
		Collection<File> certificateFiles = fileActions.getFileList(properties.getProperty("CertificateFolder"));
		for (File f : certificateFiles) {
			if (f.getName().contains("cert"))
			{
				Utils.writeContentInFile(f, binaryTokenDecoded);
				
			}
		}
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
