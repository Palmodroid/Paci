package digitalgarden.librarydb.exportimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper;
import digitalgarden.logger.Logger;

 
class AsyncTaskImport extends TimeConsumingAsyncTask 
	{
	// Átadott adatok
	File inputFile;
	
	protected AsyncTaskImport(AsyncTaskDialogFragment asyncTaskDialogFragment, File inputFile)
		{
		super(asyncTaskDialogFragment);
		Logger.note("AsyncTaskIMPORT from " + inputFile.getName());
		
		this.inputFile = inputFile;
		}
	
	private int length = 0;
	
	// Indítás előtt elvégzendő előkészítések
    @Override
    protected void onPreExecute() 
    	{
		// Elvileg az inputFile már megfelelő, de létezését ellenőrizzük
		
/*    	// http://lattilad.org/android/?x=entry:entry121231-185235
		// SD Card nem írható és olvasható
    	if ( !Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState() )) 
    		{
   			setReturnedMessage( R.string.msg_error_sdcard );
    		return;
    		} 

    	// Előkészítjük a könyvtárat. Ez később igen nehéz, mert a cursorról is gondoskodni kell 
    	File directory = new File(Environment.getExternalStorageDirectory(), applicationContext.getString(R.string.directory));
		if ( !directory.exists() ) 
			{
   			setReturnedMessage( R.string.msg_error_directory );
			}
		
		File inputFile = new File(directory, applicationContext.getString(R.string.file));
*/		
		if ( inputFile==null || !inputFile.exists() || !inputFile.isFile())
			{
   			setReturnedMessage( R.string.msg_error_file );
			}
		
		length = (int) inputFile.length(); 
		// Nem jó az átalakítás!!!!
		// És ráadásul long-ról int-re is alakítunk
		
		Logger.debug("AsyncTaskIMPORT file length:" + length );
		
    	callerFragment.setProgressMax( length );
   		callerFragment.updateLayout();
    	}

    
	// A tényleges, háttérben zajló feladat
	// UI-szál elérése TILOS!
	@Override
	protected Void doInBackground(Void... params) 
		{
		if ( !isRunning() )
			return null;

		BufferedReader bufferedReader = null;
		try
			{
	    	// File directory = new File(Environment.getExternalStorageDirectory(), applicationContext.getString(R.string.directory));
			// File inputFile = new File(directory, applicationContext.getString(R.string.file));

			// FileInputStream fileInputStream = new FileInputStream( inputFile );
			// InputStreamReader inputStreamReader = new InputStreamReader( fileInputStream, "UTF-8" );
			// bufferedReader = new BufferedReader( inputStreamReader, 1024 );
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( inputFile ), "UTF-8" ) );
			
			int count = 0;
			String row;
			String[] records;
			
			AuthorsTableExportImport authorsImport = new AuthorsTableExportImport( applicationContext );
			BooksTableExportImport booksImport = new BooksTableExportImport( applicationContext );

			while ( (row=bufferedReader.readLine()) != null )
				{
				records = row.split("\\t", -1);

				if (count == 0)
					{
					// Ez a legelső sor ellenőrzése
					if (records.length < 2 ||
						!records[0].equals( LibraryDatabaseHelper.DATABASE_NAME ) ||
						!records[1].equals( Integer.toString(LibraryDatabaseHelper.DATABASE_VERSION)) )
						{
						setReturnedMessage( R.string.msg_error_database_version );
						Logger.note( "[" + row + "] and database: " + LibraryDatabaseHelper.DATABASE_NAME + " (" + LibraryDatabaseHelper.DATABASE_VERSION + ") does not match!");
						break;
						}
					else
						{
						Logger.debug( "Database: " + LibraryDatabaseHelper.DATABASE_NAME + " (" + LibraryDatabaseHelper.DATABASE_VERSION + ") matches!");
						}
					}

				else if (records.length < 2)
					{
					// Nincs, legfeljebb az elso rekord
					Logger.debug( "Empty row!");
					}
				
				else if ( records[0].equals( authorsImport.getTableName() ))
					{
					authorsImport.importRow( records );
					}
				
				else if ( records[0].equals( booksImport.getTableName() ))
					{
					booksImport.importRow( records );
					}
				
				else
					{
					Logger.note("[" + row + "]: malformed row skipped!");
					}
				
				count += row.length()+1;
				publishProgress( count );

				if (isCancelled())
					break;
				}

			// A file hosszat a beolvasott karakterekkel vetjuk ossze
			// Ez az utf kodolas miatt nem lesz pontos, de a puffereles miatt nem latjuk, hol tartunk
			// Itt javitjuk a hibat, es 100%-t szimulalunk
			if ( row == null )
				publishProgress( length );
		
			}
		catch (IOException ioe)
			{
   			setReturnedMessage( R.string.msg_error_io + ioe.toString());
     		return null;
			}
		finally 
			{
			if (bufferedReader != null) 
				{
				try 
					{
					bufferedReader.close();
					}
				catch (IOException ioe)
					{
					Logger.note("ERROR IN CLOSE (AsyncTaskImport) " + ioe.toString());
					}
				}
			}
		return null;
		}      
	}   
