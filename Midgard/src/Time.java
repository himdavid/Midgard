import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
	
	public Time(){
		
	}
	
	public String getTodaysDate(String format){
		Date date = new Date();
		String todaysDate = new SimpleDateFormat(format).format(date);
		
		return todaysDate;
	}
}
