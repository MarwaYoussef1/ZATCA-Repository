package stc.zatca.tests.base;

import java.io.FileInputStream;
import java.util.Properties;

import com.shaft.api.RestActions;
import com.shaft.driver.DriverFactory;
import com.shaft.driver.SHAFT;

import stc.zatca.utils.Constants;


public abstract class BaseTest  {
	
	public static Properties properties;
	public static RestActions apiObj;
	static SHAFT.API driver; 
	static {
		readConfigurationFile();
	}
	
	

	public static Properties readConfigurationFile() {
		try{
			properties = new Properties();
			properties.load(new FileInputStream(Constants.CONFIG_FILE_PATH));
			
		} catch (Exception e){
			System.out.println("Cannot load properties");
		}
		return properties;	
	}
	public static void getBaseApiObj()
	{
		 String baseUrl=properties.getProperty("BaseUrl");
		 apiObj=DriverFactory.getAPIDriver(baseUrl);
		// driver=new SHAFT.API(baseUrl);
	}
	

}
/*****************************************************************************************************************/