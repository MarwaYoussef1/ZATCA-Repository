package stc.zatca.services;

import java.util.Properties;

import org.json.simple.JSONObject;

import com.shaft.api.RestActions;
import com.shaft.api.RestActions.RequestType;
import com.shaft.tools.io.ReportManager;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import stc.zatca.pojo.ComplianceCSIDResponse;
import stc.zatca.utils.Constants;

public class ComplianceCSIDService {

	private RestActions apiObj;
	public Properties properties;
	private static String complianceCSIDUrl = "/compliance";/// core/csids

	// Constractor
	public ComplianceCSIDService(RestActions apiObj, Properties properties) {

		this.properties = properties;
		this.apiObj = apiObj;

	}

	public ComplianceCSIDResponse generateComplianceCSID(String otp, String csrValue) {

		ReportManager.log("Start calling complianceCSID service ");
		// Request paylaod sending along with post request
		JSONObject requestParams = new JSONObject();
		requestParams.put("csr", csrValue);
		ComplianceCSIDResponse responeObj = null;
		Response response = apiObj.buildNewRequest(complianceCSIDUrl, RequestType.POST).addHeader("OTP", otp)
				.addHeader("request-from", "zatca-service").addHeader("Accept-Version", "V2")
				.setContentType(ContentType.JSON).setRequestBody(requestParams).perform();

		ReportManager.log(response.body().asPrettyString());
		System.out.println(response.body().asPrettyString());
		if (response.getStatusCode() == Constants.API_STATUS_CODE) {
			responeObj = response.body().as(ComplianceCSIDResponse.class);
			ReportManager.log("End calling complianceCSID service ");
			if (responeObj.getErrors() != null) {
				return null;
			} else
				return responeObj;

		}

		return responeObj;

	}
}
