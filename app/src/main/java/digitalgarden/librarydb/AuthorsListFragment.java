package digitalgarden.librarydb;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.AuthorsTable;

public class AuthorsListFragment extends GeneralListFragment
	{
	// static factory method
	// http://www.androiddesignpatterns.com/2012/05/using-newinstance-to-instantiate.html
	public static GeneralListFragment newInstance( long select )
		{
		GeneralListFragment listFragmenet = new AuthorsListFragment();
	
		Bundle args = new Bundle();

		args.putLong( SELECTED_ITEM, select );
		// args.putString( LIMITED_COLUMN, null); Sem ez, sem LIMITED_ITEM nem kell!

		args.putStringArray( FILTERED_COLUMN, new String[] {AuthorsTable.NAME});
		args.putString( ORDERED_COLUMN, AuthorsTable.NAME);

		listFragmenet.setArguments(args);
		
		return listFragmenet;
		}

	
	protected int getLoaderId()
		{
		return AuthorsTable.TABLEID;
		}

	@Override
	protected Uri getContentUri()
		{
		return AuthorsTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		String[] projection = new String[] {
				AuthorsTable._ID,
				AuthorsTable.NAME };

		return projection;
		}
	
	@Override
	protected int getRowView()
		{
		return R.layout.author_list_row_view;
		}

	@Override
	protected String[] getFrom()
		{
		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] {
			AuthorsTable.NAME,
			AuthorsTable._ID };

		return from;
		}

	@Override
	protected int[] getTo()
		{
		// the XML defined views which the data will be bound to
		int[] to = new int[] {
			R.id.author,
			R.id.id };

		return to;
		}

	@Override
	protected void addExamples()
		{
		ContentValues values = new ContentValues();

		values.put( AuthorsTable.NAME, "Láng Attila D.");
		getActivity().getContentResolver().insert( AuthorsTable.CONTENT_URI, values);

		values.put( AuthorsTable.NAME, "Gárdonyi Géza");
		getActivity().getContentResolver().insert( AuthorsTable.CONTENT_URI, values);

		values.put( AuthorsTable.NAME, "Molnár Ferenc");
		getActivity().getContentResolver().insert( AuthorsTable.CONTENT_URI, values);

		values.put( AuthorsTable.NAME, "Szabó Magda");
		getActivity().getContentResolver().insert( AuthorsTable.CONTENT_URI, values);

		values.put( AuthorsTable.NAME, "Fekete István");
		getActivity().getContentResolver().insert( AuthorsTable.CONTENT_URI, values);
		}
	}
