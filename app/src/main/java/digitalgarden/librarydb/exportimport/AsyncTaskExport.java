package digitalgarden.librarydb.exportimport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import digitalgarden.R;
import digitalgarden.librarydb.database.LibraryDatabaseHelper;
import digitalgarden.logger.Logger;

 
class AsyncTaskExport extends TimeConsumingAsyncTask 
	{
	// Átadott adatok
	File outputFile;
	
	protected AsyncTaskExport(AsyncTaskDialogFragment asyncTaskDialogFragment, File outputFile)
		{
		super(asyncTaskDialogFragment);
		Logger.note("AsyncTaskEXPORT to " + outputFile.getName());
		
		this.outputFile = outputFile;
		}
	
	// Indítás előtt elvégzendő előkészítések
	// onPreExecute kimarad, mert:
	// az adatbázislekérdezés (lassú lehet) is háttérszálra került
	// az elemszám kiszámítása is ott történik
    
	// A tényleges, háttérben zajló feladat
	// UI-szál elérése TILOS!
	@Override
	protected Void doInBackground(Void... params) 
		{
		if ( !isRunning() )
			return null;

		GeneralTableExportImport authorsExport = new AuthorsTableExportImport( applicationContext );
		GeneralTableExportImport booksExport = new BooksTableExportImport( applicationContext );
		GeneralTableExportImport pillsExport = new PillsTableExportImport( applicationContext );
		GeneralTableExportImport patientsExport = new PatientsTableExportImport( applicationContext );
		GeneralTableExportImport medicationsExport = new MedicationsTableExportImport( applicationContext );

		// Elkérjük az adatokat, ezt majd a finally-ban zárjuk le
		int count = authorsExport.collateRows();
		count += booksExport.collateRows();
		count += pillsExport.collateRows();
		count += patientsExport.collateRows();
		count += medicationsExport.collateRows();

		// Itt állítjuk be a progress végértékét a 2. paraméter használatával
		int cnt = 0;
		publishProgress( cnt, count );

    	if ( count == 0 )
    		{
    		// Üres az adatbázis, de végigfutunk, és a feljlécet kiírjuk
   			setReturnedMessage( R.string.msg_error_database_empty);
    		}

		// http://stackoverflow.com/questions/15799157/is-it-overkill-to-use-bufferedwriter-and-bufferedoutputstream-together
		BufferedWriter bufferedWriter = null;
		try
			{
			bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( outputFile ), "UTF-8" ) );
			
			bufferedWriter.append( LibraryDatabaseHelper.DATABASE_NAME );
			bufferedWriter.append('\t');
			bufferedWriter.append( Integer.toString(LibraryDatabaseHelper.DATABASE_VERSION) );
			bufferedWriter.append('\t');
			
			SimpleDateFormat sdf=new SimpleDateFormat( "yy-MM-dd (EEE) HH:mm", Locale.US );	
			bufferedWriter.append( "exported on " + sdf.format(new Date()) );
			bufferedWriter.append('\n');
			
			String data;
			
			// http://stackoverflow.com/questions/10723770/whats-the-best-way-to-iterate-an-android-cursor
			while ( (data=authorsExport.getNextRow()) != null ) 
				{
				Logger.note("AsyncTaskEXPORT exporting: " + data);
				// http://stackoverflow.com/questions/5949926/what-is-the-difference-between-append-and-write-methods-of-java-io-writer
				bufferedWriter.append( data );

				publishProgress( ++cnt );

				if (isCancelled())
					break;
				}

			while ( (data=booksExport.getNextRow()) != null ) 
				{
				Logger.note("AsyncTaskEXPORT exporting: " + data);
				// http://stackoverflow.com/questions/5949926/what-is-the-difference-between-append-and-write-methods-of-java-io-writer
				bufferedWriter.append( data );

				publishProgress( ++cnt );

				if (isCancelled())
					break;
				}

            while ( (data=pillsExport.getNextRow()) != null )
                {
                Logger.note("AsyncTaskEXPORT exporting: " + data);
                // http://stackoverflow.com/questions/5949926/what-is-the-difference-between-append-and-write-methods-of-java-io-writer
                bufferedWriter.append( data );

                publishProgress( ++cnt );

                if (isCancelled())
                    break;
                }

            while ( (data=patientsExport.getNextRow()) != null )
                {
                Logger.note("AsyncTaskEXPORT exporting: " + data);
                // http://stackoverflow.com/questions/5949926/what-is-the-difference-between-append-and-write-methods-of-java-io-writer
                bufferedWriter.append( data );

                publishProgress( ++cnt );

                if (isCancelled())
                    break;
                }

            while ( (data=medicationsExport.getNextRow()) != null )
                {
                Logger.note("AsyncTaskEXPORT exporting: " + data);
                // http://stackoverflow.com/questions/5949926/what-is-the-difference-between-append-and-write-methods-of-java-io-writer
                bufferedWriter.append( data );

                publishProgress( ++cnt );

                if (isCancelled())
                    break;
                }

            bufferedWriter.flush();
			}
		catch (IOException ioe)
			{
	   		setReturnedMessage( R.string.msg_error_io + ioe.toString());
			}
		finally 
			{
			// Always close the cursor
            medicationsExport.close();
            patientsExport.close();
            pillsExport.close();
			booksExport.close();
			authorsExport.close();

			if (bufferedWriter != null) 
				{
				try 
					{
					bufferedWriter.close();
					}
				catch (IOException ioe)
					{
					Logger.note("ERROR IN CLOSE (AsyncTaskExport) " + ioe.toString());
					}
				}
			}
		return null;
		}      
	}   
