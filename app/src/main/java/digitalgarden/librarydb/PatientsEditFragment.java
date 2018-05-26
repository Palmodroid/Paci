package digitalgarden.librarydb;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PatientsTable;
import digitalgarden.librarydb.formtypes.EditTextField;
import digitalgarden.logger.Logger;

public class PatientsEditFragment extends GeneralEditFragment
	{
	private EditTextField nameField;
	private EditTextField dobField;
	private EditTextField tajField;
	private EditTextField phoneField;
	private EditTextField noteField;

	@Override
	protected Uri getTableContentUri()
		{
		return PatientsTable.CONTENT_URI;
		}

	@Override
	protected int getFormLayout()
		{
		return 	R.layout.patient_edit_fragment_form;
		}

	@Override
	protected void setupFormLayout( View view )
		{
		Logger.note("PatientsEditFragment setupFormLayout");
		
        nameField = (EditTextField) view.findViewById( R.id.edittext_patient_name );
        nameField.connect( this );

		dobField = (EditTextField) view.findViewById( R.id.edittext_patient_dob );
		dobField.connect( this );

		tajField = (EditTextField) view.findViewById( R.id.edittext_patient_taj );
		tajField.connect( this );

		phoneField = (EditTextField) view.findViewById( R.id.edittext_patient_phone );
		phoneField.connect( this );

		noteField = (EditTextField) view.findViewById( R.id.edittext_patient_note );
		noteField.connect( this );
		}

	@Override
	protected void setupFieldsData(long id)
		{
		Logger.note("PatientsEditFragment setupFieldsData");

		String[] projection = {
				PatientsTable.NAME,
				PatientsTable.DOB,
				PatientsTable.TAJ,
				PatientsTable.PHONE,
				PatientsTable.NOTE };
		Cursor cursor = getActivity().getContentResolver().query(getItemContentUri(), projection, null, null, null);

		if (cursor != null) // Ez vajon kell? 
			{
			cursor.moveToFirst();

			nameField.setText(cursor.getString(cursor.getColumnIndexOrThrow(  PatientsTable.NAME )));
			dobField.setText(cursor.getString(cursor.getColumnIndexOrThrow(  PatientsTable.DOB )));
			tajField.setText(cursor.getString(cursor.getColumnIndexOrThrow(  PatientsTable.TAJ )));
			phoneField.setText(cursor.getString(cursor.getColumnIndexOrThrow(  PatientsTable.PHONE )));
			noteField.setText(cursor.getString(cursor.getColumnIndexOrThrow(  PatientsTable.NOTE )));

			// Always close the cursor
			cursor.close();
			}
		}

	@Override
	protected ContentValues getFieldsData()
		{
		Logger.note("PatientsEditFragment getFieldsData");

		String name = nameField.getText().toString();
		String dob = dobField.getText().toString();
		String taj = tajField.getText().toString();
		String phone = phoneField.getText().toString();
		String note = noteField.getText().toString();

	    ContentValues values = new ContentValues();
		values.put( PatientsTable.NAME, name);
		values.put( PatientsTable.DOB, dob);
		values.put( PatientsTable.TAJ, taj);
		values.put( PatientsTable.PHONE, phone);
		values.put( PatientsTable.NOTE, note);

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
