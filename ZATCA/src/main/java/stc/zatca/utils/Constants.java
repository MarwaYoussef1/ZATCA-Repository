package stc.zatca.utils;

public interface Constants {
	
	public static final String CONFIG_FILE_PATH = "./src/test/resources/ZATCAProperties/ZATCA.properties";
	public static final String SDK_FOLDER_PATH="C:\\zatca-einvoicing-sdk-238-R3.3.0\\Apps";
    public static final String SDK_JAVA_PATH="C:\\Program Files\\Java\\jdk-11.0.17";
    public static final String SDK_JAVA_PATH_BIN="C:\\Program Files\\Java\\jdk-11.0.17/bin";
	public static final int API_STATUS_CODE=200;
	public static final int API_WARNING_STATUS_CODE=202;
	public static final int API_ERROR_STATUS_CODE=400;
	public static final String BINARYSECURITYTOKEN="binarySecurityToken";
	public static final String SECRET="secret";
	public static final String SDK_WARNING="[WARN]";
	public static final String SDK_ERROR="[ERROR]";
	public static final String API_WARNING_INVOICE="WARNING";
	public static final String API_ERROR_INVOICE="ERROR";
	public static final String API_CLEARED_INVOICE="CLEARED";
	public static final String API_NOT_CLEARED_INVOICE="NOT_CLEARED";
	public static final String API_REPORTED_INVOICE="REPORTED";
	public static final String API_NOT_REPORTED_INVOICE="NOT_REPORTED";
	
	
}
