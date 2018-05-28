package digitalgarden.librarydb;


// res:
// http://stackoverflow.com/a/5796606


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
		long initiallySelectedItem = getIntent().getLongExtra(GeneralListFragment.SELECTED_ITEM, MedicationsListFragment.SELECT_DISABLED);
		return MedicationsListFragment.newInstance( initiallySelectedItem );
		}

	}
