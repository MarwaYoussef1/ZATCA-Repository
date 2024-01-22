package stc.zatca.services;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.shaft.api.RestActions;
import com.shaft.api.RestActions.ParametersType;
import com.shaft.api.RestActions.RequestType;
import com.shaft.tools.io.ReportManager;

import io.restassured.response.Response;
import stc.zatca.utils.Constants;

public class GenerateOtpService {

	private RestActions apiObj;
	public Properties properties;
	private static String generateOtpUrl = "/sme/otp/generate/";
	
	

	// Constractor
	public GenerateOtpService(RestActions apiObj, Properties properties) {
	
		this.properties = properties;
		this.apiObj=apiObj;
		
		

	}

	public String generateOtp(int noOfOtps,String vatNumber) {
		ReportManager.log("Start OTP generation .");
		String otp = null;
		String apiKey=properties.getProperty("GenerateOtpAPIKey");
		List<List<Object>> noOfOtpsValue=Arrays.asList(Arrays.asList("noOfOtp",noOfOtps)) ;
		Response response = apiObj.buildNewRequest(generateOtpUrl, RequestType.GET)
				.addHeader("vat", vatNumber).addHeader("request-from", "zatca-service").addHeader("api-key", apiKey).setParameters(noOfOtpsValue, ParametersType.QUERY).perform();
		ReportManager.log(response.body().asPrettyString());		
	    if(response.getStatusCode()==Constants.API_STATUS_CODE)
	    {
		List<Object> otps=RestActions.getResponseJSONValueAsList(response, "OTPs");
		otp=otps.get(0).toString();
		ReportManager.log("New Created OTP = "+otp);
		}
	    
		return otp;

	}
	
	
	
}
