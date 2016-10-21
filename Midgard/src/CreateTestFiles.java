import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CreateTestFiles {
	
	private BufferedReader br;

	/**
	 * Constructor
	 */
	public CreateTestFiles(){
		
	}
	
	public HashMap<String, String> parseFields(String filePath, String delimiter) throws IOException{
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getParsedFields(HashMap<String, String> map) {
		Iterator iter = map.entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry<String, String>pair = (Map.Entry)iter.next();
			String key = pair.getKey();
			String value = pair.getValue();
			
			System.out.println("Key:" + key + "\n Value:" + value);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void generateFiles(HashMap<String, String> map, String testCaseFilePath) throws IOException {
		br = new BufferedReader(new FileReader(testCaseFilePath));
		String parsedTestCase;
		
		while((parsedTestCase = br.readLine()) != null) {
			String field = parsedTestCase.split("=")[0];
			String value = parsedTestCase.split("=")[1];
			
			Iterator iter = map.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<String, String>pair = (Map.Entry)iter.next();
				if(pair.getKey().equals(field)){
					System.out.println("Found field \n Old Value:" + pair.getValue());
					pair.setValue(value);
					System.out.println(" New Value:" + pair.getValue());
				}
			}
			
		}
	}

	public static void main(String args[]) throws IOException{
		CreateTestFiles ctf = new CreateTestFiles();
		HashMap<String, String> parsedFields = ctf.parseFields("C:/Users/david_him/Documents/Projects/AARP/nuance.std.in.20161019David.dat", "\\|");
		//ctf.getParsedFields(parsedFields);
		ctf.generateFiles(parsedFields, "C:/Users/david_him/Documents/Projects/AARP/testCase.txt");
	}
}
