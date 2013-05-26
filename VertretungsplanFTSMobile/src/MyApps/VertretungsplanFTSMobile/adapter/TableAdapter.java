package MyApps.VertretungsplanFTSMobile.adapter;

import java.util.List;

import MyApps.VertretungsplanFTSMobile.R;
import MyApps.VertretungsplanFTSMobile.model.TimeTable;
import MyApps.VertretungsplanFTSMobile.model.TimeTableRow;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableAdapter {
	private TableLayout table;
	private Context context;
	private LayoutInflater inflater;
	
	public TableAdapter(Context pContext, TableLayout pTable){
		table = pTable;
		context = pContext;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void update(List<TimeTable> tables){
		table.removeAllViews();
		if(tables.size() > 0){
			addHeader(tables.get(0).getHeader());
			for(TimeTable table:tables){
				addDate(table.getDate());
				int i=0;
				for(TimeTableRow row: table.getTableRows()){
					addTableRow(row,i%2 == 0);
					i++;
				}
			}
		}else{
			addNoChanges();
		}
	}
	
	private void addNoChanges(){
		TextView tvDate = (TextView) inflater.inflate(R.layout.row_date, null);
		tvDate.setText("Keine Änderungen");
		table.addView(tvDate);
	}
	
	private void addHeader(TimeTableRow header){
		TableRow tableRow = new TableRow(context);
		for(String data: header.getRowData()){
			TextView tvHeader = (TextView) inflater.inflate(R.layout.row_header, null);
			tvHeader.setText(data);
			tableRow.addView(tvHeader);
		}
		table.addView(tableRow);
	}
	
	private void addDate(String date){
		TextView tvDate = (TextView) inflater.inflate(R.layout.row_date, null);
		tvDate.setText(date);
		table.addView(tvDate);
	}
	
	private void addTableRow(TimeTableRow row, boolean even){
		TableRow tableRow = new TableRow(context);
		for(String data: row.getRowData()){
			TextView tvEntry;
			if(even){
				tvEntry = (TextView) inflater.inflate(R.layout.row_even, null);
			}else{
				tvEntry = (TextView) inflater.inflate(R.layout.row_odd, null);
			}
			tvEntry.setText(data);
			tableRow.addView(tvEntry);
		}
		
		table.addView(tableRow);
	}
}
