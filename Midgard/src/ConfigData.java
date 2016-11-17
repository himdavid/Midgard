
/**
 * Describes a file layout of a fixed-width input file
 * @author David Him
 *
 */
public class ConfigData {
	
	private String fieldName;
	private String value;
	private int beginIndex;
	private int endIndex;
	private String header;
	private String footer;
	
	/**
	 * The constructor
	 * @param fieldName
	 * @param value
	 * @param beginIndex
	 * @param endIndex
	 */
	public ConfigData(String fieldName, int beginIndex, int endIndex, String value){
		this.fieldName = fieldName;
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		this.value = value;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * Get the beginning index
	 * @return
	 */
	public int getBeginIndex() {
		return beginIndex;
	}
	
	/**
	 * Set the ending index
	 * @param beginIndex
	 */
	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
