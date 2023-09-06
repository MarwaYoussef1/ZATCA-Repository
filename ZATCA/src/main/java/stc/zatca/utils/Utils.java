// Utils
// This Class is used for all common utility functions
// 18-11-2021 
//----------------------

package stc.zatca.utils;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;

import com.shaft.tools.io.ReportManager;

public class Utils {

	static ArrayList<String> treeMdsNames;
	static String compNode = "";
	static String nodeValue = "";
	static String mainNode = "";

	public static boolean setProperty(String key, String value) {
		boolean propertySaved;
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
				PropertiesConfiguration.class).configure(
						params.properties().setFileName("./src/test/resources/Properties/testconfig.properties"));
		Configuration config;
		try {
			config = builder.getConfiguration();
			config.setProperty(key, value);
			builder.save();
			propertySaved = true;
			ReportManager.log("property with key = " + key + " changed");
		} catch (ConfigurationException e) {
			ReportManager.log("property with key = " + key + " couldn't be changed");
			propertySaved = false;
		}

		return propertySaved;
	}

	

	public static void PrintArray(ArrayList<String> Ar) {
		ReportManager.log("--------------- Array -----------------");
		ReportManager.log(Arrays.toString(Ar.toArray()));
		ReportManager.log("--------------- Array End-----------------");
	}

	

	public static boolean compareLists(ArrayList<String> List1, ArrayList<String> List2) {
		boolean matched;
		Collections.sort(List1);
		Collections.sort(List2);

		matched = List1.equals(List2);
		return matched;
	}

	
	public static ArrayList<String> sortListAlphabetically(ArrayList<String> unOrderedList,boolean asc)
	{
		
		Utils.PrintArray(unOrderedList);
		Collections.sort(unOrderedList);
		if(!asc)//sort descending
		{
			Collections.reverse(unOrderedList);
		}
		Utils.PrintArray(unOrderedList);
		return unOrderedList;
	}
	
	public static String getCurrentTime() {

		Date date = new Date(System.currentTimeMillis());
		DateFormat formatter = new SimpleDateFormat("ddMMyyHHmmss");
		return formatter.format(date);

		
	}
	
	  // function to generate a random string of length n
    public  static String getRandomString(int n, String type)
    {
    	 String text=null;
  
    	if(type.equals("num"))
    		text=RandomStringUtils.randomNumeric(n);
    	else if (type.equals("String"))
    		text=RandomStringUtils.randomAlphabetic(n);
    	else
    		text=RandomStringUtils.randomAlphanumeric(n);
    	
       	return text;
    }
    
    
  //generate random e-mail addresses 
    public static String generateRandomEmailAddress(String domain) {
        String emailAddress = "";
        // Generate random email address
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        while (emailAddress.length() < 5) {
            int character = (int) (Math.random() * 26);
            emailAddress += alphabet.substring(character, character + 1);
        }
        emailAddress += Integer.valueOf((int) (Math.random() * 99))
                .toString();
        emailAddress += "@" + domain+".com";
        return emailAddress;
    }
    
    public static String cmd(File dir, String command) {
        System.out.println("> " + command);   // better to use e.g. Slf4j
        System.out.println();
        ReportManager.log(command);
        String[] commands = new String[] { "cmd.exe", "/c", "set JAVA_HOME="+Constants.SDK_JAVA_PATH+" && set PATH=%JAVA_HOME%\\bin;%PATH% && "+command };
        try {
            Process p =Runtime.getRuntime().exec(commands, null, dir);
            String result = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
            String error = IOUtils.toString(p.getErrorStream(), StandardCharsets.UTF_8);
            System.out.println(result);
            System.out.println(error);
            ReportManager.log(error);
            if (error != null && !error.isEmpty()) {  // throw exception if error stream
                throw new RuntimeException(error);
            }
           
          
            return result;                // return result for optional additional processing
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeContentInFile(File f,String content)
    {
    	try{
			  FileUtils.write(f, "", StandardCharsets.UTF_8);
			  FileUtils.write(f, content, StandardCharsets.UTF_8);
			} catch (IOException e) {
			  e.printStackTrace();
			}
    }
   

  
}
