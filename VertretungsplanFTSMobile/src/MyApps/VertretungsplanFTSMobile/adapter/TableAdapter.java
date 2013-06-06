package MyApps.VertretungsplanFTSMobile.adapter;

import java.util.List;

import MyApps.VertretungsplanFTSMobile.R;
import MyApps.VertretungsplanFTSMobile.model.TimeTable;
import MyApps.VertretungsplanFTSMobile.model.TimeTableRow;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class TableAdapter {
	private TableLayout table;
	private Context context;
	private LayoutInflater inflater;
	private LayoutParams params;
	
	public TableAdapter(Context pContext, TableLayout pTable){
		table = pTable;
		context = pContext;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	public void update(List<TimeTable> tables){
		table.removeAllViews();
		if(tables.size() > 0){
			addHeader(tables);
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
		View noChanges = inflater.inflate(R.layout.row_no_changes, null);
		table.addView(noChanges);
	}
	
	private void addHeader(List<TimeTable> tables){
		for(TimeTable timeTable:tables){
			TimeTableRow header = timeTable.getHeader();
			if(header != null){
				TableRow tableRow = new TableRow(context);
				tableRow.setLayoutParams(params);
				tableRow.setBackgroundResource(R.color.row_entry_header_background);
				for(String data: header.getRowData()){
					TextView tvHeader = (TextView) inflater.inflate(R.layout.row_header, null);
					tvHeader.setText(data);
					tableRow.addView(tvHeader);
				}
				table.addView(tableRow);
				return;
			}
		}
	}
	
	private void addDate(String date){
		TextView tvDate = (TextView) inflater.inflate(R.layout.row_date, null);
		tvDate.setText(date);
		table.addView(tvDate);
	}
	
	private void addTableRow(TimeTableRow row, boolean even){
		TableRow tableRow = new TableRow(context);
		tableRow.setLayoutParams(params);
		for(String data: row.getRowData()){
			TextView tvEntry;
			if(even){
				tableRow.setBackgroundResource(R.drawable.row_even_background);
				tvEntry = (TextView) inflater.inflate(R.layout.row_even, null);
			}else{
				tableRow.setBackgroundResource(R.drawable.row_odd_background);
				tvEntry = (TextView) inflater.inflate(R.layout.row_odd, null);
			}
			tvEntry.setText(data);
			tableRow.addView(tvEntry);
		}
		
		table.addView(tableRow);
	}
}
