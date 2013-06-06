package MyApps.VertretungsplanFTSMobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;

public class UniScrollTableLayout extends TableLayout{
	private MyHorizontalScrollView hScroll;
	private MyVerticalScrollView vScroll;
	private int currentX,currentY;
	private TableLayout table;
	
	public UniScrollTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		hScroll = new MyHorizontalScrollView(context);
		vScroll = new MyVerticalScrollView(context);
		table = new TableLayout(context);

		hScroll.setFillViewport(true);

		hScroll.addView(vScroll);
		vScroll.addView(table);
		super.addView(hScroll);
	}

	@Override
	public void addView(View child){
		table.addView(child);
	}
	@Override
	public void removeAllViews(){
		table.removeAllViews();
	}
	
	@Override 
	public boolean onTouchEvent(final MotionEvent event) {
		switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN: {
		    	currentX = (int) event.getRawX();
	            currentY = (int) event.getRawY();
	            break;
	        }
	        case MotionEvent.ACTION_MOVE: {
	            int x2 = (int) event.getRawX();
	            int y2 = (int) event.getRawY();
	            vScroll.scrollBy(currentX - x2 , currentY - y2);
	            hScroll.scrollBy(currentX - x2 , currentY - y2);
	            currentX = x2;
	            currentY = y2;
	            break;
	        }
		}
		return true; 
  	}
	
	class MyHorizontalScrollView extends HorizontalScrollView{
		public MyHorizontalScrollView(Context context) {
			super(context);
		}
		@Override
	    public boolean onTouchEvent(MotionEvent ev) {
	        return false;
	    }
	}
	class MyVerticalScrollView extends ScrollView{
		public MyVerticalScrollView(Context context) {
			super(context);
		}
		@Override
	    public boolean onTouchEvent(MotionEvent ev) {
	        return false;
	    }
	}
}
