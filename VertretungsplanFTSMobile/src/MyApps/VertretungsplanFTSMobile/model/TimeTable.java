package MyApps.VertretungsplanFTSMobile.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class TimeTable implements Comparable<TimeTable>{
	private List<TimeTableRow> rows = new LinkedList<TimeTableRow>();
	private TimeTableRow header;
	private String date;
	private boolean invalidLogin;
	
	public TimeTable() {}
	
	public TimeTable(Iterator<String> html){
		String buffer;
		while(html.hasNext()){
			buffer = html.next();
			if(buffer.startsWith("<h1>")){
				setDate(html.next());
			}else if(buffer.startsWith("<table class=\"maintable\">")){
				while(html.hasNext()){
					buffer = html.next();
					if(!buffer.startsWith("</table")){
						if(header == null){
							setHeader(new TimeTableRow(html));
						}else{
							addTableRow(new TimeTableRow(html));
						}
					}else{
						return;
					}
				}
			}else if(buffer.startsWith("<TITLE>Invalid Login</TITLE>")){
				setInvalidLogin(true);
				return;
			}
		}
	}	

	public void addTableRow(TimeTableRow row){
		rows.add(row);
	}
	public List<TimeTableRow> getTableRows(){
		return rows;
	}
	public void setHeader(TimeTableRow pHeader){
		header = pHeader;
	}
	public TimeTableRow getHeader(){
		return header;
	}
	public void setDate(String pDate){
		date = pDate;
	}
	public String getDate(){
		return date;
	}
	public boolean isInvalidLogin() {
		return invalidLogin;
	}
	public void setInvalidLogin(boolean pInvalidLogin) {
		invalidLogin = pInvalidLogin;
	}
	
	public Date getParsedDate() throws ParseException{
		if(getDate() != null){
			SimpleDateFormat  format = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
			return format.parse(getDate().substring(getDate().indexOf(" ")));
		}
		return null;
	}
	
	@Override
	public int compareTo(TimeTable another) {
		try {
			Date date1 = getParsedDate();
			Date date2 = another.getParsedDate();
			if(date1 == null || date2 == null){
				return 0;
			}
			return getParsedDate().compareTo(another.getParsedDate());
		} catch (ParseException e) {
			return 0;
		}
	}
}
