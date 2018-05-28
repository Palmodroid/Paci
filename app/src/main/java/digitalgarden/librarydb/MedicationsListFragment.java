package digitalgarden.librarydb;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.MedicationsTable;

public class MedicationsListFragment extends GeneralListFragment
	{
	// static factory method
	// http://www.androiddesignpatterns.com/2012/05/using-newinstance-to-instantiate.html
	public static GeneralListFragment newInstance( long select )
		{
		GeneralListFragment listFragmenet = new MedicationsListFragment();

		Bundle args = new Bundle();

		args.putLong( SELECTED_ITEM, select );
		// args.putString( LIMITED_COLUMN, null); Sem ez, sem LIMITED_ITEM nem kell!

		args.putStringArray( FILTERED_COLUMN, new String[] {MedicationsTable.NAME});
		args.putString( ORDERED_COLUMN, MedicationsTable.NAME);

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
				MedicationsTable._ID,
				MedicationsTable.NAME };

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
			MedicationsTable.NAME,
			MedicationsTable._ID };

		return from;
		}

	@Override
	protected int[] getTo()
		{
		// the XML defined views which the data will be bound to
		int[] to = new int[] {
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
