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
import stc.zatca.pojo.reporting.ReportingRequest;
import stc.zatca.pojo.reporting.ReportingResponse;
import stc.zatca.utils.Constants;


public class ReportingService {

	private RestActions apiObj;
	public Properties properties;
	private static String reportingUrl = "/invoices/reporting/single";
	
	

	// Constractor
	public ReportingService(RestActions apiObj, Properties properties) {
	
		this.properties = properties;
		this.apiObj=apiObj;
		
		

	}

	public ReportingResponse InvoiceReporting(String token,String secret,String invoiceBody,int expectedStatusCode) {
		ReportManager.log("Start calling reporting service to clear invoice");
		ReportingResponse responeObj = null;
		ObjectMapper mapper = new ObjectMapper();
		String reportingStatus;
		//String clearedInvoice;
		try {
			ReportingRequest reportingRequestObj = mapper.readValue(invoiceBody, ReportingRequest.class);
			String nestedJsonPayload = mapper.writeValueAsString(reportingRequestObj).replace("\n", "").replace("\r", "").replace(" ", "");			
			JSONObject jsonObject = new JSONObject(nestedJsonPayload);
			Response response = apiObj.buildNewRequest(reportingUrl, RequestType.POST).setAuthentication(token, secret, AuthenticationType.BASIC)
					.addHeader("Accept-Version", "V2").addHeader("Accept-Language", "en").addHeader("Authentication-Certificate", token)
					.setContentType(ContentType.JSON).setRequestBody(jsonObject).perform();
			
			ReportManager.log(response.body().asPrettyString());
			System.out.println(response.body().asPrettyString());
			if(response.getStatusCode()==expectedStatusCode)
			{
			responeObj=response.body().as(ReportingResponse.class);
			reportingStatus=responeObj.getReportingStatus();
			//clearedInvoice=responeObj.getClearedInvoice();
			
			if((reportingStatus!=null && reportingStatus.equals("REPORTED")) )//&& (clearedInvoice!=null)
			{
				return responeObj;
			}
			}
		} catch ( JsonProcessingException | JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();		
		
	}
		ReportManager.log("End calling reporting service to clear invoice");
		return responeObj;
	}
	
}
