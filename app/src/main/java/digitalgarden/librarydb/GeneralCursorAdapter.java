package digitalgarden.librarydb;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import digitalgarden.R;


public class GeneralCursorAdapter extends SimpleCursorAdapter
	{
	// Az eddig kiválasztott elemet mutatja, vagy semmit, ha Selected_none v. Select_disbaled
	// (Konstruktorban mindenképp normálértéket kap)
	private long selectedItem;
	
	// Az editálásra kiválasztott elemet mutatja
	private long editedItem = -1L;
	
	// A hátteret ezek segítségével állítjuk be. Három kell, mert különben mindig ugyanazt változtatnánk
	// Context miatt ezt constructorban megszerezzük
	private GradientDrawable backgroundSolid;
	private GradientDrawable backgroundBorder;
	private GradientDrawable backgroundBoth;
	
	// editedItem változása után újra kell rajzolni a listát 
	// ez kikényszeríthető pl. a notifyDataSetChanged paranccsal, 
	// de esetünkben nem kell, mert az editFragment változása miatt listFragment is változik, és újrarajzolja a listát.
	public void setEditedItem(long editedItem)
		{
		this.editedItem = editedItem;
		}
	
	public void clearEditedItem()
		{
		this.editedItem = -1L;
		}
	
	public GeneralCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, long selectedItem)
		{
		super(context, layout, c, from, to, flags);

		this.selectedItem = selectedItem; 
		}

	// http://stackoverflow.com/questions/12310836/custom-simplecursoradapter-with-background-color-for-even-rows
	@SuppressWarnings("deprecation")
	@Override
	public void bindView(View view, Context context, Cursor cursor) 
		{
	    super.bindView(view, context, cursor);


	    if ( getItemId( cursor.getPosition() ) == editedItem && getItemId( cursor.getPosition() ) == selectedItem )
	    	{
	    	if (backgroundBoth == null)
	    		{
	    		backgroundBoth = (GradientDrawable)context.getResources().getDrawable(R.drawable.border_translucent);
	    		backgroundBoth.setColor(0xFF006699);
		    	backgroundBoth.setStroke(3, 0xFFFFA500);
	    		}
	    	view.setBackgroundDrawable( backgroundBoth );
	    	}
	    else if ( getItemId( cursor.getPosition() ) == editedItem )
	    	{
	    	if (backgroundSolid == null)
	    		{
	    		backgroundSolid = (GradientDrawable)context.getResources().getDrawable(R.drawable.border_translucent);
	    		backgroundSolid.setColor(0xFF006699);
		    	backgroundSolid.setStroke(0, 0);
	    		}
	    	view.setBackgroundDrawable( backgroundSolid );
	    	}
	    else if ( getItemId( cursor.getPosition() ) == selectedItem )
	    	{
	    	if (backgroundBorder == null)
	    		{
	    		backgroundBorder = (GradientDrawable)context.getResources().getDrawable(R.drawable.border_translucent);
	    		backgroundBorder.setColor(0);
		    	backgroundBorder.setStroke(3, 0xFFFFA500);
	    		}
	    	view.setBackgroundDrawable( backgroundBorder );
	    	}
	    else
	    	view.setBackgroundResource( 0 );
		}
	}
