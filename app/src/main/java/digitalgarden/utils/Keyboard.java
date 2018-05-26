package digitalgarden.utils;

import android.app.*;
import android.view.*;
import android.view.inputmethod.*;
import digitalgarden.logger.*;

public class Keyboard
	{
	// http://stackoverflow.com/a/17789187; többi is jó lehet, ez elsőre is működött 
    public static void hide(Activity activity) 
		{
		if (activity == null)
			{	
			Logger.note("Hide-keyboard: activity is missing!!");
			return;
			}

		//start with an 'always hidden' command for the activity's window
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
		| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		Logger.note("Hide-keyboard: SOFT_INPUT_STATE_ALWAYS_HIDDEN");

		//now tell the IMM to hide the keyboard FROM whatever has focus in the activity
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		View cur_focus = activity.getCurrentFocus();
		if(cur_focus != null)
			{
			inputMethodManager.hideSoftInputFromWindow(cur_focus.getWindowToken(), 0);
			Logger.note("Hide-keyboard: hideSoftInputFromWindow");
			}
		}     
	}
