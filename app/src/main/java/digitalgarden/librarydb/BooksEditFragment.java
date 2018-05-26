package digitalgarden.librarydb;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.AuthorsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.BooksTable;
import digitalgarden.librarydb.formtypes.EditTextField;
import digitalgarden.librarydb.formtypes.ForeignKey;
import digitalgarden.librarydb.formtypes.ForeignTextField;
import digitalgarden.logger.Logger;

public class BooksEditFragment extends GeneralEditFragment
	{
	private EditTextField editTextTitle;
	private ForeignKey authorId = new ForeignKey( AuthorsTable.CONTENT_URI ); 
	// Míg a címet a szöveg azonosítja, a szerzőt az id
	// Nem lehet null, ezért -1 azonosítja a null-t Ezt ellenőrizni kell!!
	private ForeignTextField foreignTextAuthor;
	
	
	@Override
	protected Uri getTableContentUri()
		{
		return BooksTable.CONTENT_URI;
		}

	@Override
	protected int getFormLayout()
		{
		return R.layout.book_edit_fragment_form;
		}
	
	@Override
	protected void setupFormLayout( View view )
		{
		Logger.note("BooksEditFragment setupFormLayout");

		// EditTextField
        editTextTitle = (EditTextField) view.findViewById( R.id.edittext_title );
        editTextTitle.connect( this );
		
        // ForeignKey
        authorId.connect( this );
        authorId.setupSelector( AuthorsControllActivity.class,
        		getActivity().getString( R.string.select_author ),
        		editTextTitle );
        
        // ForeignTextField
       	foreignTextAuthor = (ForeignTextField) view.findViewById(R.id.edittext_author);
        foreignTextAuthor.link( authorId, AuthorsTable.NAME );
		}

	@Override
	protected void setupFieldsData(long id)
		{
		Logger.note("BooksEditFragment setupFieldsData");

		String[] projection = { 
			BooksTable.AUTHOR_ID,
			BooksTable.TITLE };
		Cursor cursor = getActivity().getContentResolver().query(getItemContentUri(), projection, null, null, null);

		if (cursor != null) // Ez vajon kell? 
			{
			cursor.moveToFirst();

			int column = cursor.getColumnIndexOrThrow( BooksTable.AUTHOR_ID );
			if ( cursor.isNull( column ) )
				authorId.setValue( -1L );
			else
				authorId.setValue( cursor.getLong( column ) );
			editTextTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(  BooksTable.TITLE )));

			// Always close the cursor
			cursor.close();
			}
		}

	@Override
	protected ContentValues getFieldsData()
		{
		Logger.note("BooksEditFragment getFieldsData");
		
		String title = editTextTitle.getText().toString();

	    ContentValues values = new ContentValues();
	    if (authorId.getValue() >= 0)
	    	values.put( BooksTable.AUTHOR_ID, authorId.getValue());
	    else
	    	values.putNull( BooksTable.AUTHOR_ID );
	    values.put( BooksTable.TITLE, title);

	    return values;
		}

	@Override
	protected void saveFieldData(Bundle data)
		{
		data.putLong("AUTHOR_ID", authorId.getValue() );
		}

	@Override
	protected void retrieveFieldData(Bundle data)
		{
		authorId.setValue( data.getLong( "AUTHOR_ID" ) );
		}

	@Override
	protected void checkReturningSelector(int requestCode, long selectedId)
		{
		authorId.checkReturningSelector( requestCode, selectedId );
		Logger.note("Author id: " + selectedId + " was selected in onActivityResult");
		}
	}
