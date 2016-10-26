
public class ColumnData {
	
	private String fieldName;
	private int index;
	private String value;
	private boolean createFile;

	public ColumnData(String fieldName, String value, int index, boolean createFile){
		this.fieldName = fieldName;
		this.index = index;
		this.value = value;
		this.createFile = createFile;
	}

	public boolean getCreateFile() {
		return createFile;
	}

	public void setCreateFile(boolean createFile) {
		this.createFile = createFile;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
