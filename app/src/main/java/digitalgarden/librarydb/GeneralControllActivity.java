package digitalgarden.librarydb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import digitalgarden.R;
import digitalgarden.logger.Logger;
import digitalgarden.utils.Keyboard;


// res:
// http://stackoverflow.com/a/5796606

/*
 * GeneralControllActivity
 * 
 *  Minden egyes táblához tartozik egy-egy programrész:
 *  A tábla egészét a ListFragment mutatja be List formában
 *  Az egyes kiválasztott elemeket az EditFragment kezeli
 *  Az Activity e két fragmentumot kontrollálja.
 *  
 *  Csak a fregmenteket elkészítő részt kell a leszármazottaknak elkészíteni:
 *  Fragment createEditFragment()
 *  Fragment cretaeListFragment()
 *
 *  Beállítható Extrák:
 *  	TITLE - a teljes Activity címe
 *  
 *  Visszatéréskor:
 *  	ITEM_SELECTED - a kiválasztott elem id-je
 */

public abstract class GeneralControllActivity extends AppCompatActivity
    {
    public final static String TITLE = "title";

    // Ezeket az értékeket GeneralListFragment definiálja
    // public static final String SELECTED_ITEM = "selected item";
    // public static final long SELECTED_NONE = -2L;
    // public static final long SELECT_DISABLED = -1L;

    // Ezeket az értékeket GeneralEditFragment definiálja
    // public final static String EDITED_ITEM = "edited item";

    // Edit Fragment létrehozásáért felelős rész
    protected abstract GeneralEditFragment createEditFragment();

    // List Fragment létrehozásáért felelős rész
    protected abstract GeneralListFragment createListFragment();

    //boolean flag to know if main FAB is in open or closed state.
    private boolean fabExpanded = false;
    private FloatingActionButton fabSettings;

    //Linear layout holding the Save submenu
    private LinearLayout layoutFabSave;

    //Linear layout holding the Edit submenu
    private LinearLayout layoutFabEdit;
    private LinearLayout layoutFabPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        Logger.note("LibraryControllActivity.onCreate started");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_controll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().hide();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            });

        fabSettings = (FloatingActionButton) this.findViewById(R.id.fabSetting);

        layoutFabSave = (LinearLayout) this.findViewById(R.id.layoutFabSave);
        layoutFabSave.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                fabSettings.show();
                }
            });
        layoutFabEdit = (LinearLayout) this.findViewById(R.id.layoutFabEdit);
        layoutFabEdit.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                fabSettings.hide();
                }
            });

        layoutFabPhoto = (LinearLayout) this.findViewById(R.id.layoutFabPhoto);
        layoutFabPhoto.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Snackbar.make(view, "Photo was taken!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            });


        //When main Fab (Settings) is clicked, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Settings) open/close behavior
        fabSettings.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                if (fabExpanded == true)
                    {
                    closeSubMenusFab();
                    }
                else
                    {
                    openSubMenusFab();
                    }
                }
            });

        //Only main FAB is visible in the beginning
        closeSubMenusFab();


        String title = getIntent().getStringExtra(TITLE);
        if (title != null)
            {
            Logger.note("LibraryControllActivity TITLE set: " + title);
            setTitle(title);
            }
        }

    // https://ptyagicodecamp.github.io/creating-sub-menuitems-for-fab-floating-action-button.html
    //closes FAB submenus
    private void closeSubMenusFab()
        {
        layoutFabSave.setVisibility(View.INVISIBLE);
        layoutFabEdit.setVisibility(View.INVISIBLE);
        layoutFabPhoto.setVisibility(View.INVISIBLE);
        // fabSettings.setImageResource(R.drawable.ic_settings_black_24dp);
        fabExpanded = false;
        }

    //Opens FAB submenus
    private void openSubMenusFab()
        {
        layoutFabSave.setVisibility(View.VISIBLE);
        layoutFabEdit.setVisibility(View.VISIBLE);
        layoutFabPhoto.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        // fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
        }

    // FragmentTransaction csak ettől a ponttól történhet
    @Override
    protected void onResumeFragments()
        {
        super.onResumeFragments();
        Logger.note("LibraryControllActivity.onResumeFragments started");

        FragmentManager fragmentManager = getSupportFragmentManager();

        // LIST és EDIT Fragment is magától megjelenik a látható két Frame-ben
        // Ha nincs LIST, akkor létrehozzuk (ez első indításkor fordulhat elő)
        // Ha nincs EDIT, akkor a hozzá tartozó Frame-t is kikapcsoljuk.
        // Ha van EDIT ÉS PORTRAIT-ban vagyunk, akkor az átfedő LIST Frame-jét kapcsoljuk ki

        Fragment listFrag = fragmentManager.findFragmentByTag("LIST");
        if (listFrag == null)
            {
            listFrag = createListFragment();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.list_frame, listFrag, "LIST");
            fragmentTransaction.commit();

            Keyboard.hide(this);

            Logger.note("new LIST Fragment was created, added");
            }

        Fragment editFrag = fragmentManager.findFragmentById(R.id.edit_frame);
        if (editFrag == null)
            {
            findViewById(R.id.edit_frame).setVisibility(View.GONE);
            Logger.note("EDIT Fragment not found, EDIT Frame GONE");
            }
        else if (findViewById(R.id.landscape) == null)
            {
            findViewById(R.id.list_frame).setVisibility(View.GONE);
            Logger.note("EDIT Fragment found in PORTRAIT mode, LIST Frame GONE");
            }
        else
            {
            Logger.note("EDIT Fragment found in LANDSCAPE mode, LIST and EDIT visible");
            }
        }

    // Listfragment jelzett vissza - elemet választottunk ki
    public void onItemSelected(long id)
        {
        Logger.note("LIST ITEM was selected for returning: " + id);

        // Ezt lehet, h. a fragment is meg tudná tenni
        Intent i = new Intent();
        i.putExtra(GeneralListFragment.SELECTED_ITEM, id);
        setResult(RESULT_OK, i);
        finish();
        }

    public void onItemEditing(long id)
        {
        Logger.note("LIST ITEM was selected for editing: " + id);
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Ameddig van aktív EDIT, addig nem választhatunk újat!
        Fragment editFrag = fragmentManager.findFragmentById(R.id.edit_frame);
        if (editFrag != null)
            {
            Logger.note("New ITEM ignored, previous EDIT is active");
            return;
            }

        editFrag = createEditFragment();
        Bundle args = new Bundle();
        args.putLong(GeneralEditFragment.EDITED_ITEM, id);
        editFrag.setArguments(args);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // http://stackoverflow.com/questions/4817900/android-fragments-and-animation és
        // http://daniel-codes.blogspot.hu/2012/06/fragment-transactions-reference.html
        // fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left , android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.edit_frame, editFrag, "EDIT");
        fragmentTransaction.addToBackStack("LIBDB");
        fragmentTransaction.commit();

        Logger.note("List item selected: New EDIT was created, added");

        findViewById(R.id.edit_frame).setVisibility(View.VISIBLE);
        Logger.note("EDIT Frame VISIBLE");
        if (findViewById(R.id.landscape) == null)
            {
            findViewById(R.id.list_frame).setVisibility(View.GONE);
            Logger.note("PORTRAIT MODE: LIST Frame GONE");
            }
        }

    @Override
    public void onBackPressed()
        {
        // A BACK Billentyűt visszaadjuk editFragment-nek, ha létezik
        // (uis. nem lehet csak a BackStack-et visszaszedni)
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment editFrag = fragmentManager.findFragmentById(R.id.edit_frame);
        if (editFrag != null)
            {
            Logger.note("BACK PRESS was forwarded to EDIT Fragment");
            ((GeneralEditFragment) editFrag).cancelEdit();
            }
        else
            {
            // Mi lesz itt a setResult értéke??
            Logger.note("BACK PRESS (normal way)");
            super.onBackPressed();
            }
        }

    // EditFragment jelzett vissza - befejeztük a szerkesztést.
    public void onFinished()
        {
        FragmentManager fragmentManager = getSupportFragmentManager();
        findViewById(R.id.list_frame).setVisibility(View.VISIBLE);
        Fragment listFrag = fragmentManager.findFragmentById(R.id.list_frame);
        if (listFrag != null)
            {
            ((GeneralListFragment) listFrag).editFinished();
            }

        fragmentManager.popBackStack();
        findViewById(R.id.edit_frame).setVisibility(View.GONE);

        Keyboard.hide(this);
        Logger.note("EDIT Fragment was finished, BackStack was popped");
        }

    // Az Options Menu (Export/Import) letiltva, csak főoldalról elérhető functiók lettek!
    /*
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
		{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.library_controll_menu, menu);
		return true;
		}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 	
		{
		switch (item.getItemId())
			{ 	
			case R.id.menu_export:
				{
				Logger.note("MainActivity Menu: EXPORT started");
				Intent i = new Intent();
				
				i.setClass( this, SelectCsvActivity.class );
				
				i.putExtra( SelectCsvActivity.MODE, SelectCsvActivity.Mode.EXPORT );
				i.putExtra( SelectCsvActivity.FILE_ENDING, getString( R.string.extension ) );
				i.putExtra( SelectCsvActivity.DIRECTORY_SUB_PATH, getString( R.string.directory ) );
				
				startActivity( i );
					
				return true; 	
				}
			case R.id.menu_import:
				{
				Logger.note("MainActivity Menu: IMPORT started");
				Intent i = new Intent();

				i.setClass( this, SelectCsvActivity.class );

				i.putExtra( SelectCsvActivity.MODE, SelectCsvActivity.Mode.IMPORT );
				i.putExtra( SelectCsvActivity.FILE_ENDING, getString( R.string.extension ) );
				i.putExtra( SelectCsvActivity.DIRECTORY_SUB_PATH, getString( R.string.directory ) );

				startActivity( i );
						
				return true; 	
				}
			default: 	
				return super.onOptionsItemSelected(item); 	 
			}
		}
	*/
    }
