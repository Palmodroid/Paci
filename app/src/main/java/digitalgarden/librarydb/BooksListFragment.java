package digitalgarden.librarydb;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.AuthorsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.BooksTable;

public class BooksListFragment extends GeneralListFragment
	{
	// static factory method
	// http://www.androiddesignpatterns.com/2012/05/using-newinstance-to-instantiate.html
	public static GeneralListFragment newInstance( long limit )
		{
		GeneralListFragment listFragmenet = new BooksListFragment();

		Bundle args = new Bundle();
		
		// args.putLong( SELECTED_ITEM , SELECT_DISABLED ); Nincs szelektálás!
		
		args.putLong( LIMITED_ITEM, limit );
		args.putString( LIMITED_COLUMN, BooksTable.FULL_AUTHOR_ID);
		args.putString( ORDERED_COLUMN, AuthorsTable.FULL_NAME);
		// args.putString( FILTERED_COLUMN, BooksTable.FULL_SEARCH);
		args.putStringArray( FILTERED_COLUMN, new String[] {AuthorsTable.FULL_SEARCH, BooksTable.FULL_SEARCH});
		
		listFragmenet.setArguments(args);

		return listFragmenet;
		}

	protected int getLoaderId()
		{
		return BooksTable.TABLEID;
		}

	@Override
	protected Uri getContentUri()
		{
		return BooksTable.CONTENT_URI;
		}

	@Override
	protected String[] getProjection()
		{
		String[] projection = new String[] {
				BooksTable.FULL_ID,
				AuthorsTable.FULL_NAME,
				BooksTable.FULL_TITLE };

		return projection;
		}
	
	@Override
	protected int getRowView()
		{
		return R.layout.book_list_row_view;
		}

	@Override
	protected String[] getFrom()
		{
		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] {
				AuthorsTable.NAME,
				BooksTable.TITLE,
				BooksTable._ID };

		return from;
		}

	@Override
	protected int[] getTo()
		{
		// the XML defined views which the data will be bound to
		int[] to = new int[] {
				R.id.author,
				R.id.title,
				R.id.id };

		return to;
		}

	@Override
	protected void addExamples()
		{
		ContentValues values = new ContentValues();

		values.putNull( BooksTable.AUTHOR_ID );
		values.put( BooksTable.TITLE, "Urania");
    	getActivity().getContentResolver().insert(  BooksTable.CONTENT_URI, values);

		values.putNull( BooksTable.AUTHOR_ID );
		values.put( BooksTable.TITLE, "Elrontottam!");
    	getActivity().getContentResolver().insert( BooksTable.CONTENT_URI, values);

		values.putNull( BooksTable.AUTHOR_ID );
		values.put( BooksTable.TITLE, "Egri csillagok");
    	getActivity().getContentResolver().insert( BooksTable.CONTENT_URI, values);

		values.putNull( BooksTable.AUTHOR_ID );
		values.put( BooksTable.TITLE, "A Pál utcai fiúk");
    	getActivity().getContentResolver().insert( BooksTable.CONTENT_URI, values);

		values.putNull( BooksTable.AUTHOR_ID );
		values.put( BooksTable.TITLE, "Abigél");
    	getActivity().getContentResolver().insert( BooksTable.CONTENT_URI, values);

		values.putNull( BooksTable.AUTHOR_ID );
		values.put( BooksTable.TITLE, "Tüskevár");
		getActivity().getContentResolver().insert( BooksTable.CONTENT_URI, values);

		values.putNull( BooksTable.AUTHOR_ID );
		values.put( BooksTable.TITLE, "Ábel a rengetegben");
    	getActivity().getContentResolver().insert( BooksTable.CONTENT_URI, values);

		values.putNull( BooksTable.AUTHOR_ID );
		values.put( BooksTable.TITLE, "Példa Fibinek");
		getActivity().getContentResolver().insert( BooksTable.CONTENT_URI, values);
		}

	}
