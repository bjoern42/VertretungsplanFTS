package MyApps.VertretungsplanFTSMobile.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TimeTableRow {
	private List<String> rowData = new LinkedList<String>();
	
	public TimeTableRow(Iterator<String> html){
		String buffer;
		while(html.hasNext()){
			buffer = html.next();
			if(buffer.startsWith("</tr")){
				return;
			}
			if(buffer.startsWith("<td")){
				StringBuilder entry = new StringBuilder();
				while(!(buffer = html.next()).startsWith("</td")){
					if(!buffer.startsWith("&nbsp")){
						entry.append(buffer);
					}
				}
				addRowData(entry.toString());
			}
		}
	}
	
	private void addRowData(String data){
		rowData.add(data);
	}
	public List<String> getRowData(){
		return rowData;
	}
	
	public String getGrades(){
		return getRowData().get(0);
	}
}
