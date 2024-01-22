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
import stc.zatca.pojo.clearance.ClearanceRequest;
import stc.zatca.pojo.clearance.ClearanceResponse;
import stc.zatca.utils.Constants;


public class ClearanceService {

	private RestActions apiObj;
	public Properties properties;
	private static String clearanceUrl = "/invoices/clearance/single";
	
	

	// Constractor
	public ClearanceService(RestActions apiObj, Properties properties) {
	
		this.properties = properties;
		this.apiObj=apiObj;
		
		

	}

	public ClearanceResponse InvoiceClearance(String token,String secret,String invoiceBody,int expectedStatusCode) {
		ReportManager.log("Start calling clearance service to clear invoice");
		ClearanceResponse responeObj = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			ClearanceRequest clearanceRequestObj = mapper.readValue(invoiceBody, ClearanceRequest.class);
			String nestedJsonPayload = mapper.writeValueAsString(clearanceRequestObj).replace("\n", "").replace("\r", "").replace(" ", "");			
			JSONObject jsonObject = new JSONObject(nestedJsonPayload);
			Response response = apiObj.buildNewRequest(clearanceUrl, RequestType.POST).setAuthentication(token, secret, AuthenticationType.BASIC)
					.addHeader("Accept-Version", "V2").addHeader("Accept-Language", "en").addHeader("Authentication-Certificate", token)
					.setContentType(ContentType.JSON).setRequestBody(jsonObject).perform();
			System.out.println(response.body().asPrettyString());
			ReportManager.log(response.body().asPrettyString());
			
			if(response.getStatusCode()==expectedStatusCode)
			{
			responeObj=response.body().as(ClearanceResponse.class);
			
			}
		} catch ( JsonProcessingException | JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();		
		
	}
		ReportManager.log("End calling clearance service to clear invoice");
		return responeObj;
	}
	
}
