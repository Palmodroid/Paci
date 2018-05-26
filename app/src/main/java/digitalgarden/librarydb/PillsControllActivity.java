package digitalgarden.librarydb;


// res:
// http://stackoverflow.com/a/5796606


public class PillsControllActivity extends GeneralControllActivity
	implements PillsListFragment.OnListReturnedListener, PillsEditFragment.OnFinishedListener
	{

	@Override
	protected GeneralEditFragment createEditFragment()
		{
		return new PillsEditFragment();
		}


	@Override
	protected GeneralListFragment createListFragment()
		{
		long initiallySelectedItem = getIntent().getLongExtra(GeneralListFragment.SELECTED_ITEM, PillsListFragment.SELECT_DISABLED);
		return PillsListFragment.newInstance( initiallySelectedItem );
		}

	}
