package digitalgarden.librarydb;


// res:
// http://stackoverflow.com/a/5796606


public class AuthorsControllActivity extends GeneralControllActivity
	implements AuthorsListFragment.OnListReturnedListener, AuthorsEditFragment.OnFinishedListener
	{

	@Override
	protected GeneralEditFragment createEditFragment()
		{
		return new AuthorsEditFragment();
		}


	@Override
	protected GeneralListFragment createListFragment()
		{
		long initiallySelectedItem = getIntent().getLongExtra(GeneralListFragment.SELECTED_ITEM, AuthorsListFragment.SELECT_DISABLED);
		return AuthorsListFragment.newInstance( initiallySelectedItem );
		}

	}
