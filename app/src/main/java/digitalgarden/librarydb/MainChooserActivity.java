package digitalgarden.librarydb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.File;

import digitalgarden.librarydb.MainChooserDialogFragment.Type;
import digitalgarden.librarydb.exportimport.AsyncTaskDialogFragment;
import digitalgarden.logger.Logger;
import digitalgarden.R;
import digitalgarden.selectfile.SelectFileActivity;
import digitalgarden.selectfile.SelectFileActivity.Mode;


public class MainChooserActivity extends FragmentActivity
	{
	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		
		Logger.dumpSysLog();
		Logger.clearSysLog();
		
		Logger.title("LibraryDb started");

		setContentView(R.layout.main_chooser_activity);
		
		findViewById(R.id.button_authors_table).setOnClickListener(new OnClickListener()
    		{
    		public void onClick(View view) 
    			{
    			Logger.title("MAINCHOOSER: Authors table called");

				Intent i = new Intent();

				i.setClass( MainChooserActivity.this, AuthorsControllActivity.class );
				startActivity( i );
    			} 
    		});
			
		findViewById(R.id.button_books_table).setOnClickListener(new OnClickListener()
    		{
			public void onClick(View view) 
    			{
    			Logger.title("MAINCHOOSER: Books table called");

				Intent i = new Intent();

				i.setClass( MainChooserActivity.this, BooksControllActivity.class );
				startActivity( i );
    			} 
    		});

		findViewById(R.id.button_patients_table).setOnClickListener(new OnClickListener()
			{
			public void onClick(View view)
				{
				Logger.title("MAINCHOOSER: Patients table called");

				Intent i = new Intent();

				i.setClass( MainChooserActivity.this, PatientsControllActivity.class );
				startActivity( i );
				}
			});

		findViewById(R.id.button_pills_table).setOnClickListener(new OnClickListener()
			{
			public void onClick(View view)
				{
				Logger.title("MAINCHOOSER: Pills table called");

				Intent i = new Intent();

				i.setClass( MainChooserActivity.this, PillsControllActivity.class );
				startActivity( i );
				}
			});

		findViewById(R.id.button_export).setOnClickListener(new OnClickListener()
    		{
    		public void onClick(View view) 
    			{
    			Logger.title("MAINCHOOSER: Export called");
    			startPorting( PortingType.EXPORT );
    			} 
    		});

		findViewById(R.id.button_import).setOnClickListener(new OnClickListener()
    		{
    		public void onClick(View view) 
    			{
    			Logger.title("MAINCHOOSER: Import called");    			
       			startPorting( PortingType.IMPORT );
       			} 
    		});
		}

	
	static enum PortingType
		{
		EXPORT,
		IMPORT
		};
	
	static enum PortingState
		{
		NO_PORTING,
		SELECT_FILE_FOR_PORTING,	// Ez pl. tök felesleges
		CONFIRM_PORTING
		};
	
	private PortingType portingType; 
	private PortingState portingState = PortingState.NO_PORTING;	
		
	private final static int SELECT_FILE_REQUEST = 1;
	
	protected File portingFile;
	protected String directorySubPath;

	protected void onSaveInstanceState( Bundle state ) 
		{
		super.onSaveInstanceState( state );
		Logger.note("MainChooser: onSaveInstanceState, State: " + portingState.toString() );
		
		state.putSerializable( "PORTING_TYPE", portingType );
		state.putSerializable( "PORTING_STATE", portingState );
		state.putSerializable( "PORTING_FILE", portingFile );
		state.putString( "DIRECTORY_SUB_PATH", directorySubPath );
		}

	public void onRestoreInstanceState( Bundle state ) 
		{
		super.onRestoreInstanceState( state );
		
		if (state != null)
			{
			portingType = (PortingType) state.getSerializable( "PORTING_TYPE" );
			portingState = (PortingState) state.getSerializable( "PORTING_STATE" );
			portingFile = (File) state.getSerializable( "PORTING_FILE" );
			directorySubPath = state.getString( "DIRECTORY_SUB_PATH" );

			Logger.note("MainChooser: onRestoreInstanceState, State: " + portingState.toString() );
			}
		else
			Logger.note("MainChooser: onRestoreInstanceState is NULL!");
		}
	
	protected void startPorting( PortingType type )
		{
		Logger.note("MainChooser: startPorting: " + type.toString() );
		
		portingType = type;
		portingState = PortingState.SELECT_FILE_FOR_PORTING;
		
		Intent i = new Intent();
		
		i.setClass( MainChooserActivity.this, SelectFileActivity.class );
		
		i.putExtra( SelectFileActivity.FILE_ENDING, getString( R.string.extension ) );
		
		if ( directorySubPath == null )
			i.putExtra( SelectFileActivity.DIRECTORY_SUB_PATH, getString( R.string.directory ) );
		else
			i.putExtra( SelectFileActivity.DIRECTORY_SUB_PATH, directorySubPath );
		
		if ( portingType == PortingType.IMPORT )
			{
			i.putExtra( SelectFileActivity.CUSTOM_TITLE, "Select File for Import!");
			i.putExtra( SelectFileActivity.CREATE_ALLOWED, false);
			}
		else // portingType == EXPORT
			{
			i.putExtra( SelectFileActivity.CUSTOM_TITLE, "Select File for Export!");
			i.putExtra( SelectFileActivity.CREATE_ALLOWED, true);
			}

		startActivityForResult( i, SELECT_FILE_REQUEST );
		}
	
	// http://stackoverflow.com/a/18345899
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
		Logger.note("MainChooser: onActivityResult" );

		if ( requestCode == SELECT_FILE_REQUEST )
			{
			if ( resultCode == RESULT_OK )
				{
				// Az a baj, hogy android.net.uri != java.net.uri
				portingFile = new File ( data.getStringExtra( SelectFileActivity.SELECTED_FILE ) );
				directorySubPath = data.getStringExtra( SelectFileActivity.DIRECTORY_SUB_PATH );
				
				portingState = PortingState.CONFIRM_PORTING;
				}
			else
				portingState = PortingState.NO_PORTING;
			}
		}
	
	// Ha Activity visszatérése miatt jöttünk...
	@Override
	protected void onResumeFragments() 
		{
	    super.onResumeFragments();
		Logger.note("MainChooser: onResumeFragments" );

	    if ( portingState == PortingState.CONFIRM_PORTING )
	    	{
			Logger.note("    Confirm Porting part started" );

			if ( portingType == PortingType.IMPORT )
				{
				MainChooserDialogFragment.showNewDialog(this, 
					MainChooserDialogFragment.Type.CONFIRM_IMPORT, portingFile.getName() );
				}
			else if ( portingType == PortingType.EXPORT )
				{
				if ( portingFile.exists() )
					{
					MainChooserDialogFragment.showNewDialog(this, 
						MainChooserDialogFragment.Type.CONFIRM_OVERWRITING_EXPORT, portingFile.getName() );
					}
				else
					{
					MainChooserDialogFragment.showNewDialog(this, 
						MainChooserDialogFragment.Type.CONFIRM_NEW_EXPORT, portingFile.getName() );
					}
				}
	    	}
	    else
			Logger.note("    Normal start, Confirm Porting not yet started" );

		}
	
	public void onDialogPositiveResult( Type type )
		{
		Logger.note("MainChooser: Return from Dialogs: POSITIVE");
		
		portingState = PortingState.NO_PORTING;
		
		switch (type)
			{
			case CONFIRM_IMPORT:
				{
				Logger.note("Confirm Import - Import started");
				
				AsyncTaskDialogFragment asyncTaskDialogFragment = AsyncTaskDialogFragment.newInstance( Mode.IMPORT, portingFile );
				asyncTaskDialogFragment.show(getSupportFragmentManager(), "DIALOG");
				break;
				}
			case CONFIRM_NEW_EXPORT:
			case CONFIRM_OVERWRITING_EXPORT:
				{
				Logger.note("Confirm NEW/OVERWRITING Export - Export Started");
				
				AsyncTaskDialogFragment asyncTaskDialogFragment = AsyncTaskDialogFragment.newInstance( Mode.EXPORT, portingFile );
				asyncTaskDialogFragment.show(getSupportFragmentManager(), "DIALOG");
				break;
				}
			}
		}
	
	public void onDialogCancelled()
		{
		Logger.note("MainChooser: Return from Dialogs: CANCEL");

		startPorting( portingType );
		}
	}
