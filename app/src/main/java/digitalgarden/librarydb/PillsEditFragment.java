package digitalgarden.librarydb;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PillsTable;
import digitalgarden.librarydb.formtypes.EditTextField;
import digitalgarden.logger.Logger;

public class PillsEditFragment extends GeneralEditFragment
	{
	private EditTextField pillNameField;

	@Override
	protected Uri getTableContentUri()
		{
		return PillsTable.CONTENT_URI;
		}

	@Override
	protected int getFormLayout()
		{
		return 	R.layout.pill_edit_fragment_form;
		}

	@Override
	protected void setupFormLayout( View view )
		{
		Logger.note("PillsEditFragment setupFormLayout");
		
        pillNameField = (EditTextField) view.findViewById( R.id.edittext_pill_name );
        pillNameField.connect( this );

    	/*
		setupListButton( BooksControllActivity.class,
    			getActivity().getString( R.string.button_books_list ), 
    			getActivity().getString( R.string.books_of ),
    			nameField );
    	*/
		}

	@Override
	protected void setupFieldsData(long id)
		{
		Logger.note("PillsEditFragment setupFieldsData");

		String[] projection = { 
				 PillsTable.NAME };
		Cursor cursor = getActivity().getContentResolver().query(getItemContentUri(), projection, null, null, null);

		if (cursor != null) // Ez vajon kell? 
			{
			cursor.moveToFirst();

			pillNameField.setText(cursor.getString(cursor.getColumnIndexOrThrow(  PillsTable.NAME )));

			// Always close the cursor
			cursor.close();
			}
		}

	@Override
	protected ContentValues getFieldsData()
		{
		Logger.note("PillsEditFragment getFieldsData");

		String name = pillNameField.getText().toString();

	    ContentValues values = new ContentValues();
	    values.put( PillsTable.NAME, name);
	    
	    return values;
		}

	@Override
	protected void saveFieldData(Bundle data)
		{
		// Itt csak EditTExt van, azt nem kell elmenteni	
		}

	@Override
	protected void retrieveFieldData(Bundle data)
		{
		// Itt csak EditTExt van, azt nem kell elmenteni
		}

	@Override
	protected void checkReturningSelector(int requestCode, long selectedId)
		{
		// Nincs ForeignKey, ezzel nem kell foglalkoznunk
		}
	}
