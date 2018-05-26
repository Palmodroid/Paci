package digitalgarden.librarydb.exportimport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.AuthorsTable;
import digitalgarden.logger.Logger;
import digitalgarden.utils.StringUtils;

public class AuthorsTableExportImport extends GeneralTableExportImport
	{
	public AuthorsTableExportImport(Context context)
		{
		super(context);
		// TODO Auto-generated constructor stub
		}

	@Override
	protected Uri getContentUri()
		{
		return AuthorsTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		return new String[] { AuthorsTable.NAME };
		}

	@Override
	protected String[] getRowData(Cursor cursor)
		{
		return new String[] 
			{ cursor.getString( cursor.getColumnIndexOrThrow( AuthorsTable.NAME )) };
		}

	@Override
	protected String getTableName()
		{
		return AuthorsTable.TABLENAME;
		}
	
	@Override
	public void importRow(String[] records)
		{
		// Mivel csak egy adat van, hossz ellenőrzése nem szükséges
		
		records[1] = StringUtils.revertFromEscaped( records[1] );
	
		// Uniqe ellenőrzés kódból. Lehetne adatbázis szinten is, hiba ellenőrzésével
		String[] projection = { 
			AuthorsTable.NAME };
		Cursor cursor = getContentResolver()
			.query(AuthorsTable.CONTENT_URI, projection, 
			AuthorsTable.NAME + "='" + records[1] + "'", null, null);
		
		// http://stackoverflow.com/a/16108435
		if (cursor == null || cursor.getCount() == 0)
			{
			ContentValues values = new ContentValues();
			values.put( AuthorsTable.NAME, records[1]);
			
			getContentResolver()
				.insert( AuthorsTable.CONTENT_URI, values);
			Logger.debug( "Author [" + records[1] + "] was inserted.");
			}
		else 
			Logger.note( "Author [" + records[1] + "] already exists! Item was skipped.");
		
		if ( cursor != null )
			cursor.close();
		}

	}
