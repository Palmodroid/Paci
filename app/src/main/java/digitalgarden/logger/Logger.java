package digitalgarden.logger;

import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;

import java.lang.Process;

public class Logger
	{
	public static final String DEFAULT_FILE_NAME = "libdb.log";
	public static final String DEFAULT_LOG_TAG = "digitalgardenlogger";
	public static final String DEFAULT_DIRECTORY = "";
	
	public static final String OK = "Log ready";
	public static final String OFF = "Log disabled";
	public static final String LOGFILE_ERROR = "LOGFILE ERROR: ";
	
	private static final String SEPARATOR = System.getProperty("line.separator");

	// Teljes mukodest engedelyezi: enable() es disable()
	private static boolean enabled = true;
	// A debug mukodeset engedelyezi: enableDebug() es disableDebug()
	// Ez a kapcsolo csak enabled==true eseten mukodik, egyebkent minden tiltva van
	private static boolean debugEnabled = true;
	// Tiltas eseten em turtenik semmi, de OFF valaszt ad
	
	// System log-ra is atkuldi. NULL eseten tiltva enableSysLog([logTag]) disableSysLog()
	private static String logTag = DEFAULT_LOG_TAG;
	// File-ba irja a log-ot. NULL eseten tiltva enableFileLog([fileName]) disableFileLog()
	private static String fileName = DEFAULT_FILE_NAME;
	// Toast-ra irja a log-ot. NULL eseten tiltva enableToastLog( context ) disableToastLog
	private static Context context = null;
	// EZT CSAK ATMENETILEG, EGY-EGY RESZBEN SZABAD BEKAPCSONI!! MENTES MINDIG KIKAPCSOLJA!!
	
	// megadhato, hogy melyik konyvtarba mentsen az sd kartyan setFileDirectory( dir )
	private static String directoryName = DEFAULT_DIRECTORY;
	
	public static Bundle getState()
		{
		Bundle state = new Bundle( 5 );
		
		state.putBoolean("ENABLED", enabled);
		state.putBoolean("DEBUG_ENABLED", debugEnabled);
		state.putString("LOG_TAG", logTag);
		state.putString("FILE_NAME", fileName);
		state.putString("DIRECTORY_NAME", directoryName);
		context = null; // Ezt veszélyes hosszan használni, mentésnél is kikapcsoljuk!
		
		return state;
		}
	
	public static void setState( Bundle state )
		{
		enabled = state.getBoolean("ENABLED", true);
		debugEnabled = state.getBoolean("DEBUG_ENABLED", true);
		logTag = state.getString("LOG_TAG");
		if ( logTag == null ) 
			logTag = DEFAULT_LOG_TAG;
		fileName = state.getString("FILE_NAME");
		if ( fileName == null )
			fileName = DEFAULT_FILE_NAME;
		directoryName = state.getString("DIRECTORY_NAME");
		if ( directoryName == null )
			directoryName = DEFAULT_DIRECTORY;
		context = null;  // Ezt veszélyes hosszan használni, töltésnél is kikapcsoljuk!
		}

	public static void enable()
		{
		enabled = true;
		}
		
	public static void disable()
		{
		enabled = false;
		}
		
	public static void enableDebug()
		{
		debugEnabled = true;
		}

	public static void disableDebug()
		{
		debugEnabled = false;
		}

	public static void enableSysLog() 
		{
		Logger.logTag = DEFAULT_LOG_TAG;
		}
	
	public static void enableSysLog( String logTag ) 
		{
		Logger.logTag = logTag;
		}

	public static void disableSysLog()
		{
		Logger.logTag = null;
		}
	
	public static void enableFileLog() 
		{
		Logger.fileName = DEFAULT_FILE_NAME;
		}

	public static void enableFileLog( String fileName ) 
		{
		Logger.fileName = fileName;
		}

	public static void disableFileLog()
		{
		Logger.fileName = null;
		}
	
	public static void enableToastLog( Context context ) 
		{
		Logger.context = context;
		}

	public static void disableToastLog()
		{
		Logger.fileName = null;
		}

	public static void setDirectoryName( String directoryName )
		{
		if ( directoryName != null )
			Logger.directoryName = directoryName;
		}
	
	private static File getDirectory()
		{
		File directory = new File( Environment.getExternalStorageDirectory(), directoryName );
		if ( !directory.isDirectory() )
			directory = Environment.getExternalStorageDirectory();
		
		return directory;
		}
	
	private static enum Type
		{
		TITLE,
		NOTE,
		DEBUG,
		ERROR
		};
	
	private static String addText( Type type, String text )
		{
		if ( !enabled )
			return OFF;
			
		if ( type == Type.DEBUG && !debugEnabled )
			return OFF;
			
		addTextToSysLog( type, text );
		addTextToToastLog( type, text );
		return addTextToFileLog( type, text );
		}
	
	private static void addTextToToastLog( Type type, String text )
		{
		// Log a kepernyore
		if ( context != null )
			{
			Toast.makeText( context, text, Toast.LENGTH_SHORT ).show();
			}
		}
	
	private static void addTextToSysLog( Type type, String text )
		{
		// Log a syslog-ba
		if ( logTag != null )
			{
			if ( type == Type.DEBUG )
				Log.d( logTag, text );
			else if ( type == Type.ERROR )
				Log.e( logTag, text );
			else // TITLE es NOTE
				Log.i( logTag, text );
			}
		}
		
	private static String addTextToFileLog( Type type, String text )
		{
		// Log a file-ba
		if ( fileName != null )
			{
			OutputStreamWriter logStream = null;

			try
				{
				File logFile = new File( getDirectory(), fileName);
				logStream = new OutputStreamWriter( new FileOutputStream(logFile, true) );
				if ( type == Type.TITLE )
					logStream.append( SEPARATOR + "   *** " + text + " ***" + SEPARATOR );
				else
					{
					logStream.append( "(" + timeStamp() + ") " );
					if ( type == Type.ERROR )
						logStream.append( "ERROR: " );
					logStream.append( text );
					}
				logStream.append( SEPARATOR );
				logStream.flush();			
				}
			catch (IOException ioe)
				{
				// A hibat a visszateresi ertek mutatja, DE
				// a bekapcsolas fuggvenyeben a tobbi log is kiadhatja
				addTextToToastLog( Type.ERROR, LOGFILE_ERROR + ioe.toString() );
				addTextToSysLog( Type.ERROR, LOGFILE_ERROR + ioe.toString() );
	
				// visszateresben mindig adja
				return LOGFILE_ERROR + ioe.toString();
				}
			finally
				{
				if (logStream != null)
					{
					try 
						{	
						logStream.close();
						}
					catch (IOException ioe)
						{
						// Ezt a hibát végképp nem tudjuk hol jelenteni...
						}
					}
				}
			}
			
		return OK;		
		}
			
	private static String timeStamp()
		{
		SimpleDateFormat sdf=new SimpleDateFormat( "yy-MM-dd HH:mm:ss.SSS", Locale.US );	
		return sdf.format( new Date() );
		}	
	
	public static String note(String text)
		{
		return addText( Type.NOTE, text );
		}
		
	public static String title(String text)
		{
		return addText( Type.TITLE, text );
		}
		
	public static String debug(String text)
		{
		return addText( Type.DEBUG, text );
		}
		
	public static String error(String text)
		{
		return addText( Type.ERROR, text );
		}

	
	
	// Csak a logFile-t tudjuk torolni, ha log es file-log is be van kapcsolva
	public static String clear()
		{
		if ( !enabled)
			return OFF;
		
		if ( fileName == null )
			return OFF;
		
		File logFile = new File( getDirectory(), fileName);
		if ( logFile.delete() )
			{
			// sikeres torlest a tobbi logon is jelezzuk
			addTextToToastLog( Type.NOTE, "<" + fileName + "> cleared." );
			addTextToSysLog( Type.NOTE, "<" + fileName + "> cleared." );

			return OK;
			}
			
		// Sikertelenseg eseten is jelzunk
		addTextToToastLog( Type.ERROR, "Cannot clear <" + fileName + ">!" );
		addTextToSysLog( Type.ERROR, "Cannot clear <" + fileName + ">!" );

		return LOGFILE_ERROR + "Cannot clear <" + fileName + ">!";
		}
		

// Az alabbi metodusok a sysLog-ot kezelik !!

		
	public static String dumpSysLog()
		{
		if ( !enabled ) 
			return OFF;
			
		if ( fileName == null )
			return OFF;
		
		OutputStreamWriter logStream = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		
		try
			{		
			process = Runtime.getRuntime().exec("logcat -d -v time");
			bufferedReader = new BufferedReader( new InputStreamReader( process.getInputStream() ));			

			File logFile = new File( getDirectory(), fileName);
			logStream = new OutputStreamWriter( new FileOutputStream(logFile, true) );
			
			String line;
			logStream.append( SEPARATOR + "--- SYSTEM LOG DUMP on " );
			logStream.append( timeStamp() + "---" + SEPARATOR + SEPARATOR);
			while ( (line = bufferedReader.readLine()) != null )
				{
				logStream.append(line + SEPARATOR);
				}
			logStream.append(SEPARATOR + "--- END OF SYSTEM LOG ---" + SEPARATOR + SEPARATOR);			
			logStream.flush();
			
			// Sikeres befejezes
			addTextToToastLog( Type.NOTE, "<" + fileName + "> system log dump ready" );
			addTextToSysLog( Type.NOTE, "<" + fileName + "> system log dump ready" );
			
			return OK;
			}
		catch (IOException ioe)
			{
			// Sikertelenseg
			addTextToToastLog( Type.ERROR, LOGFILE_ERROR + ioe.toString() );
			addTextToSysLog( Type.ERROR, LOGFILE_ERROR + ioe.toString() );
				
			return LOGFILE_ERROR + ioe.toString();
			}
		finally
			{
			if (logStream != null)
				{
				try 
					{	
					logStream.close();
					}
				catch (IOException ioe)
					{
					// Ezt a hibát végképp nem tudjuk hol jelenteni...
					}
				}
			if (bufferedReader != null)
				{
				try 
					{	
					bufferedReader.close();
					}
				catch (IOException ioe)
					{
					// Ezt a hibát végképp nem tudjuk hol jelenteni...
					}
				}
			if (process != null)
				{
				process.destroy();
				}
			}
		}	
		
		
	public static String clearSysLog()
		{
		if ( !enabled ) 
			return OFF;
		
		Process process = null;

		try
			{		
			process = Runtime.getRuntime().exec("logcat -c");
			
			// Siker
			addTextToToastLog( Type.NOTE, "System log cleared." );
			addTextToFileLog( Type.NOTE, "System log cleared." );
			
			return OK;
			}
		catch (IOException ioe)
			{
			// sikertelen
			addTextToToastLog( Type.ERROR, "SYSLOG ERROR: " + ioe.toString() );
			addTextToFileLog( Type.ERROR, "SYSLOG ERROR: " + ioe.toString() );
			
			return "SYSLOG ERROR: " + ioe.toString();
			}
		finally
			{
			if (process != null)
				{
				process.destroy();
				}
			}
		}	
		
	}
	
