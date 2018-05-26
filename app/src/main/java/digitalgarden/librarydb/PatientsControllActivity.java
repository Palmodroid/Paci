package digitalgarden.librarydb;


// res:
// http://stackoverflow.com/a/5796606


public class PatientsControllActivity extends GeneralControllActivity
	implements PatientsListFragment.OnListReturnedListener, PatientsEditFragment.OnFinishedListener
	{

	@Override
	protected GeneralEditFragment createEditFragment()
		{
		return new PatientsEditFragment();
		}


	@Override
	protected GeneralListFragment createListFragment()
		{
		long initiallySelectedItem = getIntent().getLongExtra(GeneralListFragment.SELECTED_ITEM, PatientsListFragment.SELECT_DISABLED);
		return PatientsListFragment.newInstance( initiallySelectedItem );
		}

	}
