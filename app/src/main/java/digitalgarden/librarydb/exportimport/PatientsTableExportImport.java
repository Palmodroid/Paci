package digitalgarden.librarydb.exportimport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import digitalgarden.librarydb.database.LibraryDatabaseHelper;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PatientsTable;
import digitalgarden.logger.Logger;
import digitalgarden.utils.StringUtils;

public class PatientsTableExportImport extends GeneralTableExportImport
	{
	public PatientsTableExportImport(Context context)
		{
		super(context);
		// TODO Auto-generated constructor stub
		}

	@Override
	protected Uri getContentUri()
		{
		return LibraryDatabaseHelper.PatientsTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		return new String[] {
				PatientsTable.NAME,
				PatientsTable.DOB,
				PatientsTable.TAJ,
				PatientsTable.PHONE,
				PatientsTable.NOTE };
		}

	@Override
	protected String[] getRowData(Cursor cursor)
		{
		return new String[] {
				cursor.getString( cursor.getColumnIndexOrThrow( PatientsTable.NAME )),
				cursor.getString( cursor.getColumnIndexOrThrow( PatientsTable.DOB )),
				cursor.getString( cursor.getColumnIndexOrThrow( PatientsTable.TAJ )),
				cursor.getString( cursor.getColumnIndexOrThrow( PatientsTable.PHONE )),
				cursor.getString( cursor.getColumnIndexOrThrow( PatientsTable.NOTE )) };
		}

	@Override
	protected String getTableName()
		{
		return PatientsTable.TABLENAME;
		}
	
	@Override
	public void importRow(String[] records)
		{
        // Több adat miatt itt szükséges a hossz ellenőrzése
        if ( records.length < 6 )
            {
            Logger.note( "Parameters missing from PATIENTS row. Item was skipped.");
            return;
            }

        ContentValues values = new ContentValues();

        records[1] = StringUtils.revertFromEscaped( records[1] );
        values.put( LibraryDatabaseHelper.PatientsTable.NAME, records[1] );

        records[2] = StringUtils.revertFromEscaped( records[2] );
        values.put( LibraryDatabaseHelper.PatientsTable.DOB, records[2] );

        records[3] = StringUtils.revertFromEscaped( records[3] );
        values.put( LibraryDatabaseHelper.PatientsTable.TAJ, records[3] );

        records[4] = StringUtils.revertFromEscaped( records[4] );
        values.put( LibraryDatabaseHelper.PatientsTable.PHONE, records[4] );

        records[5] = StringUtils.revertFromEscaped( records[5] );
        values.put( LibraryDatabaseHelper.PatientsTable.NOTE, records[5] );

        getContentResolver()
                .insert( LibraryDatabaseHelper.PatientsTable.CONTENT_URI, values);
        Logger.debug( "Patient [" + records[1] + "] was inserted.");
		}

	}
