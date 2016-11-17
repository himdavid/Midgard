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
	public HashMap<String, ConfigData> parseFieldsFixedWidth(String baseLineFile, String configFile, boolean header, boolean footer) throws IOException{
		
		BufferedReader br1 = new BufferedReader(new FileReader(baseLineFile));
		BufferedReader br2 = new BufferedReader(new FileReader(configFile));
		
		String parsedBaseLineFile;
		String parsedBaseLineFileValue = null;
		String parsedConfigFile;
		String configFieldName;
		String[] configFields;
		int configBeginIndex;
		int configEndIndex;
		String baseLineValue;
		LinkedHashMap<String, ConfigData> map = new LinkedHashMap<String,ConfigData>();

		int countLineBaseLineFile = 0;
		while((parsedBaseLineFile = br1.readLine()) != null){
			if(header == true && footer == true && countLineBaseLineFile == 1){
				parsedBaseLineFileValue = parsedBaseLineFile;
			}
			if(header == true && footer == false && countLineBaseLineFile == 1){
				parsedBaseLineFileValue = parsedBaseLineFile;
			}
			if(header == false && footer == true && countLineBaseLineFile == 0){
				parsedBaseLineFileValue = parsedBaseLineFile;
			}
			if(header == false && footer == false && countLineBaseLineFile == 0){
				parsedBaseLineFileValue = parsedBaseLineFile;
			}
			countLineBaseLineFile++;
		}
		
		while((parsedConfigFile = br2.readLine()) != null){
			
			configFields = parsedConfigFile.split("\\t");
			
			configFieldName = configFields[0];
			configBeginIndex = Integer.parseInt(configFields[1]);
			configEndIndex = Integer.parseInt(configFields[2]);
			baseLineValue = parsedBaseLineFileValue.substring(configBeginIndex - 1, configEndIndex);
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
	public HashMap<String, ConfigData>updateFieldValueFixedWidth(HashMap<String, ConfigData> map, List<String> testCase) throws IOException {

		for(int i = 0; i < testCase.size(); i++){
			String field = testCase.get(i).split("=")[0];
			String value = testCase.get(i).split("=")[1].replaceAll("\"", "");
			System.out.println("Found field \n" + " Field Name:" + value + "\n Old Value:" + map.get(field).getValue());
			ConfigData cd = map.get(field);
			int beginIndex = cd.getBeginIndex();
			int endIndex = cd.getEndIndex();
			
			if(value.length() != endIndex - beginIndex - 1){
				System.out.println("Not enough characters in test case");
			} else {
				
			}
			cd.setValue(value);
			map.put(field, cd);
			System.out.println(" New Value:" + map.get(field).getValue());
			}
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
	 * Generate individual test files using a config file that describes each field and its beginning and ending index. 
	 * @param testCaseFilePath, the path to the test case file
	 * @param testDataFilePath, the path to the test data file
	 * @param configFilePath, the path to the configuration file
	 * @param fileNameFormat, the expected file name
	 * @param header, true if the test data file contains a header
	 * @param footer, true if the test data file contains a footer
	 * @return testFile, the File object representing the final output file
	 * @throws IOException
	 */
	public File createOutputFixedWidth(String testCaseFilePath, String testDataFilePath, String configFilePath, String fileNameFormat,
			boolean header, boolean footer) throws IOException{

		List<String> testCasesLists = new ArrayList<String>();
		File testFile = null;

		BufferedReader br = new BufferedReader(new FileReader(testCaseFilePath));
		String content = null;
		String filePath = testCaseFilePath.substring(0, testCaseFilePath.lastIndexOf("/")) + "/";

		while((content = br.readLine()) != null){
			if(content.contains(",")){
				testCasesLists = new ArrayList<String>(Arrays.asList(content.split(",")));
			} else {
				testCasesLists.add(content);
			}
			
			HashMap<String, ConfigData> map = parseFieldsFixedWidth(testDataFilePath,configFilePath, header, footer);
			HashMap<String, ConfigData> updatedMap = updateFieldValueFixedWidth(map, testCasesLists);
			
			//Set the file name, trim if the total path is > 260 characters.
			String fileName = filePath + fileNameFormat.replace("[FieldName]", content.replace("\"", ""));
			fileName = fileName.substring(0, Math.min(fileName.length(), 260));
			testFile = new File(fileName);

			File tempFileValue = new File(filePath + "tempValue.dat");
			FileOutputStream fopsFinalFile = new FileOutputStream(testFile);
			FileOutputStream fopsValue = new FileOutputStream(tempFileValue);
			System.out.println(testFile.getAbsolutePath());
			System.out.println("file length is: " + testFile.getAbsolutePath().length());
			
			for(Map.Entry<String, ConfigData> entry: updatedMap.entrySet()){
				String field = entry.getKey();
				String value = entry.getValue().getValue();

				fopsValue.write(value.getBytes());
				System.out.println("Field:" + field + " \n Value:" + value);
			}
			
			if(header == true) {
				File tempFileHeader = new File(filePath + "tempHeader.dat");
				FileOutputStream fopsHeader = new FileOutputStream(tempFileHeader);
				fopsHeader.write(getHeader(testDataFilePath).getBytes());
				BufferedReader brHeader = new BufferedReader(new FileReader(tempFileHeader));
				String copyHeader = brHeader.readLine() + "\n";
				fopsFinalFile.write(copyHeader.getBytes());
				fopsHeader.close();
				brHeader.close();
				tempFileHeader.delete();
			}
			
			fopsValue.close();
			BufferedReader brValue = new BufferedReader(new FileReader(tempFileValue));
			String copyValue = brValue.readLine();
			fopsFinalFile.write(copyValue.getBytes());
			brValue.close();
			tempFileValue.delete();

			if(footer == true) {
				File tempFileFooter = new File(filePath + "tempFooter.dat");
				FileOutputStream fopsFooter = new FileOutputStream(tempFileFooter);
				fopsFooter.write(getFooter(testDataFilePath, header).getBytes());
				BufferedReader brFooter = new BufferedReader(new FileReader(tempFileFooter));
				String copyFooter = "\n" + brFooter.readLine();
				fopsFinalFile.write(copyFooter.getBytes());
				fopsFooter.close();
				brFooter.close();
				tempFileFooter.delete();
			}
			testCasesLists.clear();
			fopsFinalFile.close();
		}
		br.close();
		return testFile;
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
		String content = null;
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
	 * Get the file header
	 * @param baseLineFile, path to the baseline file
	 * @return header, the header string
	 * @throws IOException
	 */
	public String getHeader(String baseLineFile) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(baseLineFile));
		String parsedBaseLine;
		String header = null;
		
		int countLine = 0;
		while((parsedBaseLine = br.readLine()) != null){

			if(countLine == 0){
				header = parsedBaseLine;
			}
			countLine++;
		}
		br.close();
		return header;
	}
	
	/**
	 * Get the file footer
	 * @param baseLineFile, path to the baseline file
	 * @param header, indicate if the file contains a header
	 * @return header, the footer string
	 * @throws IOException
	 */
	public String getFooter(String baseLineFile, boolean header) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(baseLineFile));
		String parsedBaseLine;
		String footer = null;
		
		int countLine = 0;
		while((parsedBaseLine = br.readLine()) != null){

			if(countLine == 2 && header == true){
				footer = parsedBaseLine;
			}
			if(countLine == 1 && header == false){
				footer = parsedBaseLine;
			}
			countLine++;
		}
		br.close();
		
		return footer;
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
		//ctf.createOutput("C:/Users/david_him/Documents/Projects/AARP/testCase.txt", "C:/Users/david_him/Documents/Projects/AARP/nuance.std.in.20161019David.dat", "\\|", "nuance.std.in."+ todaysDate + "_[FieldName].dat");
		//ctf.createOutputFixedWidth("C:/Users/david_him/Documents/Projects/ADS/testCase.txt", "C:/Users/david_him/Documents/Projects/ADS/PROD.CM.DIALER.AUTO.NUANCE_header_trailer", "C:/Users/david_him/Documents/Projects/ADS/config.txt", "nuance.std.in."+ todaysDate + "_[FieldName].dat", true, true);
		ctf.createOutputFixedWidth("C:/Users/david_him/Documents/Projects/ADS/testCase.txt", "C:/Users/david_him/Documents/Projects/ADS/PROD.CM.DIALER.AUTO.NUANCE.20161026", "C:/Users/david_him/Documents/Projects/ADS/config.txt", "nuance.std.in."+ todaysDate + "_[FieldName].dat", false, false);

	}
}
