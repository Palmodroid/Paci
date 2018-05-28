package digitalgarden.librarydb.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Locale;

import digitalgarden.logger.Logger;


public class LibraryDatabaseHelper extends SQLiteOpenHelper 
	{
	// Database Version & Name
	public static final int DATABASE_VERSION = 9;
	public static final String DATABASE_NAME = "library";

	
	// Ezek ugyan a ContentProvider-hez tartoznak, de mi minden fontos értéket itt gyűjtünk össze
	private static final String AUTHORITY = "digitalgarden.librarydb.contentprovider";
	private static final String CONTENT_COUNT = "/count";

	
	// http://martin.cubeactive.com/android-using-joins-with-a-provider-sqlite/
	public static final class BooksTable implements BaseColumns 
		{
		private BooksTable() {} // Cannot instantiate class
		
		public static final String TABLENAME = "books";
		public static final int TABLEID = 0x100;

		public static final String TITLE = "title";
		// public static final String AUTHOR = "author";
		public static final String AUTHOR_ID = "author_id";
		public static final String NOTE = "note";
		public static final String SEARCH = "search";	

		public static final String FULL_ID = TABLENAME + "." + _ID;
		public static final String FULL_TITLE = TABLENAME + "." + TITLE;
		// public static final String FULL_AUTHOR = TABLENAME + "." + AUTHOR;
		public static final String FULL_AUTHOR_ID = TABLENAME + "." + AUTHOR_ID;
		public static final String FULL_NOTE = TABLENAME + "." + NOTE;
		public static final String FULL_SEARCH = TABLENAME + "." + SEARCH;
		
		// http://stackoverflow.com/a/15422557 - FOREIGN KEY CSAK AZ UTOLSÓ LEHET!
		static final String TABLECREATE =
				"CREATE TABLE " + TABLENAME + "(" + 
				_ID + " INTEGER PRIMARY KEY, " + 
				TITLE + " TEXT, " + 
		//		AUTHOR + " TEXT, " + 
				NOTE + " TEXT, " + 
				SEARCH + " TEXT, " + 
				AUTHOR_ID + " INTEGER, " +
					" FOREIGN KEY (" + AUTHOR_ID + ")" + 
					" REFERENCES " + AuthorsTable.TABLENAME + " (" + AuthorsTable._ID + ")" +
				")";

		public static final String AUTHORITY = LibraryDatabaseHelper.AUTHORITY;
		public static final String CONTENT_COUNT = LibraryDatabaseHelper.CONTENT_COUNT;

		private static final String CONTENT_SUBTYPE = "vnd.digitalgarden.librarydb.contentprovider.book";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_SUBTYPE;
		
		public static final Uri CONTENT_URI = Uri.parse( "content://" + AUTHORITY + "/" + TABLENAME );
		public static final Uri CONTENT_COUNT_URI = Uri.parse(CONTENT_URI + CONTENT_COUNT);
		
		public static final int COUNTID = TABLEID + 1;
		public static final int DIRID = TABLEID + 2;
		public static final int ITEMID = TABLEID + 3;
		}

	
	public static final class AuthorsTable implements BaseColumns 
		{
		private AuthorsTable() {} // Cannot instantiate class
		
		public static final String TABLENAME = "authors";
		public static final int TABLEID = 0x200;
	
		public static final String NAME = "name";
		public static final String SEARCH = "search";	
		
		public static final String FULL_ID = TABLENAME + "." + _ID;
		public static final String FULL_NAME = TABLENAME + "." + NAME;	
		public static final String FULL_SEARCH = TABLENAME + "." + SEARCH;
		
		static final String TABLECREATE =
				"CREATE TABLE " + TABLENAME + " (" + 
				_ID + " INTEGER PRIMARY KEY, " + 
				NAME + " TEXT," + 
				SEARCH + " TEXT" + ")";
		
		public static final String AUTHORITY = LibraryDatabaseHelper.AUTHORITY;
		public static final String CONTENT_COUNT = LibraryDatabaseHelper.CONTENT_COUNT;

		private static final String CONTENT_SUBTYPE = "vnd.digitalgarden.librarydb.contentprovider.author";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_SUBTYPE;

		public static final Uri CONTENT_URI = Uri.parse( "content://" + AUTHORITY + "/" + TABLENAME );
		public static final Uri CONTENT_COUNT_URI = Uri.parse(CONTENT_URI + CONTENT_COUNT);

		public static final int COUNTID = TABLEID + 1;
		public static final int DIRID = TABLEID + 2;
		public static final int ITEMID = TABLEID + 3;
		}


	public static final class PatientsTable implements BaseColumns
		{
		private PatientsTable() {} // Cannot instantiate class

		public static final String TABLENAME = "patients";
		public static final int TABLEID = 0x300;

		public static final String NAME = "name";
		public static final String DOB = "dob";
		public static final String TAJ = "taj";
		public static final String PHONE = "phone";
		public static final String NOTE = "note";
		public static final String SEARCH = "search";

		public static final String FULL_ID = TABLENAME + "." + _ID;
		public static final String FULL_NAME = TABLENAME + "." + NAME;
		public static final String FULL_DOB = TABLENAME + "." + DOB;
		public static final String FULL_TAJ = TABLENAME + "." + TAJ;
		public static final String FULL_PHONE = TABLENAME + "." + PHONE;
		public static final String FULL_NOTE = TABLENAME + "." + NOTE;
		public static final String FULL_SEARCH = TABLENAME + "." + SEARCH;

		// http://stackoverflow.com/a/15422557 - FOREIGN KEY CSAK AZ UTOLSÓ LEHET!
		static final String TABLECREATE =
				"CREATE TABLE " + TABLENAME + "(" +
						_ID + " INTEGER PRIMARY KEY, " +
						NAME + " TEXT, " +
						DOB + " TEXT, " +
						TAJ + " TEXT, " +
						PHONE + " TEXT, " +
						NOTE + " TEXT, " +
						SEARCH + " TEXT " +	")";

		public static final String AUTHORITY = LibraryDatabaseHelper.AUTHORITY;
		public static final String CONTENT_COUNT = LibraryDatabaseHelper.CONTENT_COUNT;

		private static final String CONTENT_SUBTYPE = "vnd.digitalgarden.librarydb.contentprovider.patient";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_SUBTYPE;

		public static final Uri CONTENT_URI = Uri.parse( "content://" + AUTHORITY + "/" + TABLENAME );
		public static final Uri CONTENT_COUNT_URI = Uri.parse(CONTENT_URI + CONTENT_COUNT);

		public static final int COUNTID = TABLEID + 1;
		public static final int DIRID = TABLEID + 2;
		public static final int ITEMID = TABLEID + 3;
		}


	public static final class PillsTable implements BaseColumns
		{
		private PillsTable() {} // Cannot instantiate class

		public static final String TABLENAME = "pills";
		public static final int TABLEID = 0x400;

		public static final String NAME = "name";
		public static final String SEARCH = "search";

		public static final String FULL_ID = TABLENAME + "." + _ID;
		public static final String FULL_NAME = TABLENAME + "." + NAME;
		public static final String FULL_SEARCH = TABLENAME + "." + SEARCH;

		static final String TABLECREATE =
				"CREATE TABLE " + TABLENAME + " (" +
						_ID + " INTEGER PRIMARY KEY, " +
						NAME + " TEXT," +
						SEARCH + " TEXT" + ")";

		public static final String AUTHORITY = LibraryDatabaseHelper.AUTHORITY;
		public static final String CONTENT_COUNT = LibraryDatabaseHelper.CONTENT_COUNT;

		private static final String CONTENT_SUBTYPE = "vnd.digitalgarden.librarydb.contentprovider.pill";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_SUBTYPE;

		public static final Uri CONTENT_URI = Uri.parse( "content://" + AUTHORITY + "/" + TABLENAME );
		public static final Uri CONTENT_COUNT_URI = Uri.parse(CONTENT_URI + CONTENT_COUNT);

		public static final int COUNTID = TABLEID + 1;
		public static final int DIRID = TABLEID + 2;
		public static final int ITEMID = TABLEID + 3;
		}


	public static final class MedicationsTable implements BaseColumns
		{
		private MedicationsTable() {} // Cannot instantiate class

		public static final String TABLENAME = "medications";
		public static final int TABLEID = 0x500;

		public static final String NAME = "name";
		public static final String SEARCH = "search";

		public static final String FULL_ID = TABLENAME + "." + _ID;
		public static final String FULL_NAME = TABLENAME + "." + NAME;
		public static final String FULL_SEARCH = TABLENAME + "." + SEARCH;

		static final String TABLECREATE =
				"CREATE TABLE " + TABLENAME + " (" +
						_ID + " INTEGER PRIMARY KEY, " +
						NAME + " TEXT," +
						SEARCH + " TEXT" + ")";

		public static final String AUTHORITY = LibraryDatabaseHelper.AUTHORITY;
		public static final String CONTENT_COUNT = LibraryDatabaseHelper.CONTENT_COUNT;

		private static final String CONTENT_SUBTYPE = "vnd.digitalgarden.librarydb.contentprovider.medication";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_SUBTYPE;

		public static final Uri CONTENT_URI = Uri.parse( "content://" + AUTHORITY + "/" + TABLENAME );
		public static final Uri CONTENT_COUNT_URI = Uri.parse(CONTENT_URI + CONTENT_COUNT);

		public static final int COUNTID = TABLEID + 1;
		public static final int DIRID = TABLEID + 2;
		public static final int ITEMID = TABLEID + 3;
		}


	public LibraryDatabaseHelper(Context context)
		{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Logger.note("DATABASE: LibraryDatabaseHelper constructed");
		}
	
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) 
		{
		Logger.note("DATABASE: Create books table: " + BooksTable.TABLECREATE +
				", Create Authors table: " + AuthorsTable.TABLECREATE +
				", Create Patients table: " + PatientsTable.TABLECREATE +
				", Create Pills table: " + PillsTable.TABLECREATE +
				", Create Medications table: " + MedicationsTable.TABLECREATE +
				", Locale: " + Locale.getDefault().toString());
		
		db.execSQL( AuthorsTable.TABLECREATE );
		db.execSQL( BooksTable.TABLECREATE );
		db.execSQL( PatientsTable.TABLECREATE );
		db.execSQL( PillsTable.TABLECREATE );
		db.execSQL( MedicationsTable.TABLECREATE );
		// SQLException - mivel a string konstans, nem feltétlenül kell lekezelni!

		db.setLocale( Locale.getDefault() );
		}
	
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
		Logger.note("DATABASE: Update (drop and recreate) tables...");
		
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + BooksTable.TABLENAME );
		db.execSQL("DROP TABLE IF EXISTS " + AuthorsTable.TABLENAME );
		db.execSQL("DROP TABLE IF EXISTS " + PatientsTable.TABLENAME );
		db.execSQL("DROP TABLE IF EXISTS " + PillsTable.TABLENAME );
		db.execSQL("DROP TABLE IF EXISTS " + MedicationsTable.TABLENAME );
		// SQLException - mivel a string konstans, nem feltétlenül kell lekezelni!
		
		// Create tables again
		onCreate(db);
		}
	
	// CheckColumns megoldása az egyes ...Table osztályokba kerülhet, jelenleg töröltük.
	
	// http://stackoverflow.com/a/3266882
	// http://code.google.com/p/android/issues/detail?id=11607
	// A foreign keys constraint-et engedélyezni kell. Csak 3.6.19 felett, azaz 2.2 verzió felett elérhető
	// Sajnos úgy tűnik, hogy a beindítása sem egyszerű, minden megnyitáskor ki kell adni
	@Override
	public void onOpen(SQLiteDatabase db) 
		{
	    super.onOpen(db);
	    if (!db.isReadOnly()) 
	    	{
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    	}
		}
	}
