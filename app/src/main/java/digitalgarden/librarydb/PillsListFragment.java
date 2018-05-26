package digitalgarden.librarydb;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PillsTable;

public class PillsListFragment extends GeneralListFragment
	{
	// static factory method
	// http://www.androiddesignpatterns.com/2012/05/using-newinstance-to-instantiate.html
	public static GeneralListFragment newInstance( long select )
		{
		GeneralListFragment listFragmenet = new PillsListFragment();
	
		Bundle args = new Bundle();

		args.putLong( SELECTED_ITEM, select );
		// args.putString( LIMITED_COLUMN, null); Sem ez, sem LIMITED_ITEM nem kell!

		args.putStringArray( FILTERED_COLUMN, new String[] {PillsTable.NAME});
		args.putString( ORDERED_COLUMN, PillsTable.NAME);

		listFragmenet.setArguments(args);
		
		return listFragmenet;
		}

	
	protected int getLoaderId()
		{
		return PillsTable.TABLEID;
		}

	@Override
	protected Uri getContentUri()
		{
		return PillsTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		String[] projection = new String[] {
				PillsTable._ID,
				PillsTable.NAME };

		return projection;
		}
	
	@Override
	protected int getRowView()
		{
		return R.layout.pill_list_row_view;
		}

	@Override
	protected String[] getFrom()
		{
		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] {
			PillsTable.NAME,
			PillsTable._ID };

		return from;
		}

	@Override
	protected int[] getTo()
		{
		// the XML defined views which the data will be bound to
		int[] to = new int[] {
			R.id.pill,
			R.id.id };

		return to;
		}

	@Override
	protected void addExamples()
		{
		ContentValues values = new ContentValues();

		values.put( PillsTable.NAME, "Algopyrin");
		getActivity().getContentResolver().insert( PillsTable.CONTENT_URI, values);

		values.put( PillsTable.NAME, "Proxelan");
		getActivity().getContentResolver().insert( PillsTable.CONTENT_URI, values);

		values.put( PillsTable.NAME, "Politrate");
		getActivity().getContentResolver().insert( PillsTable.CONTENT_URI, values);

		values.put( PillsTable.NAME, "Abirateron");
		getActivity().getContentResolver().insert( PillsTable.CONTENT_URI, values);

		values.put( PillsTable.NAME, "Enzalutamid");
		getActivity().getContentResolver().insert( PillsTable.CONTENT_URI, values);
		}
	}
