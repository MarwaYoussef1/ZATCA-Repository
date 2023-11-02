package stc.zatca.services;

import java.util.Properties;

import org.json.simple.JSONObject;

import com.shaft.api.RequestBuilder.AuthenticationType;
import com.shaft.api.RestActions;
import com.shaft.api.RestActions.RequestType;
import com.shaft.tools.io.ReportManager;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import stc.zatca.pojo.CSIDProductionResponse;
import stc.zatca.utils.Constants;

public class CSIDProductionService {

	private RestActions apiObj;
	public Properties properties;
	private static String CSIDProductionUrl = "/production/csids/";///core/csids
	
	

	// Constractor
	public CSIDProductionService(RestActions apiObj, Properties properties) {
	
		this.properties = properties;
		this.apiObj=apiObj;
		
		

	}

	public CSIDProductionResponse generateCSID(String token,String secret,String requestId) {

		CSIDProductionResponse responeObj = null;
	 RequestSpecification httpRequestSpec;
		  //Request paylaod sending along with post request
		  JSONObject requestParams=new JSONObject();
		  requestParams.put("compliance_request_id",String.valueOf(requestId));
		 	 
		Response response = apiObj.buildNewRequest(CSIDProductionUrl, RequestType.POST).setAuthentication(token, secret, AuthenticationType.BASIC)
				.addHeader("Accept-Version", "V2").setContentType(ContentType.JSON).setRequestBody(requestParams).perform();
		
		ReportManager.log(response.body().asPrettyString());
		System.out.println(response.body().asPrettyString());
		
	
	    if(response.getStatusCode()==Constants.STATUS_CODE)
	    	responeObj =response.body().as(CSIDProductionResponse.class);
	    
	    return responeObj;

	}
	
	
	
}
