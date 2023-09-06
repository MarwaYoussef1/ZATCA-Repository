package stc.zatca.bussiness;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.shaft.cli.FileActions;
import com.shaft.tools.io.ReportManager;

import stc.zatca.utils.Utils;

public class Commands {

	public Properties properties;
	private String invoiceDirectoryPath;

	public Commands(Properties properties) {

		this.properties = properties;

	}

	public String getInvoiceDirectoryPath() {
		return invoiceDirectoryPath;
	}

	public void setInvoiceDirectoryPath(String invoiceDirectoryPath) {
		this.invoiceDirectoryPath = invoiceDirectoryPath;
	}

	public boolean validateInvoice(String invoiceFileName, InvoiceType type) {
		ReportManager.log("Start Validate invoice.");
		setInvoiceDirectoryPath(getInvoiceDirectory(type));
		File dir = new File(getInvoiceDirectoryPath());
		String invoicePath = getInvoiceDirectoryPath() + "\\" + invoiceFileName;
		String result = Utils.cmd(dir, "fatoora -validate -invoice " + invoicePath);
		ReportManager.log("End Validate invoice.");
		return result.contains(properties.getProperty("SuccessValidateInvoiceMsg"));
	}

	public String signInvoice(String invoiceFileName, InvoiceType type) {
		ReportManager.log("Start Signing invoice ");
		setInvoiceDirectoryPath(getInvoiceDirectory(type));
		FileActions fileActions = new FileActions();
		File dir = new File(getInvoiceDirectoryPath());
		File signedInvoice = null;
		String invoicePath = getInvoiceDirectoryPath() + "\\" + invoiceFileName;
		String result = Utils.cmd(dir, "fatoora -sign -invoice " + invoicePath);
		if (result.contains(properties.getProperty("SuccessSignInvoiceMsg"))) {
			Collection<File> files = fileActions.getFileList(getInvoiceDirectoryPath());
			for (File f : files) {
				if (f.getName().contains("_signed")) {
					signedInvoice = f;
					break;
				}

			}
		}
		ReportManager.log("End Signing invoice ");
		return signedInvoice.getName();
	}

	public String invoiceRequest(String signedInvoiceFileName, InvoiceType type) {
		ReportManager.log("Start invoicerequest json file creation for  " + type.name());
		setInvoiceDirectoryPath(getInvoiceDirectory(type));
		FileActions fileActions = new FileActions();
		File dir = new File(getInvoiceDirectoryPath());
		File signedInvoice = null;
		String invoiceRequest = null;
		String invoicePath = getInvoiceDirectoryPath() + "\\" + signedInvoiceFileName;
		signedInvoice = new File(invoicePath);
		String result = Utils.cmd(dir, "fatoora -invoicerequest -invoice " + invoicePath);
		if (result.contains(properties.getProperty("SuccessInvoiceRequestMsg"))) {
			Collection<File> files = fileActions.getFileList(getInvoiceDirectoryPath());
			for (File f : files) {
				if (f.getName().contains("generated-json-request")) {
					invoiceRequest = fileActions.readFile(f.getAbsolutePath()).replace("\n", "").replace("\r", "");
					f.delete();
					signedInvoice.delete();
					break;
				}

			}
		}
		ReportManager.log("End invoicerequest json file creation for  " + type.name());
		return invoiceRequest;
	}

	public Collection<File> generateCSR(String csrFileName, InvoiceType type) {
		ReportManager.log("Start CSR generation .");
		setInvoiceDirectoryPath(getInvoiceDirectory(type));
		File dir = new File(getInvoiceDirectoryPath());
		Collection<File> csrFiles = new ArrayList<File>();
		File generatedCsrFile = null;
		File generatedKeyFile = null;
		String csrFilePath = properties.getProperty("CsrFolder") + csrFileName;
		String result = Utils.cmd(dir, "fatoora -sim -csr -csrconfig " + csrFilePath);// + " -nonprod"
		boolean csrGenerated = result.contains(properties.getProperty("SuccessCsrMsg"));
		if (csrGenerated) {
			FileActions fileActions = new FileActions();
			Collection<File> files = fileActions.getFileList(getInvoiceDirectoryPath());
			for (File f : files) {
				if (f.getName().contains("generated-csr"))
					generatedCsrFile = f;
				if (f.getName().contains("generated-private-key"))
					generatedKeyFile = f;
			}
			csrFiles.add(generatedCsrFile);
			csrFiles.add(generatedKeyFile);
		}
		ReportManager.log("End CSR generation .");
		return csrFiles;

	}

	String getInvoiceDirectory(InvoiceType type) {
		String invoicesFolder = properties.getProperty("InvoicesFolder");
		String standardInvoiceFolder = properties.getProperty("StandardInvoicesFolder");
		String simplifiedInvoiceFolder = properties.getProperty("SimplifiedInvoicesFolder");
		String invoiceNoteFolder = properties.getProperty("InvoiceNoteFolder");
		String creditInvoiceFolder = properties.getProperty("CreditInvoiceFolder");
		String debitInvoiceFolder = properties.getProperty("DebitInvoiceFolder");
		String invoiceDirectoryPath = null;
		switch (type) {
		case STANDARDNOTE: {
			invoiceDirectoryPath = invoicesFolder + "\\" + standardInvoiceFolder + "\\" + invoiceNoteFolder;
			System.out.println("StandardNote path = " + invoiceDirectoryPath);
			break;
		}
		case STANDARDCREDIT: {
			invoiceDirectoryPath = invoicesFolder + "\\" + standardInvoiceFolder + "\\" + creditInvoiceFolder;
			System.out.println("StandardCredit path = " + invoiceDirectoryPath);
			break;
		}
		case STANDARDDEBIT: {
			invoiceDirectoryPath = invoicesFolder + "\\" + standardInvoiceFolder + "\\" + debitInvoiceFolder;
			System.out.println("StandardDebit path = " + invoiceDirectoryPath);
			break;
		}
		case SIMPLIFIEDNOTE: {
			invoiceDirectoryPath = invoicesFolder + "\\" + simplifiedInvoiceFolder + "\\" + invoiceNoteFolder;
			System.out.println("SimplifiedNote path = " + invoiceDirectoryPath);
			break;
		}
		case SIMPLIFIEDCREDIT: {
			invoiceDirectoryPath = invoicesFolder + "\\" + simplifiedInvoiceFolder + "\\" + creditInvoiceFolder;
			System.out.println("SimplifiedCredit path = " + invoiceDirectoryPath);
			break;
		}
		case SIMPLIFIEDDEBIT: {
			invoiceDirectoryPath = invoicesFolder + "\\" + simplifiedInvoiceFolder + "\\" + debitInvoiceFolder;
			System.out.println("SimplifiedDebit path = " + invoiceDirectoryPath);
			break;
		}
		}
		return invoiceDirectoryPath;

	}

	public enum InvoiceType {
		STANDARDNOTE, STANDARDCREDIT, STANDARDDEBIT, SIMPLIFIEDNOTE, SIMPLIFIEDCREDIT, SIMPLIFIEDDEBIT;

	}

}
