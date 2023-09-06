package stc.zatca.bussiness;

import java.io.File;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import com.shaft.api.RestActions;
import com.shaft.cli.FileActions;
import com.shaft.tools.io.ReportManager;

import stc.zatca.bussiness.Commands.InvoiceType;
import stc.zatca.pojo.CSIDProductionResponse;
import stc.zatca.pojo.ComplianceCSIDResponse;
import stc.zatca.pojo.complianceInvoice.ComplianceInvoiceResponse;
import stc.zatca.services.CSIDProductionService;
import stc.zatca.services.ComplianceCSIDService;
import stc.zatca.services.ComplianceInvoiceService;
import stc.zatca.services.GenerateOtpService;
import stc.zatca.utils.Utils;

public class GenerateCSID {

	private RestActions apiObj;
	public Properties properties;
	public Commands commands;
	
	
	public GenerateCSID(RestActions apiObj, Properties properties) {

		this.properties = properties;
		this.apiObj = apiObj;
		this.commands=new Commands(properties);

	}

	public ComplianceCSIDResponse complianceCSID(InvoiceType type, String invoiceFileName, String vatNumber,
			String csrFileName) {
		GenerateOtpService otpService = new GenerateOtpService(apiObj, properties);
		ComplianceCSIDService complianceCSIDService = new ComplianceCSIDService(apiObj, properties);
		ComplianceCSIDResponse ComplianceCSIDResponseObj = null;

		FileActions fileActions = new FileActions();
		String otp = null;
		//Step1 validate the invoice
		boolean validInvoice = commands.validateInvoice(invoiceFileName, type);
		if (validInvoice) {
			//Step2 Generate CSR
			Collection<File> csrGeneratedFiles = commands.generateCSR(csrFileName, type);
			Optional<File> csrFile = csrGeneratedFiles.stream().findFirst();
			String csr = fileActions.readFile(csrFile.get().getAbsolutePath());
			
			ReportManager.log("csr = " + csr);
		   //Step3 Generate OTP
			otp = otpService.generateOtp(1, vatNumber);
			//Step4 Generate complianceCSID
			ComplianceCSIDResponseObj = complianceCSIDService.generateComplianceCSID(otp, csr);
			ReportManager.log("requestID " + ComplianceCSIDResponseObj.getRequestID());
			ReportManager.log("binarySecurityToken " + ComplianceCSIDResponseObj.getBinarySecurityToken());
			ReportManager.log("secret " + ComplianceCSIDResponseObj.getSecret());
			//delete generated csr files 
			for	(File f:csrGeneratedFiles)
			{
				f.delete();
			}

		}

		return ComplianceCSIDResponseObj;

	}
	
	public ComplianceCSIDResponse complianceSimplifiedCSID(InvoiceType type, String invoiceFileName, String vatNumber,
			String csrFileName) {
		GenerateOtpService otpService = new GenerateOtpService(apiObj, properties);
		ComplianceCSIDService complianceCSIDService = new ComplianceCSIDService(apiObj, properties);
		ComplianceCSIDResponse ComplianceCSIDResponseObj = null;
		String binaryToken=null;
		String binaryTokenDecoded=null;
		

		FileActions fileActions = new FileActions();
		String otp = null;
		//Step1 Generate CSR
		Collection<File> csrGeneratedFiles = commands.generateCSR(csrFileName, type);
		Optional<File> csrFile = csrGeneratedFiles.stream().findFirst();
		Optional<File> keyFile = csrGeneratedFiles.stream().skip(1).findFirst();
		String csr = fileActions.readFile(csrFile.get().getAbsolutePath());
		String key=fileActions.readFile(keyFile.get().getAbsolutePath());
		
		ReportManager.log("csr = " + csr);
		//Step2 Generate OTP
		otp = otpService.generateOtp(1, vatNumber);
		
		//Step3 Generate complianceCSID
		ComplianceCSIDResponseObj = complianceCSIDService.generateComplianceCSID(otp, csr);
		binaryToken=ComplianceCSIDResponseObj.getBinarySecurityToken();
		ReportManager.log("requestID " + ComplianceCSIDResponseObj.getRequestID());
		ReportManager.log("binarySecurityToken " + binaryToken);
		ReportManager.log("secret " + ComplianceCSIDResponseObj.getSecret());
		binaryTokenDecoded=new String(Base64.getDecoder().decode(binaryToken));
		
		//Step4 copy the decoded token in cert.pem & copy the key to priv-key.pem
		Collection<File> certificateFiles = fileActions.getFileList(properties.getProperty("CertificateFolder"));
		for (File f : certificateFiles) {
			if (f.getName().contains("cert"))
			{
				Utils.writeContentInFile(f, binaryTokenDecoded);
				
			}
			if (f.getName().contains("priv-key"))
			{
				Utils.writeContentInFile(f, key);
			}
				
		}
		//delete generated csr files 
		for	(File f:csrGeneratedFiles)
		{
			f.delete();
		}
	
		return ComplianceCSIDResponseObj;

	}

	public ComplianceInvoiceResponse complianceInvoice(InvoiceType type, String invoiceFileName, String token,
			String secretKey) {
		
		String invoiceSignedXml = null;
		String invoiceRequestBody = null;
		boolean validInvoice=false;
		ComplianceInvoiceService complianceInvoiceService = new ComplianceInvoiceService(apiObj, properties);
		ComplianceInvoiceResponse ComplianceInvoiceResponseObj=null;
		switch (type) {
		case STANDARDCREDIT:
		case STANDARDDEBIT:
		{
			validInvoice =commands.validateInvoice(invoiceFileName, type);
			break;
		}
		case STANDARDNOTE:
		case SIMPLIFIEDNOTE:
		case SIMPLIFIEDCREDIT:
		case SIMPLIFIEDDEBIT:
		{
			validInvoice =true;
			break;
		}
		}
		// Step 5 sign fatoora
		if(!validInvoice)
		{
		   return ComplianceInvoiceResponseObj;
		}
		invoiceSignedXml = commands.signInvoice(invoiceFileName, type);
		switch (type) {
		case SIMPLIFIEDNOTE:
		case SIMPLIFIEDCREDIT:
		case SIMPLIFIEDDEBIT:
		{
			validInvoice = commands.validateInvoice(invoiceSignedXml, type);
			break;
		}
		case STANDARDNOTE:
		{
			validInvoice =true;
			break;
		}
			
		}
		if(validInvoice)
		{
		//Step 6 invoice request
		invoiceRequestBody = commands.invoiceRequest(invoiceSignedXml, type);
		//Step 7 check on compliance request
		 ComplianceInvoiceResponseObj = complianceInvoiceService.generateComplianceInvoice(token, secretKey, invoiceRequestBody);
		
		}
		return ComplianceInvoiceResponseObj;
	}
	
	public CSIDProductionResponse csidProduction( String token,String secretKey,String requestId) {
		
		CSIDProductionService CSIDProductionService = new CSIDProductionService(apiObj, properties);
		//Step 8 Generate Production CSID
		CSIDProductionResponse CSIDProductionResponseObj = CSIDProductionService.generateCSID(token, secretKey, requestId);
		return CSIDProductionResponseObj;
	}

	


}
