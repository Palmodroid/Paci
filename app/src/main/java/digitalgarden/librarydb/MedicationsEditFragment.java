package digitalgarden.librarydb;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.MedicationsTable;
import digitalgarden.librarydb.formtypes.EditTextField;
import digitalgarden.logger.Logger;

public class MedicationsEditFragment extends GeneralEditFragment
	{
	private EditTextField medicationNameField;

	@Override
	protected Uri getTableContentUri()
		{
		return MedicationsTable.CONTENT_URI;
		}

	@Override
	protected int getFormLayout()
		{
		return 	R.layout.medication_edit_fragment_form;
		}

	@Override
	protected void setupFormLayout( View view )
		{
		Logger.note("MedicationsEditFragment setupFormLayout");
		
        medicationNameField = (EditTextField) view.findViewById( R.id.edittext_medication_name );
        medicationNameField.connect( this );

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
		Logger.note("MedicationsEditFragment setupFieldsData");

		String[] projection = { 
				 MedicationsTable.NAME };
		Cursor cursor = getActivity().getContentResolver().query(getItemContentUri(), projection, null, null, null);

		if (cursor != null) // Ez vajon kell? 
			{
			cursor.moveToFirst();

			medicationNameField.setText(cursor.getString(cursor.getColumnIndexOrThrow(  MedicationsTable.NAME )));

			// Always close the cursor
			cursor.close();
			}
		}

	@Override
	protected ContentValues getFieldsData()
		{
		Logger.note("MedicationsEditFragment getFieldsData");

		String name = medicationNameField.getText().toString();

	    ContentValues values = new ContentValues();
	    values.put( MedicationsTable.NAME, name);
	    
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
