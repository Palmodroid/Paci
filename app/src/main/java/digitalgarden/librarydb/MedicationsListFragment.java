package digitalgarden.librarydb;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PatientsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PillsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.MedicationsTable;

public class MedicationsListFragment extends GeneralListFragment
	{
	// static factory method
	// http://www.androiddesignpatterns.com/2012/05/using-newinstance-to-instantiate.html
	public static GeneralListFragment newInstance( long limit )
		{
        GeneralListFragment listFragmenet = new MedicationsListFragment();

        Bundle args = new Bundle();

        // args.putLong( SELECTED_ITEM , SELECT_DISABLED ); Nincs szelektálás!

        args.putLong( LIMITED_ITEM, limit );
        args.putString( LIMITED_COLUMN, MedicationsTable.FULL_PILL_ID);
        args.putString( ORDERED_COLUMN, MedicationsTable.FULL_NAME);
        // args.putString( FILTERED_COLUMN, BooksTable.FULL_SEARCH);
        args.putStringArray( FILTERED_COLUMN, new String[] {PillsTable.FULL_SEARCH, MedicationsTable.FULL_SEARCH});

        listFragmenet.setArguments(args);

        return listFragmenet;
		}


	protected int getLoaderId()
		{
		return MedicationsTable.TABLEID;
		}

	@Override
	protected Uri getContentUri()
		{
		return MedicationsTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		String[] projection = new String[] {
                PillsTable.FULL_NAME,
                PatientsTable.FULL_NAME,
				PatientsTable.FULL_DOB,
                MedicationsTable.FULL_NAME,
                MedicationsTable.FULL_ID };

		return projection;
		}

	@Override
	protected int getRowView()
		{
		return R.layout.medication_list_row_view;
		}

	@Override
	protected String[] getFrom()
		{
		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] {
            PillsTable.NAME,
            PatientsTable.NAME,
            PatientsTable.DOB,
			MedicationsTable.NAME,
			MedicationsTable._ID };

		return from;
		}

	@Override
	protected int[] getTo()
		{
		// the XML defined views which the data will be bound to
		int[] to = new int[] {
            R.id.pill,
            R.id.patient,
            R.id.patient_dob,
			R.id.medication,
			R.id.id };

		return to;
		}

	@Override
	protected void addExamples()
		{
		ContentValues values = new ContentValues();

		values.put( MedicationsTable.NAME, "2003.01.02");
		getActivity().getContentResolver().insert( MedicationsTable.CONTENT_URI, values);

		values.put( MedicationsTable.NAME, "2017.12.20");
		getActivity().getContentResolver().insert( MedicationsTable.CONTENT_URI, values);

		values.put( MedicationsTable.NAME, "2018.05.01");
		getActivity().getContentResolver().insert( MedicationsTable.CONTENT_URI, values);

		}
	}
