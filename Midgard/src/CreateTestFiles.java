import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CreateTestFiles {
	
	private BufferedReader br;

	/**
	 * Constructor
	 */
	public CreateTestFiles(){
		
	}
	
	public HashMap<String, Integer> parseFields(String filePath, boolean header, String delimiter) throws IOException{
		br = new BufferedReader(new FileReader(filePath));
		String[] parsedFields;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		if(header == true) {
			parsedFields = br.readLine().split(delimiter);
			for(int i = 0; i <= parsedFields.length - 1; i++){
				map.put(parsedFields[0], i);
			}
		}
		return map;
	}
	
	public void getParsedFields(String[] parsedFields) {
		for(int i = 0; i <= parsedFields.length - 1; i++) {
			System.out.println(parsedFields[i]);
		}
	}
	
	public HashMap<String, Integer> generateFile(String testFilePath, HashMap<String, Integer> parsedFieldsMap, String testCaseFilePath) throws IOException{
		/** br = new BufferedReader(new FileReader(testCaseFilePath));
		InputStream input = new FileInputStream(testFilePath);
		OutputStream output;
		String currentLineTestCase;
		String currentLineNewFile;
		Iterator it = parsedFieldsMap.entrySet().iterator();

		while((currentLineTestCase = br.readLine()) != null) {
			String[] parsedString = currentLineTestCase.trim().split("=");
			String fieldName = parsedString[0];
			String value = parsedString[1];
			output = new FileOutputStream(fieldName + "_" + value);
			BufferedReader brOutput = new BufferedReader(new FileReader(output.toString()));
			
			while((currentLineNewFile = brOutput.readLine()) != null) {
				while(it.hasNext()) {
					Map.Entry<String, Integer>pair = (Map.Entry)it.next();
					
					currentLineNewFile.replace();
					
					pair.getKey();
					pair.getValue();
				}
			}
		} **/
		br = new BufferedReader(new FileReader(testCaseFilePath));
		HashMap<String, Integer> newMap = new HashMap<String, Integer>();
		HashMap<String, Integer> mapTestCaseFile = new HashMap<String, Integer>();
		Iterator itParsedFieldsMap = parsedFieldsMap.entrySet().iterator();
		Iterator itmapTestCaseFile = parsedFieldsMap.entrySet().iterator();
		
		String[] parsedFields = br.readLine().split("=");
		for(int i = 0; i <= parsedFields.length - 1; i++){
			mapTestCaseFile.put(parsedFields[0], i);
		}
		while(itParsedFieldsMap.hasNext()) {
			Map.Entry<String, Integer>pair = (Map.Entry)itParsedFieldsMap.next();
			String parsedFieldKey = pair.getKey();
			
		}
		
		return newMap;
	}

	public static void main(String args[]) throws IOException{
		CreateTestFiles ctf = new CreateTestFiles();
		String[] parsedFields = ctf.parseFields("C:/Users/david_him/Documents/Projects/AARP/nuance.std.in.20161019David.dat", true, "\\|");
		ctf.getParsedFields(parsedFields);
	}
}
