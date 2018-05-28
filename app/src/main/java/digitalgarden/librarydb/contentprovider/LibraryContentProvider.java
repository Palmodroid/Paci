
package digitalgarden.librarydb.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import digitalgarden.librarydb.database.LibraryDatabaseHelper;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.AuthorsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.BooksTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.MedicationsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PatientsTable;
import digitalgarden.librarydb.database.LibraryDatabaseHelper.PillsTable;
import digitalgarden.logger.Logger;
import digitalgarden.utils.StringUtils;



public class LibraryContentProvider extends ContentProvider
	{
	private LibraryDatabaseHelper libraryDbDatabaseHelper;

	// MIME Type
	// Minden, ami az egyes táblákra jellemző, átment a LibraryDatabaseHelper alosztályaiba
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static 
		{
		sURIMatcher.addURI(BooksTable.AUTHORITY, BooksTable.TABLENAME, BooksTable.DIRID);
		sURIMatcher.addURI(BooksTable.AUTHORITY, BooksTable.TABLENAME + "/#", BooksTable.ITEMID);
		sURIMatcher.addURI(BooksTable.AUTHORITY, BooksTable.TABLENAME + BooksTable.CONTENT_COUNT, BooksTable.COUNTID);

		sURIMatcher.addURI(AuthorsTable.AUTHORITY, AuthorsTable.TABLENAME, AuthorsTable.DIRID);
		sURIMatcher.addURI(AuthorsTable.AUTHORITY, AuthorsTable.TABLENAME + "/#", AuthorsTable.ITEMID);
		sURIMatcher.addURI(AuthorsTable.AUTHORITY, AuthorsTable.TABLENAME + AuthorsTable.CONTENT_COUNT, AuthorsTable.COUNTID);

		sURIMatcher.addURI(PatientsTable.AUTHORITY, PatientsTable.TABLENAME, PatientsTable.DIRID);
		sURIMatcher.addURI(PatientsTable.AUTHORITY, PatientsTable.TABLENAME + "/#", PatientsTable.ITEMID);
		sURIMatcher.addURI(PatientsTable.AUTHORITY, PatientsTable.TABLENAME + PatientsTable.CONTENT_COUNT, PatientsTable.COUNTID);

		sURIMatcher.addURI(PillsTable.AUTHORITY, PillsTable.TABLENAME, PillsTable.DIRID);
		sURIMatcher.addURI(PillsTable.AUTHORITY, PillsTable.TABLENAME + "/#", PillsTable.ITEMID);
		sURIMatcher.addURI(PillsTable.AUTHORITY, PillsTable.TABLENAME + PillsTable.CONTENT_COUNT, PillsTable.COUNTID);

		sURIMatcher.addURI(MedicationsTable.AUTHORITY, MedicationsTable.TABLENAME, MedicationsTable.DIRID);
		sURIMatcher.addURI(MedicationsTable.AUTHORITY, MedicationsTable.TABLENAME + "/#", MedicationsTable.ITEMID);
		sURIMatcher.addURI(MedicationsTable.AUTHORITY, MedicationsTable.TABLENAME + MedicationsTable.CONTENT_COUNT, MedicationsTable.COUNTID);
		}

	
	@Override
	public boolean onCreate()
		{
		Logger.note("CONTENTPROVIDER: onCreate");
		
		libraryDbDatabaseHelper = new LibraryDatabaseHelper( getContext() );
		return true; 
		}

	
	@Override
	public String getType(Uri uri)
		{
		// MIME típust ad vissza
		return null;
		}
	
	
	// Az Uri a szükséges táblát adja meg (nem egy elemet!) melybe az adatokat be kívánjuk illeszteni
	// A Visszatérési URi ezzel szemben a konkrét beillesztett elem
	@Override
	public Uri insert(Uri uri, ContentValues values)
		{
		long id = 0;

		int uriType = sURIMatcher.match(uri);
		switch (uriType) 
			{

		case BooksTable.DIRID:
			{
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( BooksTable.SEARCH, StringUtils.normalize(
//				values.getAsString( BooksTable.AUTHOR ) + 
				values.getAsString( BooksTable.TITLE ) ));
				
			SQLiteDatabase libraryDb = libraryDbDatabaseHelper.getWritableDatabase();
			id = libraryDb.insert( BooksTable.TABLENAME, null, values );
			
			getContext().getContentResolver().notifyChange(uri, null);

			Logger.note("CONTENTPROVIDER: " + id + " inserted into BOOKS");
			return Uri.parse(BooksTable.CONTENT_URI + "/" + id);
			}
			
		case AuthorsTable.DIRID:
			{
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( AuthorsTable.SEARCH, StringUtils.normalize(
				values.getAsString( AuthorsTable.NAME ) ));

			SQLiteDatabase libraryDb = libraryDbDatabaseHelper.getWritableDatabase();
			id = libraryDb.insert( AuthorsTable.TABLENAME, null, values );

			getContext().getContentResolver().notifyChange(uri, null);

			Logger.note("CONTENTPROVIDER: " + id + " inserted into AUTHORS");
			return Uri.parse(AuthorsTable.CONTENT_URI + "/" + id);
			}

        case PatientsTable.DIRID:
			{
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( PatientsTable.SEARCH, StringUtils.normalize(
					values.getAsString( PatientsTable.NAME ) ));

			SQLiteDatabase libraryDb = libraryDbDatabaseHelper.getWritableDatabase();
			id = libraryDb.insert( PatientsTable.TABLENAME, null, values );

			getContext().getContentResolver().notifyChange(uri, null);

			Logger.note("CONTENTPROVIDER: " + id + " inserted into PATIENTS");
			return Uri.parse(PatientsTable.CONTENT_URI + "/" + id);
			}

		case PillsTable.DIRID:
			{
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( PillsTable.SEARCH, StringUtils.normalize(
					values.getAsString( PillsTable.NAME ) ));

			SQLiteDatabase libraryDb = libraryDbDatabaseHelper.getWritableDatabase();
			id = libraryDb.insert( PillsTable.TABLENAME, null, values );

			getContext().getContentResolver().notifyChange(uri, null);

			Logger.note("CONTENTPROVIDER: " + id + " inserted into PILLS");
			return Uri.parse(PillsTable.CONTENT_URI + "/" + id);
			}

		case MedicationsTable.DIRID:
			{
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( MedicationsTable.SEARCH, StringUtils.normalize(
					values.getAsString( MedicationsTable.NAME ) ));

			SQLiteDatabase libraryDb = libraryDbDatabaseHelper.getWritableDatabase();
			id = libraryDb.insert( MedicationsTable.TABLENAME, null, values );

			getContext().getContentResolver().notifyChange(uri, null);

			Logger.note("CONTENTPROVIDER: " + id + " inserted into MEDICATIONS");
			return Uri.parse(MedicationsTable.CONTENT_URI + "/" + id);
			}

    	default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		}
	
	
	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs)
		{
		int rowsDeleted = 0;

		SQLiteDatabase libraryDb = libraryDbDatabaseHelper.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) 
			{
			
		case BooksTable.DIRID:
			rowsDeleted = libraryDb.delete(BooksTable.TABLENAME, whereClause, whereArgs);
			break;
			
		case BooksTable.ITEMID:
			{
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(whereClause)) 
				{
				rowsDeleted = libraryDb.delete( BooksTable.TABLENAME, BooksTable._ID + "=" + id, null);
				} 
			else 
				{
				rowsDeleted = libraryDb.delete( BooksTable.TABLENAME, BooksTable._ID + "=" + id + " and " + whereClause, whereArgs);
				}
			break;
			}
			
		case AuthorsTable.DIRID:
			rowsDeleted = libraryDb.delete(AuthorsTable.TABLENAME, whereClause, whereArgs);
			break;
		
		case AuthorsTable.ITEMID:
			{
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(whereClause)) 
				{
				rowsDeleted = libraryDb.delete( AuthorsTable.TABLENAME, AuthorsTable._ID + "=" + id, null);
				} 
			else 
				{
				rowsDeleted = libraryDb.delete( AuthorsTable.TABLENAME, AuthorsTable._ID + "=" + id + " and " + whereClause, whereArgs);
				}
			break;
			}

        case PatientsTable.DIRID:
                rowsDeleted = libraryDb.delete(PatientsTable.TABLENAME, whereClause, whereArgs);
                break;

        case PatientsTable.ITEMID:
            {
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(whereClause))
                {
                rowsDeleted = libraryDb.delete( PatientsTable.TABLENAME, PatientsTable._ID + "=" + id, null);
                }
            else
                {
                rowsDeleted = libraryDb.delete( PatientsTable.TABLENAME, PatientsTable._ID + "=" + id + " and " + whereClause, whereArgs);
                }
            break;
            }

		case PillsTable.DIRID:
			rowsDeleted = libraryDb.delete(PillsTable.TABLENAME, whereClause, whereArgs);
			break;

		case PillsTable.ITEMID:
			{
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(whereClause))
				{
				rowsDeleted = libraryDb.delete( PillsTable.TABLENAME, PillsTable._ID + "=" + id, null);
				}
			else
				{
				rowsDeleted = libraryDb.delete( PillsTable.TABLENAME, PillsTable._ID + "=" + id + " and " + whereClause, whereArgs);
				}
			break;
			}

		case MedicationsTable.DIRID:
			rowsDeleted = libraryDb.delete(MedicationsTable.TABLENAME, whereClause, whereArgs);
			break;

		case MedicationsTable.ITEMID:
			{
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(whereClause))
				{
				rowsDeleted = libraryDb.delete( MedicationsTable.TABLENAME, MedicationsTable._ID + "=" + id, null);
				}
			else
				{
				rowsDeleted = libraryDb.delete( MedicationsTable.TABLENAME, MedicationsTable._ID + "=" + id + " and " + whereClause, whereArgs);
				}
			break;
			}

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		
		if (rowsDeleted > 0)
			getContext().getContentResolver().notifyChange(uri, null);

		Logger.note("CONTENTPROVIDER: " + rowsDeleted + " rows deleted");
		return rowsDeleted;
		}
	
	
	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) 
		{
		int rowsUpdated = 0;

		SQLiteDatabase libraryDb = libraryDbDatabaseHelper.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) 
			{
			
		case BooksTable.DIRID:
			// Nem biztosítható, hogy a search rész működőképes marad!
			throw new IllegalArgumentException("Multiple updates on BOOKS are not allowed: " + uri);
			/*
			rowsUpdated = libraryDb.update(LibraryDatabaseHelper.TABLE_BOOKS, 
			values, 
			whereClause,
			whereArgs);
			
			break;
			*/
			
		case BooksTable.ITEMID:
			{			
			// if ( !values.containsKey( BooksTable.AUTHOR ) || !values.containsKey( BooksTable.AUTHOR ) )
			// 	throw new IllegalArgumentException("AUTHOR and TITLE should be updated together: " + uri);
				
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( BooksTable.SEARCH, StringUtils.normalize(
			//		   values.getAsString( BooksTable.AUTHOR ) + 
					   values.getAsString( BooksTable.TITLE ) ));
		
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(whereClause)) 
				{
				rowsUpdated = libraryDb.update( BooksTable.TABLENAME, 
				values,
				BooksTable._ID  + "=" + id, 
				null);
				} 
			else 
				{
				rowsUpdated = libraryDb.update( BooksTable.TABLENAME, 
				values,
				BooksTable._ID  + "=" + id 
				+ " and " 
				+ whereClause,
				whereArgs);
				}
			break;
			}
			
		case AuthorsTable.DIRID:
			{
			// Nem biztosítható, hogy a search rész működőképes marad!
			throw new IllegalArgumentException("Multiple updates on AUTHORS are not allowed: " + uri);
			/* rowsUpdated = libraryDb.update( AuthorsTable.TABLENAME, 
			values, 
			whereClause,
			whereArgs);
			break; */
			}
			
		case AuthorsTable.ITEMID:
			{			
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( AuthorsTable.SEARCH, StringUtils.normalize(
				values.getAsString( AuthorsTable.NAME ) ));

			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(whereClause)) 
				{
				rowsUpdated = libraryDb.update( AuthorsTable.TABLENAME, 
				values,
				AuthorsTable._ID  + "=" + id, 
				null);
				} 
			else 
				{
				rowsUpdated = libraryDb.update( AuthorsTable.TABLENAME, 
				values,
				AuthorsTable._ID  + "=" + id 
				+ " and " 
				+ whereClause,
				whereArgs);
				}
			break;
			}

        case PatientsTable.DIRID:
            // Nem biztosítható, hogy a search rész működőképes marad!
            throw new IllegalArgumentException("Multiple updates on PATIENTS are not allowed: " + uri);
			/*
			rowsUpdated = libraryDb.update(LibraryDatabaseHelper.TABLE_BOOKS, 
			values, 
			whereClause,
			whereArgs);
			
			break;
			*/

        case PatientsTable.ITEMID:
            {
            // if ( !values.containsKey( PatientsTable.AUTHOR ) || !values.containsKey( PatientsTable.AUTHOR ) )
            // 	throw new IllegalArgumentException("AUTHOR and TITLE should be updated together: " + uri);

            // Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
            values.put( PatientsTable.SEARCH, StringUtils.normalize(
                    //		   values.getAsString( PatientsTable.AUTHOR ) + 
                    values.getAsString( PatientsTable.NAME ) ));

            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(whereClause))
                {
                rowsUpdated = libraryDb.update( PatientsTable.TABLENAME,
                        values,
                        PatientsTable._ID  + "=" + id,
                        null);
                }
            else
                {
                rowsUpdated = libraryDb.update( PatientsTable.TABLENAME,
                        values,
                        PatientsTable._ID  + "=" + id
                                + " and "
                                + whereClause,
                        whereArgs);
                }
            break;
            }

		case PillsTable.DIRID:
			{
			// Nem biztosítható, hogy a search rész működőképes marad!
			throw new IllegalArgumentException("Multiple updates on PILLS are not allowed: " + uri);
			/* rowsUpdated = libraryDb.update( PillsTable.TABLENAME,
			values,
			whereClause,
			whereArgs);
			break; */
			}

		case PillsTable.ITEMID:
			{
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( PillsTable.SEARCH, StringUtils.normalize(
					values.getAsString( PillsTable.NAME ) ));

			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(whereClause))
				{
				rowsUpdated = libraryDb.update( PillsTable.TABLENAME,
						values,
						PillsTable._ID  + "=" + id,
						null);
				}
			else
				{
				rowsUpdated = libraryDb.update( PillsTable.TABLENAME,
						values,
						PillsTable._ID  + "=" + id
								+ " and "
								+ whereClause,
						whereArgs);
				}
			break;
			}

		case MedicationsTable.DIRID:
			{
			// Nem biztosítható, hogy a search rész működőképes marad!
			throw new IllegalArgumentException("Multiple updates on MEDICATIONS are not allowed: " + uri);
			/* rowsUpdated = libraryDb.update( MedicationsTable.TABLENAME,
			values,
			whereClause,
			whereArgs);
			break; */
			}

		case MedicationsTable.ITEMID:
			{
			// Az ekezet nelkuli kereseshez meg egy oszlop hozzakerul
			values.put( MedicationsTable.SEARCH, StringUtils.normalize(
					values.getAsString( MedicationsTable.NAME ) ));

			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(whereClause))
				{
				rowsUpdated = libraryDb.update( MedicationsTable.TABLENAME,
						values,
						MedicationsTable._ID  + "=" + id,
						null);
				}
			else
				{
				rowsUpdated = libraryDb.update( MedicationsTable.TABLENAME,
						values,
						MedicationsTable._ID  + "=" + id
								+ " and "
								+ whereClause,
						whereArgs);
				}
			break;
			}

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		
		if (rowsUpdated > 0)
			getContext().getContentResolver().notifyChange(uri, null);

		Logger.note("CONTENTPROVIDER " + rowsUpdated + " rows updated");
		return rowsUpdated;
		}

	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) 
		{
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		String logger;
		int uriType = sURIMatcher.match(uri);
		switch (uriType) 
			{
			
		case BooksTable.DIRID:
			// Set the table
			queryBuilder.setTables( BooksTable.TABLENAME + 
				" LEFT OUTER JOIN " + AuthorsTable.TABLENAME +
				" ON " + BooksTable.FULL_AUTHOR_ID + "=" + AuthorsTable.FULL_ID );
			logger = "ALL BOOKS";
			break;
			
		case BooksTable.ITEMID:
			// Set the table
			queryBuilder.setTables( BooksTable.TABLENAME + 
					" LEFT OUTER JOIN " + AuthorsTable.TABLENAME +
					" ON " + BooksTable.FULL_AUTHOR_ID + "=" + AuthorsTable.FULL_ID );
			// Adding the ID to the original query
			queryBuilder.appendWhere( BooksTable.FULL_ID  + "=" + uri.getLastPathSegment());
			logger = "ONE BOOK ITEM";
			break;
			
		case BooksTable.COUNTID:
			// Set the table
			queryBuilder.setTables( BooksTable.TABLENAME + 
				" LEFT OUTER JOIN " + AuthorsTable.TABLENAME +
				" ON " + BooksTable.FULL_AUTHOR_ID + "=" + AuthorsTable.FULL_ID );
			projection = new String[] { "count(*) as count" };
			// Projectiont át kell alakítani!
			logger = "BOOKS COUNT";
			break;
			
		case AuthorsTable.DIRID:
			// Set the table
			queryBuilder.setTables( AuthorsTable.TABLENAME );
			logger = "ALL AUTHORS";
			break;
			
		case AuthorsTable.ITEMID:
			// Set the table
			queryBuilder.setTables( AuthorsTable.TABLENAME );
			// Adding the ID to the original query
			queryBuilder.appendWhere( AuthorsTable._ID  + "=" + uri.getLastPathSegment());
			logger = "ONE AUTHOR ITEM";
			break;
			
		case AuthorsTable.COUNTID:
			// Set the table
			queryBuilder.setTables( AuthorsTable.TABLENAME );
			projection = new String[] { "count(*) as count" };
			// Projectiont át kell alakítani!
			logger = "AUTHORS COUNT";
			break;

        case PatientsTable.DIRID:
                // Set the table
                queryBuilder.setTables( PatientsTable.TABLENAME );
                logger = "ALL PATIENTS";
                break;

        case PatientsTable.ITEMID:
                // Set the table
                queryBuilder.setTables( PatientsTable.TABLENAME );
                // Adding the ID to the original query
                queryBuilder.appendWhere( PatientsTable.FULL_ID  + "=" + uri.getLastPathSegment());
                logger = "ONE PATIENT ITEM";
                break;

        case PatientsTable.COUNTID:
                // Set the table
                queryBuilder.setTables( PatientsTable.TABLENAME );
                projection = new String[] { "count(*) as count" };
                // Projectiont át kell alakítani!
                logger = "PATIENTS COUNT";
                break;

		case PillsTable.DIRID:
			// Set the table
			queryBuilder.setTables( PillsTable.TABLENAME );
			logger = "ALL PILLS";
			break;

		case PillsTable.ITEMID:
			// Set the table
			queryBuilder.setTables( PillsTable.TABLENAME );
			// Adding the ID to the original query
			queryBuilder.appendWhere( PillsTable._ID  + "=" + uri.getLastPathSegment());
			logger = "ONE PILL ITEM";
			break;

		case PillsTable.COUNTID:
			// Set the table
			queryBuilder.setTables( PillsTable.TABLENAME );
			projection = new String[] { "count(*) as count" };
			// Projectiont át kell alakítani!
			logger = "PILLS COUNT";
			break;

		case MedicationsTable.DIRID:
			// Set the table
			queryBuilder.setTables( MedicationsTable.TABLENAME );
			logger = "ALL MEDICATIONS";
			break;

		case MedicationsTable.ITEMID:
			// Set the table
			queryBuilder.setTables( MedicationsTable.TABLENAME );
			// Adding the ID to the original query
			queryBuilder.appendWhere( MedicationsTable._ID  + "=" + uri.getLastPathSegment());
			logger = "ONE MEDICATION ITEM";
			break;

		case MedicationsTable.COUNTID:
			// Set the table
			queryBuilder.setTables( MedicationsTable.TABLENAME );
			projection = new String[] { "count(*) as count" };
			// Projectiont át kell alakítani!
			logger = "MEDICATIONS COUNT";
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
			}

		SQLiteDatabase libraryDb = libraryDbDatabaseHelper.getReadableDatabase();
		Cursor cursor = queryBuilder.query(libraryDb, projection, selection, selectionArgs, null, null, sortOrder);

		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		Logger.note("CONTENTPROVIDER " + logger + " queried");
		return cursor;
		}
	}
