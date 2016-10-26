import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	 * Given a flat file with a header and single row of data, the fields and values will be separated
	 * based on specified delimiter. A LinkedHashMap will be returned, which is the first step in creating test files.
	 * @param filePath, the absolute file path to the flat file. (ie C:/user/david_him/project/test_file.dat)
	 * @param delimiter, the delimiter within the file. (ie: |,"")
	 * @return map, the LinkedHashMap.
	 * @throws IOException
	 */
	public HashMap<String, ColumnData> parseFields(String filePath, String delimiter) throws IOException{

		this.delimiter = delimiter;

		br = new BufferedReader(new FileReader(filePath));
		String parsedFields;
		String[] column = null;
		String[] value = null;
		int line = 0;
		LinkedHashMap<String, ColumnData> map = new LinkedHashMap<String,ColumnData>();

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

			ColumnData cd = new ColumnData(column[i], value[i], i, false);

			map.put(column[i], cd);
		}
		return map;
	}

	/**
	 * Given a LinkedHashMap and a file containing test cases, if the key in the LinkedHashMap matches the field name
	 * in the test case file, the value in the LinkedHashMap will be updated along with setting the createFile flat to True.
	 * @param map, the LinkedHashMap from the parsed flat file
	 * @param testCaseFilePath, the absolute path to the file containing your test cases.
	 * @return map, an updated LinkedHashMap with the updated test data
	 * @throws IOException
	 */
	public HashMap<String, ColumnData>updateFieldValue(HashMap<String, ColumnData> map, String testCaseFilePath) throws IOException {
		br = new BufferedReader(new FileReader(testCaseFilePath));
		String parsedTestCase;

		while((parsedTestCase = br.readLine()) != null) {
			String field = parsedTestCase.split("=")[0];
			String value = parsedTestCase.split("=")[1];

			for(Map.Entry<String, ColumnData> entry: map.entrySet()){
				String key = entry.getKey();
				ColumnData cd = entry.getValue();
				/** String fieldName = cd.getFieldName();
				int fieldIndex = cd.getIndex();
				String fieldValue = cd.getValue(); **/

				if(key.equals(field)){
					System.out.println("Found field \n" + " Field Name:" + cd.getFieldName() +  "\n Index:" + cd.getIndex() + "\n Old Value:" + cd.getValue());
					cd.setValue(value);
					cd.setCreateFile(true);
					entry.setValue(cd);
					System.out.println(" New Value:" + cd.getValue());
				}
			}
		}
		return map;
	}

	/**
	 * Given the path of the base line file(flat file), the updated LinkedHashMap, and a file name format,
	 * generate individual test files where the LinkedHashMap contain a value of createFile == true.
	 * @param baseLineFilePath, the absolute path to the base line file (flat file).
	 * @param map, the updated LinkedHashMap
	 * @param fileNameFormat, the expected file format.
	 * @throws IOException
	 */
	public void outputTestFiles(String baseLineFilePath, HashMap<String, ColumnData> map, String fileNameFormat) throws IOException{
		
		File baseLineFile = new File(baseLineFilePath);
		FileInputStream fips;
		FileOutputStream fops1;
		FileOutputStream fops2;

		for(Map.Entry<String, ColumnData> entry: map.entrySet()){
			//String key = entry.getKey();
			ColumnData cd = entry.getValue();
			String fieldName = cd.getFieldName();
			int index = cd.getIndex();
			String value = cd.getValue();
			boolean createFile = cd.getCreateFile();

			String absolutePath = baseLineFile.getAbsolutePath();
			String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(baseLineFile.separator))+"/";

			if(createFile == true) {
				File tempFile = new File(filePath+"tempfile.dat");
				File testFile = new File(filePath+fileNameFormat.replace("[FieldName]", fieldName).replace("[Value]", value));
				fips = new FileInputStream(baseLineFile);
				fops1 = new FileOutputStream(tempFile);
				fops2 = new FileOutputStream(testFile);

				System.out.println(tempFile.getAbsolutePath());
				
				byte[] buffer = new byte[1024];
				int length;
				while((length = fips.read(buffer)) > 0) {
					fops1.write(buffer, 0, length);
				}
				
				BufferedReader br1 = new BufferedReader(new FileReader(tempFile.getAbsolutePath()));
				String parsedFields;
				int line = 0;
				String[] outputValues = null;
				
				while((parsedFields = br1.readLine()) != null) {
					
					if(line == 1) {
						outputValues = parsedFields.split(delimiter);
						outputValues[index] = value;
					} else {
					}
					line++;
				}

				for(int i = 0; i < outputValues.length; i++){
					String valueToWrite = outputValues[i] + "|";
					fops1.write(valueToWrite.getBytes());
				}
				
				BufferedReader br2 = new BufferedReader(new FileReader(tempFile.getAbsolutePath()));
				
				int lineCount=1;
				String content;
				while((content = br2.readLine()) != null){
					if(lineCount != 2) {
						content = content+"\n";
						fops2.write(content.getBytes());
					}
					lineCount++;
				}
				fops1.close();
				fops2.close();
				br1.close();
				br2.close();
				System.out.println(tempFile.delete());
				
			}
		}
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
		HashMap<String, ColumnData> parsedFields = ctf.parseFields("C:/Users/david_him/Documents/Projects/AARP/nuance.std.in.20161019David.dat", "\\|");
		HashMap<String, ColumnData> updatedMap = ctf.updateFieldValue(parsedFields, "C:/Users/david_him/Documents/Projects/AARP/testCase.txt");
		ctf.outputTestFiles("C:/Users/david_him/Documents/Projects/AARP/nuance.std.in.20161019David.dat", updatedMap, "nuance.std.in."+ todaysDate + "_[FieldName]_[Value].dat");
	}
}
