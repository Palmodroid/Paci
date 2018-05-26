package digitalgarden.librarydb.exportimport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.AuthorsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.BooksTable;
import digitalgarden.logger.Logger;
import digitalgarden.utils.StringUtils;

public class BooksTableExportImport extends GeneralTableExportImport
	{
	public BooksTableExportImport(Context context)
		{
		super(context);
		// TODO Auto-generated constructor stub
		}

	@Override
	protected Uri getContentUri()
		{
		return BooksTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		return new String[] { 
			AuthorsTable.NAME,
			BooksTable.TITLE };
		}

	@Override
	protected String[] getRowData(Cursor cursor)
		{
		return new String[] { 
			cursor.getString( cursor.getColumnIndexOrThrow( AuthorsTable.NAME )), 
			cursor.getString( cursor.getColumnIndexOrThrow( BooksTable.TITLE )) };
		}

	@Override
	protected String getTableName()
		{
		return BooksTable.TABLENAME;
		}

	@Override
	public void importRow(String[] records)
		{
		// Két adat miatt itt szükséges a hossz ellenőrzése
		if ( records.length < 3 )
			{
			Logger.note( "Parameters missing from BOOKS row. Item was skipped.");
			return;
			}
		
		long authorId = findAuthorId( records[1] );
		if ( authorId == ID_MISSING )
			{
			Logger.note( "Author [" + records[1] + "] does not exists! Item was skipped.");
			return;
			}

		ContentValues values = new ContentValues();
		
		if ( authorId == ID_NULL )
    		values.putNull( BooksTable.AUTHOR_ID );
		else
    		values.put( BooksTable.AUTHOR_ID, authorId );
		
		records[2] = StringUtils.revertFromEscaped( records[2] );								
		values.put( BooksTable.TITLE, records[2] );
				
		getContentResolver()
			.insert( BooksTable.CONTENT_URI, values);
		Logger.debug( "Book [" + records[2] + "] was inserted.");
		}

	
	private long ID_MISSING = -2L;
	private long ID_NULL = -1L;
	
	// Ezt a keresőrutint nehéz generalizálni, mert az azonosító paraméterek típusa is különböző lehet
	private long findAuthorId(String authorName)
		{
		if ( authorName == null )
			{
    		return ID_NULL;
			}

		long authorId = ID_MISSING;
		
		String[] projection = {
			AuthorsTable._ID,	
			AuthorsTable.NAME };
		Cursor cursor = getContentResolver()
			.query(AuthorsTable.CONTENT_URI, projection, 
				   AuthorsTable.NAME + "=\'" + StringUtils.revertFromEscaped( authorName ) + "\'", 
				   null, null);

		if ( cursor != null)
			{
			if (cursor.moveToFirst())
				authorId = cursor.getLong( cursor.getColumnIndexOrThrow( AuthorsTable._ID ) );								
			cursor.close();
			}
		
		return authorId;
		}
	}
