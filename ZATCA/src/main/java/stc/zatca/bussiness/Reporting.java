package stc.zatca.bussiness;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.shaft.api.RestActions;
import com.shaft.cli.FileActions;
import com.shaft.tools.io.ReportManager;

import stc.zatca.bussiness.Commands.InvoiceResultType;
import stc.zatca.bussiness.Commands.InvoiceType;
import stc.zatca.pojo.clearance.ClearanceMessage;
import stc.zatca.pojo.clearance.ClearanceResponse;
import stc.zatca.pojo.reporting.ReportingMessage;
import stc.zatca.pojo.reporting.ReportingResponse;
import stc.zatca.services.ReportingService;
import stc.zatca.utils.Constants;
import stc.zatca.utils.Utils;

public class Reporting {

	private RestActions apiObj;
	public Properties properties;
	public Commands commands;

	public Reporting(RestActions apiObj, Properties properties) {

		this.properties = properties;
		this.apiObj = apiObj;
		this.commands = new Commands(properties);

	}

	public ReportingResponse invoiceReporting(String inputType, InvoiceResultType invoiceResultType, String token,
			String secret, String invoiceFileName) {
		ReportingService reportingService = new ReportingService(apiObj, properties);
		ReportingResponse reportingResponseObj = null;
		String invoiceSignedXml = null;
		String invoiceRequestBody = null;
		String binaryTokenDecoded = null;
		int expectedStatusCode = 0;
		FileActions fileActions = new FileActions();
		InvoiceType type = InvoiceType.valueOf(inputType.toUpperCase());
		binaryTokenDecoded = new String(Base64.getDecoder().decode(token));
		// Specify invoicetype
		commands.setInvoiceResultType(invoiceResultType);
		// Step4 copy the decoded token in cert.pem & copy the key to priv-key.pem
		Collection<File> certificateFiles = fileActions.getFileList(properties.getProperty("CertificateFolder"));
		for (File f : certificateFiles) {
			if (f.getName().contains("cert")) {
				Utils.writeContentInFile(f, binaryTokenDecoded);

			}
		}
		// Step1 sign the invoice
		invoiceSignedXml = commands.signInvoice(invoiceFileName, type);
		// Step2 validate the invoice
		boolean validInvoice = commands.validateInvoice(invoiceSignedXml, type);
		if (validInvoice) {
			// Step3 invoiceRequest
			invoiceRequestBody = commands.invoiceRequest(invoiceSignedXml, type);
			// Step4 call Reporting Service

			switch (invoiceResultType) {
			case ACCEPTED: {
				expectedStatusCode = Constants.API_STATUS_CODE;
				break;
			}
			case ACCEPTEDWITHWARNIG: {
				expectedStatusCode = Constants.API_WARNING_STATUS_CODE;
				break;

			}
			case ERROR:
			{
				expectedStatusCode=Constants.API_ERROR_STATUS_CODE;
				break;
			}
			}
			reportingResponseObj = reportingService.InvoiceReporting(token, secret, invoiceRequestBody,expectedStatusCode);
		}

		return reportingResponseObj;

	}
	

	public boolean checkWarningsInResponse(String invoiceFileName,ReportingResponse reportingResponseObj)
	{
		boolean invoiceChecked;
		ArrayList<Boolean> checks =new ArrayList<Boolean>();
		checks=checkCodesMsgsInReponse(invoiceFileName,InvoiceResultType.ACCEPTEDWITHWARNIG,reportingResponseObj);
	     if(checks.stream().allMatch(bool -> bool == true))
		{
	      invoiceChecked=true;
		 ReportManager.log("All Warning codes returned sucessfully with messages.");
	     ReportManager.log("End compliance Warning invoice with warnings.");
	     }
		else {
			invoiceChecked=false;
			 ReportManager.log("Either Warning codes  not displayed or messages are blank .");
		}
        
		
		 ReportManager.log("End compliance Warning invoice with warnings.");
	
         return invoiceChecked;
	}
	
	public boolean checkErrorsInResponse(String invoiceFileName,ReportingResponse reportingResponseObj)
	{
		boolean invoiceChecked;
		ArrayList<Boolean> checks =new ArrayList<Boolean>();
		checks=checkCodesMsgsInReponse(invoiceFileName,InvoiceResultType.ERROR,reportingResponseObj);
	     if(checks.stream().allMatch(bool -> bool == true))
		{
	      invoiceChecked=true;
		 ReportManager.log("All Error codes returned sucessfully with messages.");
	     ReportManager.log("End compliance invoice with Errors.");
	     }
		else {
			invoiceChecked=false;
			 ReportManager.log("Either Error codes   not displayed or messages are blank .");
		}
        
		
		 ReportManager.log("End compliance  invoice with Errors.");
	
         return invoiceChecked;
	}
	
	private  ArrayList<Boolean> checkCodesMsgsInReponse(String invoiceFileName,InvoiceResultType invoiceResultType,ReportingResponse reportingResponseObj)
	{
		ArrayList<Boolean> checks =new ArrayList<Boolean>();
		List<ReportingMessage> reportingMsgs = null;
		String[] expectedCodes = null;
		String statusMsg = null;
		ArrayList<String> actualCodes = new ArrayList<String>();
		ArrayList<String> actualMsgs = new ArrayList<String>();
		expectedCodes = invoiceFileName.replace(".xml", "").split("_");
		ArrayList<String> expectedCodesList = new ArrayList<String>(Arrays.asList(expectedCodes));
		switch (invoiceResultType) {
		case ACCEPTEDWITHWARNIG: {
			reportingMsgs=reportingResponseObj.getValidationResults().getWarningMessages();
			statusMsg=Constants.API_WARNING_INVOICE;
			break;
	         
		}
		case ERROR:
		{
			reportingMsgs=reportingResponseObj.getValidationResults().getErrorMessages();
			statusMsg=Constants.API_ERROR_INVOICE;
			break;
		}
		}
		
		 for(ReportingMessage msg :reportingMsgs)
		 {
			 actualCodes.add(msg.getCode());
			 if(msg.getMessage().equals(null)|| msg.getMessage().isEmpty() || msg.getMessage().isBlank())
			 {
				 actualMsgs.add("Empty Msg");
			 }
			 else
			 actualMsgs.add(msg.getMessage());
			
		 }
		 //All codes are  found in response
		 checks.add((reportingResponseObj.getValidationResults().getStatus()).contains(statusMsg));
         checks.add(actualCodes.containsAll(expectedCodesList));
         checks.add(!actualMsgs.contains("Empty Msg"));
         
         return checks;
	}



}
