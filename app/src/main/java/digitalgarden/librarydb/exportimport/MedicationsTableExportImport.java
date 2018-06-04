package digitalgarden.librarydb.exportimport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import digitalgarden.librarydb.database.LibraryDatabaseHelper.MedicationsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PatientsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PillsTable;
import digitalgarden.logger.Logger;
import digitalgarden.utils.StringUtils;

public class MedicationsTableExportImport extends GeneralTableExportImport
	{
	public MedicationsTableExportImport(Context context)
		{
		super(context);
		// TODO Auto-generated constructor stub
		}

	@Override
	protected Uri getContentUri()
		{
		return MedicationsTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		return new String[] {
                MedicationsTable.NAME,
                PillsTable.NAME,
			    PatientsTable.NAME,
			    PatientsTable.DOB,
			    PatientsTable.TAJ };
		}

	@Override
	protected String[] getRowData(Cursor cursor)
		{
		return new String[] {
                cursor.getString( cursor.getColumnIndexOrThrow( MedicationsTable.NAME )),
                cursor.getString( cursor.getColumnIndexOrThrow( PillsTable.NAME )),
                cursor.getString( cursor.getColumnIndexOrThrow( PatientsTable.NAME )),
                cursor.getString( cursor.getColumnIndexOrThrow( PatientsTable.DOB )),
                cursor.getString( cursor.getColumnIndexOrThrow( PatientsTable.TAJ ))
        };
		}

	@Override
	protected String getTableName()
		{
		return MedicationsTable.TABLENAME;
		}

	@Override
	public void importRow(String[] records)
		{
		// Két adat miatt itt szükséges a hossz ellenőrzése
		if ( records.length < 6 )
			{
			Logger.note( "Parameters missing from MEDICATIONS row. Item was skipped.");
			return;
			}
		
		long pillId = findPillId( records[2] );
		if ( pillId == ID_MISSING )
			{
			Logger.note( "Pill [" + records[2] + "] does not exists! Item was skipped.");
			return;
			}

        long patientId = findPatientId( records[3], records[4], records[5] );
        if ( patientId == ID_MISSING )
            {
            Logger.note( "Patient [" + records[3] + "] does not exists! Item was skipped.");
            return;
            }

		ContentValues values = new ContentValues();
		
		if ( pillId == ID_NULL )
    		values.putNull( MedicationsTable.PILL_ID );
		else
    		values.put( MedicationsTable.PILL_ID, pillId );

        if ( patientId == ID_NULL )
            values.putNull( MedicationsTable.PATIENT_ID );
        else
            values.put( MedicationsTable.PATIENT_ID, patientId );

		records[1] = StringUtils.revertFromEscaped( records[1] );
		values.put( MedicationsTable.NAME, records[1] );
				
		getContentResolver()
			.insert( MedicationsTable.CONTENT_URI, values);
		Logger.debug( "Medication [" + records[1] + "] was inserted.");
		}

	
	private long ID_MISSING = -2L;
	private long ID_NULL = -1L;
	
	// Ezt a keresőrutint nehéz generalizálni, mert az azonosító paraméterek típusa is különböző lehet
	private long findPillId(String pillName)
		{
		if ( pillName == null )
			{
    		return ID_NULL;
			}

		long pillId = ID_MISSING;
		
		String[] projection = {
			PillsTable._ID,	
			PillsTable.NAME };
		Cursor cursor = getContentResolver()
			.query(PillsTable.CONTENT_URI, projection, 
				   PillsTable.NAME + "=\'" + StringUtils.revertFromEscaped( pillName ) + "\'", 
				   null, null);

		if ( cursor != null)
			{
			if (cursor.moveToFirst())
				pillId = cursor.getLong( cursor.getColumnIndexOrThrow( PillsTable._ID ) );								
			cursor.close();
			}
		
		return pillId;
		}
 
    // Ezt a keresőrutint nehéz generalizálni, mert az azonosító paraméterek típusa is különböző lehet
    private long findPatientId(String patientName, String patientDob, String patientTaj)
        {
        if ( patientName == null || patientDob == null || patientTaj == null)
            {
            return ID_NULL;
            }

        long patientId = ID_MISSING;

        String[] projection = {
                PatientsTable._ID,
                PatientsTable.NAME,
                PatientsTable.DOB,
                PatientsTable.TAJ };
        Cursor cursor = getContentResolver()
                .query(PatientsTable.CONTENT_URI, projection,
                        PatientsTable.NAME + "=\'" + StringUtils.revertFromEscaped( patientName ) + "\' AND " +
                                PatientsTable.DOB + "=\'" + StringUtils.revertFromEscaped( patientDob ) + "\' AND " +
                                PatientsTable.TAJ + "=\'" + StringUtils.revertFromEscaped( patientTaj ) + "\'",
                        null, null);

        if ( cursor != null)
            {
            if (cursor.moveToFirst())
                patientId = cursor.getLong( cursor.getColumnIndexOrThrow( PatientsTable._ID ) );
            cursor.close();
            }

        return patientId;
        }
	}
