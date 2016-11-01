import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the process of accepting a baseline file and creating individual
 * test files that can be used to generate a phone call. 
 * @author David Him
 *
 */
public class CreateTestFiles {
	
	private BufferedReader br;
	private String delimiter;

	/**
	 * Constructor
	 */
	public CreateTestFiles(){
		
	}

	/**
	 * Given a flat file with a header and single row of data, fields and values will be separated
	 * based on the specified delimiter. A LinkedHashMap will be returned with the fields as the key.
	 * @param filePath, the absolute file path to the flat file. (ie C:/user/david_him/project/test_file.dat)
	 * @param delimiter, the delimiter within the file. (ie: |,"")
	 * @return map, the LinkedHashMap.
	 * @throws IOException
	 */
	public HashMap<String, String> parseFieldsDelimited(String filePath, String delimiter) throws IOException{

		this.delimiter = delimiter;
		
		br = new BufferedReader(new FileReader(filePath));
		String parsedFields;
		String[] column = null;
		String[] value = null;
		int line = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String,String>();

		while((parsedFields = br.readLine()) != null) {

			if(line == 0) {
				column = parsedFields.split(delimiter);
			}
			else if(line == 1) {
				value = parsedFields.split(delimiter);
			}
			line++;
		}
		for(int i = 0; i <= column.length - 1; i++) {
			map.put(column[i], value[i]);
		}
		return map;
	}
	
	/**
	 * Parse a fixed width baseline and configuration file for processing.
	 * @param baseLineFile
	 * @param configFile
	 * @return
	 * @throws IOException
	 */
	public HashMap<String, ConfigData> parseFieldsFixedWidth(String baseLineFile, String configFile) throws IOException{
		
		BufferedReader br1 = new BufferedReader(new FileReader(baseLineFile));
		BufferedReader br2 = new BufferedReader(new FileReader(configFile));
		
		String parsedBaseLineFile;
		String parsedConfigFile;
		String configFieldName;
		String[] configFields;
		int configBeginIndex;
		int configEndIndex;
		String baseLineValue;
		LinkedHashMap<String, ConfigData> map = new LinkedHashMap<String,ConfigData>();
		
		parsedBaseLineFile = br1.readLine();
		
		
		while((parsedConfigFile = br2.readLine()) != null){
			
			configFields = parsedConfigFile.split("\\t");
			
			configFieldName = configFields[0];
			configBeginIndex = Integer.parseInt(configFields[1]);
			configEndIndex = Integer.parseInt(configFields[2]);
			baseLineValue = parsedBaseLineFile.substring(configBeginIndex, configEndIndex);
			ConfigData cd = new ConfigData(configFieldName, configBeginIndex, configEndIndex, baseLineValue);
			
			map.put(configFieldName, cd);
		}
		
		br1.close();
		br2.close();
		return map;
	}

	/**
	 * Given a LinkedHashMap and a List of test cases, if the key in the LinkedHashMap matches the field name
	 * of the test case, the value in the LinkedHashMap will be updated. The LinkedHashMap will be returned.
	 * @param map, the LinkedHashMap<String, String> from the parsed flat file
	 * @param testCase, the List<String> containing unparsed test cases
	 * @return map, an updated LinkedHashMap with the updated test data
	 * @throws IOException
	 */
	public HashMap<String, String>updateFieldValue(HashMap<String, String> map, List<String> testCase) throws IOException {

		for(int i = 0; i < testCase.size(); i++){
			String field = testCase.get(i).split("=")[0];
			String value = testCase.get(i).split("=")[1].replaceAll("\"", "");
			System.out.println("Found field \n" + " Field Name:" + field + "\n Old Value:" + map.get(field));
			map.put(field, value);
			System.out.println(" New Value:" + map.get(field));
			}
		return map;
	}
	
	/**
	 * Given the path of the baseline file(flat file), the updated LinkedHashMap, delimiter, and a file name format,
	 * generate individual test files.
	 * @param testCaseFilePath, the absolute path to the test cases file.
	 * @param testDataFilePath, the absolute path to the test data file.
	 * @param fileNameFormat, the expected file name format.
	 * @throws IOException
	 */
	public File createOutput(String testCaseFilePath, String testDataFilePath, String delimiter, String fileNameFormat) throws IOException{
		
		List<String> testCasesLists = null;
		File testFile = null;
		
		BufferedReader br = new BufferedReader(new FileReader(testCaseFilePath));
		String content;
		String filePath = testCaseFilePath.substring(0, testCaseFilePath.lastIndexOf("/")) + "/";

		while((content = br.readLine()) != null){
			if(content.contains(",")){
				testCasesLists = new ArrayList<String>(Arrays.asList(content.split(",")));
			} else {
				testCasesLists.add(content);
			}
			HashMap<String, String> map = parseFieldsDelimited(testDataFilePath, delimiter);
			HashMap<String, String> updatedMap = updateFieldValue(map, testCasesLists);
			
			//Set the file name, trim if the total path is > 260 characters.
			String fileName = filePath + fileNameFormat.replace("[FieldName]", content.replace("\"", ""));
			fileName = fileName.substring(0, Math.min(fileName.length(), 260));
			testFile = new File(fileName);

			File tempFile = new File("temp.dat");
			FileOutputStream fops1 = new FileOutputStream(testFile);
			FileOutputStream fops2 = new FileOutputStream(tempFile);
			System.out.println(testFile.getAbsolutePath());
			System.out.println("file length is: " + testFile.getAbsolutePath().length());
			
			for(Map.Entry<String, String> entry: updatedMap.entrySet()){
				String field = entry.getKey() + "|";
				String value = entry.getValue()+ "|";
				fops1.write(field.getBytes());
				fops2.write(value.getBytes());
			}
			testCasesLists.clear();
			BufferedReader br2 = new BufferedReader(new FileReader(tempFile));
			String copyString = "\n" + br2.readLine();
			fops1.write(copyString.getBytes());
			fops1.close();
			fops2.close();
			br2.close();
			tempFile.delete();
		}
		br.close();
		return testFile;
	}
	/**
	 * Set the delimiter character
	 * @param delimiter, the delimiter
	 */
	public void setDelimiter(String delimiter){
		this.delimiter = delimiter;
	}

	/**
	 * Get the delimiter character
	 * @return delimiter, the delimiter
	 */
	public String getDelimiter(){
		return delimiter;
	}

	public static void main(String args[]) throws IOException{
		CreateTestFiles ctf = new CreateTestFiles();
		Time time = new Time();
		String todaysDate = time.getTodaysDate("yyyyMMdd");
		ctf.createOutput("C:/Users/david_him/Documents/Projects/AARP/testCase.txt", "C:/Users/david_him/Documents/Projects/AARP/nuance.std.in.20161019David.dat", "\\|", "nuance.std.in."+ todaysDate + "_[FieldName].dat");
	}
}
