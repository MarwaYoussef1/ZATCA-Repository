package stc.zatca.tests;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.shaft.tools.io.ReportManager;
import com.shaft.validation.Validations;

import stc.zatca.bussiness.Clearance;
import stc.zatca.bussiness.Reporting;
import stc.zatca.pojo.clearance.ClearanceResponse;
import stc.zatca.pojo.reporting.ReportingResponse;
import stc.zatca.testdata.TestData;
import stc.zatca.tests.base.BaseTest;
import stc.zatca.utils.Constants;

public class ClearanceReportingTests extends BaseTest {
	
	public Clearance clearanceBusinessObj;
	public Reporting reportingBusinessObj;
	
	
	@BeforeMethod
	public void beforeMethod() {
		getBaseApiObj();
		clearanceBusinessObj=new Clearance(apiObj,properties);
		reportingBusinessObj=new Reporting(apiObj,properties);
		
	}
	
	/**
	 * Function to read data for testAcceptedClearanceInvoice testcase
	 *
	 * @return Object[][] 2d array contains data from excel sheet
	 */

	@DataProvider(name = "testAcceptedClearanceInvoiceTestData")
	public Object[][] testAcceptedClearanceInvoiceTestData() throws InvalidFormatException, IOException {

		Object[][] data = TestData.fetchData(properties.getProperty("testDataPath"), "testAcceptedClearanceInvoice");
		return data;
	}
	
	/**
	 * Function to read data for testAcceptedReportingInvoice testcase
	 *
	 * @return Object[][] 2d array contains data from excel sheet
	 */

	@DataProvider(name = "testAcceptedReportingInvoiceTestData")
	public Object[][] testAcceptedReportingInvoiceTestData() throws InvalidFormatException, IOException {

		Object[][] data = TestData.fetchData(properties.getProperty("testDataPath"), "testAcceptedReportingInvoice");
		return data;
	}
	

	@Test(description="Test Accepted Clearance Invoice",dataProvider = "testAcceptedClearanceInvoiceTestData") 
	public void testAcceptedClearanceInvoice(String invoiceType,String invoiceFileName,ITestContext testContext){
		ReportManager.log("Start Test clearance flow for standard invoices.");
		String binarytoken=testContext.getAttribute(Constants.BINARYSECURITYTOKEN).toString();
		String secretKey = testContext.getAttribute(Constants.SECRET).toString();
		ClearanceResponse clearanceResponseObj=null;
		clearanceResponseObj=clearanceBusinessObj.invoiceClearance(invoiceType, binarytoken, secretKey, invoiceFileName);
		Validations.assertThat().object(clearanceResponseObj.getClearedInvoice()).isNotNull().perform();
	    ReportManager.log("Invoice " + invoiceFileName + " Cleared successfully.");
		ReportManager.log("End Test clearance flow."); 
	}
	
	@Test(description="Test Accepted Reporting Invoice",dataProvider = "testAcceptedReportingInvoiceTestData") 
	public void testAcceptedReportingInvoice(String invoiceType,String invoiceFileName,ITestContext testContext){
		ReportManager.log("Start Test Reporting flow for simplified invoices.");
		String binarytoken=testContext.getAttribute(Constants.BINARYSECURITYTOKEN).toString();
		String secretKey = testContext.getAttribute(Constants.SECRET).toString();
		ReportingResponse reportingResponseObj=null;
		reportingResponseObj=reportingBusinessObj.invoiceReporting(invoiceType, binarytoken, secretKey, invoiceFileName);
		Validations.assertThat().object(reportingResponseObj).isNotNull().perform();
	    ReportManager.log("Invoice " + invoiceFileName + " Reported successfully.");
		ReportManager.log("End Test Reporting flow."); 
	}
	
	
	
	

	
	
    
	
}
