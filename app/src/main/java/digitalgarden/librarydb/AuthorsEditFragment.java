package digitalgarden.librarydb;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.AuthorsTable;
import digitalgarden.librarydb.formtypes.EditTextField;
import digitalgarden.logger.Logger;

public class AuthorsEditFragment extends GeneralEditFragment
	{
	private EditTextField nameField;

	@Override
	protected Uri getTableContentUri()
		{
		return AuthorsTable.CONTENT_URI;
		}

	@Override
	protected int getFormLayout()
		{
		return 	R.layout.author_edit_fragment_form;
		}

	@Override
	protected void setupFormLayout( View view )
		{
		Logger.note("AuthorsEditFragment setupFormLayout");
		
        nameField = (EditTextField) view.findViewById( R.id.edittext_name );
        nameField.connect( this );

    	setupListButton( BooksControllActivity.class, 
    			getActivity().getString( R.string.button_books_list ), 
    			getActivity().getString( R.string.books_of ),
    			nameField );
		}

	@Override
	protected void setupFieldsData(long id)
		{
		Logger.note("AuthorsEditFragment setupFieldsData");

		String[] projection = { 
				 AuthorsTable.NAME };
		Cursor cursor = getActivity().getContentResolver().query(getItemContentUri(), projection, null, null, null);

		if (cursor != null) // Ez vajon kell? 
			{
			cursor.moveToFirst();

			nameField.setText(cursor.getString(cursor.getColumnIndexOrThrow(  AuthorsTable.NAME )));

			// Always close the cursor
			cursor.close();
			}
		}

	@Override
	protected ContentValues getFieldsData()
		{
		Logger.note("AuthorsEditFragment getFieldsData");

		String name = nameField.getText().toString();

	    ContentValues values = new ContentValues();
	    values.put( AuthorsTable.NAME, name);
	    
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
