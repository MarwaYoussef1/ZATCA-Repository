package stc.zatca.services;


import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaft.api.RequestBuilder.AuthenticationType;
import com.shaft.api.RestActions;
import com.shaft.api.RestActions.RequestType;
import com.shaft.tools.io.ReportManager;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import stc.zatca.pojo.complianceInvoice.ComplianceInvoiceRequest;
import stc.zatca.pojo.complianceInvoice.ComplianceInvoiceResponse;
import stc.zatca.utils.Constants;

public class ComplianceInvoiceService {

	private RestActions apiObj;
	public Properties properties;
	private static String complianceInvoiceUrl = "/compliance/invoices";
	
	

	// Constractor
	public ComplianceInvoiceService(RestActions apiObj, Properties properties) {
	
		this.properties = properties;
		this.apiObj=apiObj;
		
		

	}

	public ComplianceInvoiceResponse generateComplianceInvoice(String token,String secret,String invoiceBody) {
		ReportManager.log("Start calling complianceInvoice service to clear or report invoice");
		ComplianceInvoiceResponse responeObj = null;
		ObjectMapper mapper = new ObjectMapper();
		String clearenceStatus;
		String reportingStatus;
		try {
			ComplianceInvoiceRequest complianceInvoiceRequestObj = mapper.readValue(invoiceBody, ComplianceInvoiceRequest.class);
			String nestedJsonPayload = mapper.writeValueAsString(complianceInvoiceRequestObj).replace("\n", "").replace("\r", "").replace(" ", "");			
			JSONObject jsonObject = new JSONObject(nestedJsonPayload);
			Response response = apiObj.buildNewRequest(complianceInvoiceUrl, RequestType.POST).setAuthentication(token, secret, AuthenticationType.BASIC)
					.addHeader("Accept-Version", "V2").addHeader("Accept-Language", "en").addHeader("Authentication-Certificate", token)
					.setContentType(ContentType.JSON).setRequestBody(jsonObject).perform();
			
			ReportManager.log(response.body().asPrettyString());
			if(response.getStatusCode()==Constants.STATUS_CODE)
			{
			responeObj=response.body().as(ComplianceInvoiceResponse.class);
			clearenceStatus=responeObj.getClearanceStatus();
			reportingStatus=responeObj.getReportingStatus();
			
			if((clearenceStatus!=null && clearenceStatus.equals("CLEARED")) || (reportingStatus!=null && reportingStatus.equals("REPORTED")))
			{
				return responeObj;
			}
			}
		} catch ( JsonProcessingException | JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();		
		
	}
		ReportManager.log("End calling complianceInvoice service to clear or report invoice");
		return responeObj;
	}
	
}
