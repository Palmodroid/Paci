package digitalgarden.librarydb.exportimport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import digitalgarden.librarydb.database.LibraryDatabaseHelper.PillsTable;
import digitalgarden.logger.Logger;
import digitalgarden.utils.StringUtils;

public class PillsTableExportImport extends GeneralTableExportImport
	{
	public PillsTableExportImport(Context context)
		{
		super(context);
		// TODO Auto-generated constructor stub
		}

	@Override
	protected Uri getContentUri()
		{
		return PillsTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		return new String[] { PillsTable.NAME };
		}

	@Override
	protected String[] getRowData(Cursor cursor)
		{
		return new String[] 
			{ cursor.getString( cursor.getColumnIndexOrThrow( PillsTable.NAME )) };
		}

	@Override
	protected String getTableName()
		{
		return PillsTable.TABLENAME;
		}
	
	@Override
	public void importRow(String[] records)
		{
		// Mivel csak egy adat van, hossz ellenőrzése nem szükséges
		
		records[1] = StringUtils.revertFromEscaped( records[1] );
	
		// Uniqe ellenőrzés kódból. Lehetne adatbázis szinten is, hiba ellenőrzésével
		String[] projection = { 
			PillsTable.NAME };
		Cursor cursor = getContentResolver()
			.query(PillsTable.CONTENT_URI, projection,
			PillsTable.NAME + "='" + records[1] + "'", null, null);
		
		// http://stackoverflow.com/a/16108435
		if (cursor == null || cursor.getCount() == 0)
			{
			ContentValues values = new ContentValues();
			values.put( PillsTable.NAME, records[1]);
			
			getContentResolver()
				.insert( PillsTable.CONTENT_URI, values);
			Logger.debug( "Pill [" + records[1] + "] was inserted.");
			}
		else 
			Logger.note( "Pill [" + records[1] + "] already exists! Item was skipped.");
		
		if ( cursor != null )
			cursor.close();
		}

	}
