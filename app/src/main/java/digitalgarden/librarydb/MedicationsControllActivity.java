package digitalgarden.librarydb;


// res:
// http://stackoverflow.com/a/5796606


import android.content.Intent;

public class MedicationsControllActivity extends GeneralControllActivity
	implements MedicationsListFragment.OnListReturnedListener, MedicationsEditFragment.OnFinishedListener
	{

	@Override
	protected GeneralEditFragment createEditFragment()
		{
		return new MedicationsEditFragment();
		}


	@Override
	protected GeneralListFragment createListFragment()
		{
		long pillIdLimit = getIntent().getLongExtra(GeneralListFragment.LIMITED_ITEM, -1L);
		return MedicationsListFragment.newInstance( pillIdLimit );
		}

    // Ez ahhoz kell, hogy a Fragment megkapja a hívást
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
        super.onActivityResult(requestCode, resultCode, data);
        }
    }
