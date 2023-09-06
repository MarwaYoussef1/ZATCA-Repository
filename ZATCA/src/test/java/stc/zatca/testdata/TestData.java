// Test Data Reader
// Version: 1.0  
// Date: 10-01-2023
//-----------------------------------
package stc.zatca.testdata;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shaft.tools.io.ReportManager;

public class TestData {
	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;

/**
 * 
 * @param path
 * @param sheetName
 * @param testCaseName
 * @return
 * @throws InvalidFormatException
 * @throws IOException
 * 
 * get testdata for the testcase 
 */

public  static Object[][] fetchData(String path,String testCaseName) throws InvalidFormatException, IOException {
	 
	  Row row = null;
	  int cellCount = 0;
	  Object data[][] = null;
	// Open the Excel file
   
	FileInputStream ExcelFile = new FileInputStream(path);

	// Access the required test data sheet
    
		ExcelWBook = new XSSFWorkbook(ExcelFile);
		ExcelWSheet = ExcelWBook.getSheet("TestCases");

		int rowcount = ExcelWSheet.getLastRowNum();
		ArrayList<Integer> tcRowsList = new ArrayList<Integer>();

		for (int i = 1; i <= rowcount; i++) {
			row = ExcelWSheet.getRow(i);
			if (rowIsEmpty(row))
				break;
			if (row.getCell(0).getStringCellValue().equals(testCaseName)) {
				// header row
				if (row.getCell(1) == null)
					continue;
				// Run mode is false
				if (!(row.getCell(1).getBooleanCellValue()))
					continue;
				cellCount = row.getLastCellNum();
				tcRowsList.add(i);
			}
		}
		if (tcRowsList.size() > 0) {
			data = new Object[tcRowsList.size()][cellCount - 2];
			for (int i = 0; i < tcRowsList.size(); i++) {

				Row r = ExcelWSheet.getRow(tcRowsList.get(i));

				for (int j = 2; j < cellCount; j++) {
					Cell c = r.getCell(j);
					try {

						if (c.getCellType() == CellType.STRING) {
							data[i][j - 2] = c.getStringCellValue();
						} else if (c.getCellType()== CellType.NUMERIC || c.getCellType() == CellType.FORMULA) {
							// Date value
							if (DateUtil.isCellDateFormatted(c)) {
								DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
								Date date = c.getDateCellValue();
								data[i][j - 2] = df.format(date);
							}
							// Numeric value
							else {
								String cellValue = String.valueOf(c.getNumericCellValue());
								if (cellValue.contains(".0")) {
									data[i][j - 2] = cellValue.split("\\.")[0];
								}
							}
						}
						// boolean value
						else if (c.getCellType() == CellType.BOOLEAN) {
							data[i][j - 2] = c.getBooleanCellValue();
						}

					} catch (Exception e) {
						ReportManager.log(e.getMessage());
					}
				}
			}
		}
	return data ;
	 
	
	
}

private static boolean rowIsEmpty(Row row) {
    if (row == null) {
        return true;
    }
   
    for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
        Cell cell = row.getCell(cellNum);
       
        if (cell != null && cell.getCellType()!= CellType.BLANK && StringUtils.isNotBlank(cell.toString())) 
        {
            return false;
        }
       
    }
    return true;
}
}
