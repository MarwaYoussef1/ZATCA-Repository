package stc.zatca.bussiness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.shaft.api.RestActions;
import com.shaft.tools.io.ReportManager;

import stc.zatca.bussiness.Commands.InvoiceResultType;
import stc.zatca.bussiness.Commands.InvoiceType;
import stc.zatca.pojo.clearance.ClearanceMessage;
import stc.zatca.pojo.clearance.ClearanceResponse;
import stc.zatca.services.ClearanceService;
import stc.zatca.utils.Constants;
import stc.zatca.utils.Utils;


public class Clearance  {

	public Clearance(RestActions apiObj, Properties properties) {

		this.properties = properties;
		this.apiObj = apiObj;
		this.commands = new Commands(properties);

	}


	private RestActions apiObj;
	public Properties properties;
	public Commands commands;

	

	public ClearanceResponse invoiceClearance(String inputType, InvoiceResultType invoiceResultType,String token,String secret,String invoiceFileName) {
		ClearanceService clearanceService = new ClearanceService(apiObj, properties);
		ClearanceResponse clearanceResponseObj=null;
		String invoiceSignedXml = null;
		String invoiceRequestBody = null;
		int expectedStatusCode = 0;
		//Specify invoicetype
		commands.setInvoiceResultType(invoiceResultType);
		InvoiceType type = InvoiceType.valueOf(inputType.toUpperCase());
		
		//Step1 validate the invoice
		boolean validInvoice = commands.validateInvoice(invoiceFileName, type);
		if (validInvoice) {
			//Step2 Sign the invoice
			invoiceSignedXml = commands.signInvoice(invoiceFileName, type);
			//Step3 invoiceRequest
			invoiceRequestBody = commands.invoiceRequest(invoiceSignedXml, type);
			//Step4 call Clearance Service 
			switch (invoiceResultType) {
			case ACCEPTED:
			{
				expectedStatusCode=Constants.STATUS_CODE;
				break;
			}
			case ACCEPTEDWITHWARNIG: {
				expectedStatusCode=Constants.WARNING_STATUS_CODE;
				break;
		         
			}
			case ERROR: {
				
				break;
			}
			}
			clearanceResponseObj= clearanceService.InvoiceClearance(token, secret, invoiceRequestBody,expectedStatusCode);
			//Step5 Filter response
			return clearanceResponseObj;

	}
		return clearanceResponseObj;
	}
	
	public boolean checkWarningsInResponse(String invoiceFileName,ClearanceResponse clearanceResponseObj)
	{
		ArrayList<Boolean> checks =new ArrayList<Boolean>();
		String[] expectedCodes = null;
		boolean invoiceChecked;
		List<ClearanceMessage> warningMsgs;
		ArrayList<String> actualCodes = new ArrayList<String>();
		ArrayList<String> actualMsgs = new ArrayList<String>();
		expectedCodes = invoiceFileName.replace(".xml", "").split("_");
		ArrayList<String> expectedCodesList = new ArrayList<String>(Arrays.asList(expectedCodes));
	    warningMsgs=clearanceResponseObj.getValidationResults().getWarningMessages();
	     for(ClearanceMessage msg :warningMsgs)
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
			 checks.add((clearanceResponseObj.getValidationResults().getStatus()).contains("WARNING"));
	         checks.add(Utils.compareLists(actualCodes, expectedCodesList));
	         checks.add(!actualMsgs.contains("Empty Msg"));
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

	
	
}
