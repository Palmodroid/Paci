package digitalgarden.librarydb;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.MedicationsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PillsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PatientsTable;
import digitalgarden.librarydb.formtypes.EditTextField;
import digitalgarden.librarydb.formtypes.ForeignKey;
import digitalgarden.librarydb.formtypes.ForeignTextField;
import digitalgarden.logger.Logger;

public class MedicationsEditFragment extends GeneralEditFragment
	{
	private EditTextField medicationNameField;
	private ForeignKey pillId = new ForeignKey( PillsTable.CONTENT_URI );
	// Míg a címet a szöveg azonosítja, a szerzőt az id
	// Nem lehet null, ezért -1 azonosítja a null-t Ezt ellenőrizni kell!!
	private ForeignTextField foreignTextPill;
	private ForeignKey patientId = new ForeignKey( PatientsTable.CONTENT_URI );
	private ForeignTextField foreignTextPatient;
    private ForeignTextField foreignTextPatientDob;

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

        // EditTextField
        medicationNameField = (EditTextField) view.findViewById( R.id.edittext_medication_name );
        medicationNameField.connect( this );

        // ForeignKey
        pillId.connect( this );
        pillId.setupSelector( PillsControllActivity.class,
                getActivity().getString( R.string.select_pill ),
                medicationNameField );

		// ForeignKey
		patientId.connect( this );
		patientId.setupSelector( PatientsControllActivity.class,
				getActivity().getString( R.string.select_patient ),
				medicationNameField );

		// ForeignTextField
        foreignTextPill = (ForeignTextField) view.findViewById(R.id.edittext_pill);
        foreignTextPill.link( pillId, PillsTable.NAME );

		// ForeignTextField
		foreignTextPatient = (ForeignTextField) view.findViewById(R.id.edittext_patient);
		foreignTextPatient.link( patientId, PatientsTable.NAME );

        // ForeignTextField
        foreignTextPatientDob = (ForeignTextField) view.findViewById(R.id.edittext_patient_dob);
        foreignTextPatientDob.link( patientId, PatientsTable.DOB );

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
                MedicationsTable.PILL_ID,
				MedicationsTable.PATIENT_ID,
				MedicationsTable.NAME };
		Cursor cursor = getActivity().getContentResolver().query(getItemContentUri(), projection, null, null, null);

		if (cursor != null) // Ez vajon kell? 
			{
			cursor.moveToFirst();

            int column = cursor.getColumnIndexOrThrow( MedicationsTable.PILL_ID );
            if ( cursor.isNull( column ) )
                pillId.setValue( -1L );
            else
                pillId.setValue( cursor.getLong( column ) );

			column = cursor.getColumnIndexOrThrow( MedicationsTable.PATIENT_ID );
			if ( cursor.isNull( column ) )
				patientId.setValue( -1L );
			else
				patientId.setValue( cursor.getLong( column ) );

			medicationNameField.setText(cursor.getString(cursor.getColumnIndexOrThrow( MedicationsTable.NAME )));

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
        if (pillId.getValue() >= 0)
            values.put( MedicationsTable.PILL_ID, pillId.getValue());
        else
            values.putNull( MedicationsTable.PILL_ID );

        if (patientId.getValue() >= 0)
            values.put( MedicationsTable.PATIENT_ID, patientId.getValue());
        else
            values.putNull( MedicationsTable.PATIENT_ID );

        values.put( MedicationsTable.NAME, name);

        return values;
		}

	@Override
	protected void saveFieldData(Bundle data)
		{
        data.putLong("PILL_ID", pillId.getValue() );
		data.putLong("PATIENT_ID", patientId.getValue() );
		}

	@Override
	protected void retrieveFieldData(Bundle data)
		{
        pillId.setValue( data.getLong( "PILL_ID" ) );
		patientId.setValue( data.getLong( "PATIENT_ID" ) );
		}

	@Override
	protected void checkReturningSelector(int requestCode, long selectedId)
		{
        pillId.checkReturningSelector( requestCode, selectedId );
		patientId.checkReturningSelector( requestCode, selectedId );
        Logger.note("Pill id: " + selectedId + " was selected in onActivityResult?");
		Logger.note("Patient id: " + selectedId + " was selected in onActivityResult?");
		}
	}
