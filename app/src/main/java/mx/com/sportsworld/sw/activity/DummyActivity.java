package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.events.EmptyClubsInterface;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.FacebookBranchesFragment;
import mx.com.sportsworld.sw.fragment.FacebookBranchesFragment.OnFavoriteFaceBranchClickListener;
import mx.com.sportsworld.sw.fragment.FavoriteBranchesFragment;
import mx.com.sportsworld.sw.fragment.FavoriteBranchesFragment.OnFavoriteBranchClickListener;
import mx.com.sportsworld.sw.fragment.dialog.FragmentDialogBranches;

// TODO: Auto-generated Javadoc

/**
 * The Class DummyActivity.
 */
public class DummyActivity extends AuthSherlockFragmentActivity implements
		OnFavoriteBranchClickListener, OnFavoriteFaceBranchClickListener,
		EmptyClubsInterface {

	private FragmentDialogBranches branchDialog;
	/** The Constant REQUEST_CODE_UPDATE_BRANCH. */
	private static final int REQUEST_CODE_MARK_BRANCH_AS_FAVORITE = 1;
	private static final int REQUEST_CODE_REMOVE_MARK_BRANCH_AS_FAVORITE = 2;
	private FavoriteBranchesFragment branchFragment;
	private FacebookBranchesFragment facebookFragment;
	private boolean normalAdapter = false;
	private Menu cMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {

			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();

			if (isMember()) {
				Log.d("Sistemas", "Inicio");
				branchFragment = FavoriteBranchesFragment.newInstance();
				branchFragment.setOnBranchListener(this);
				ft.add(android.R.id.content, branchFragment, "branchFragment");
			} else {
				facebookFragment = FacebookBranchesFragment.newInstance();
				ft.add(android.R.id.content, facebookFragment, "branchFragment");

			}
			ft.commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.fragment.FavoriteBranchesFragment.
	 * OnFavoriteBranchClickListener#onFavoriteBranchClick(long)
	 */
	@Override
	public void onFavoriteBranchClick(long id) {
		final Intent goToShowClasses = new Intent(
				getApplicationContext() /* context */,
				GymCalendarActivity.class);
		goToShowClasses.putExtra(GymCalendarActivity.EXTRA_BRANCH_ID, id);
		startActivity(goToShowClasses);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.fragment.FacebookBranchesFragment.
	 * OnFavoriteFaceBranchClickListener#onFavoriteFaceBranchClick(long)
	 */
	@Override
	public void onFavoriteFaceBranchClick(long id) {
		final Intent goToShowClasses = new Intent(
				getApplicationContext() /* context */,
				GymCalendarActivity.class);
		goToShowClasses.putExtra(GymCalendarActivity.EXTRA_BRANCH_ID, id);
		startActivity(goToShowClasses);
	}

	/**
	 * Checks if is member.
	 * 
	 * @return true, if is member
	 */
	public boolean isMember() {
		final SportsWorldAccountManager accountMngr = new SportsWorldAccountManager(
				DummyActivity.this /* context */);
		return accountMngr.isLoggedInAsMember();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu with the options to show the Map and the List.
		if (isMember()) {
			getSupportMenuInflater().inflate(R.menu.menu_favorite, menu);
			cMenu = menu;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_favorite_add:
			if (!normalAdapter)
				openBranchesFragmentDialog();
			return true;
		case R.id.menu_favorite_edit:
			Log.d("Normal Adapter", "" + normalAdapter);
			branchFragment.changeAdapter(normalAdapter);

			if (normalAdapter)
				item.setIcon(getResources().getDrawable(R.drawable.menu_editar));

			//else item.setIcon(getResources().getDrawable(R.drawable.menu_ok));

			normalAdapter = !normalAdapter;
			return true;
		default:
			Log.d("sistemas", "se llama");
			return super.onOptionsItemSelected(item);
		}

	}

	public void openBranchesFragmentDialog() {
		branchDialog = FragmentDialogBranches.newInstance();
		branchDialog.show(getSupportFragmentManager(), "tourInfoDialog");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("sis", "segundo");
		switch (requestCode) {

		case REQUEST_CODE_MARK_BRANCH_AS_FAVORITE:

//			if (resultCode != Activity.RESULT_OK)
//				Toast.makeText(
//						this,
//						getResources().getString(
//								R.string.error_connection_server),
//						Toast.LENGTH_SHORT).show();
//
//			else
//				Toast.makeText(
//						this,
//						getResources().getString(
//								R.string.success_favorite_changes),
//						Toast.LENGTH_SHORT).show();

			if (branchDialog != null)
				branchDialog.dismiss();
			branchFragment.refreshListBranches();
			break;

		case REQUEST_CODE_REMOVE_MARK_BRANCH_AS_FAVORITE:
//			if (resultCode != Activity.RESULT_OK)
//				Toast.makeText(
//						this,
//						getResources().getString(
//								R.string.error_connection_server),
//						Toast.LENGTH_SHORT).show();
//
//			else
//				Toast.makeText(
//						this,
//						getResources().getString(
//								R.string.error_favorite_changes),
//						Toast.LENGTH_SHORT).show();

			if (branchDialog != null)
				branchDialog.dismiss();
			branchFragment.refreshListBranches();
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;

		}

	}

	@Override
	public void onBranchesEmty(boolean isEmpty) {
		// TODO Auto-generated method stub
		Log.d("", "nada");
		MenuItem bedMenuItem = cMenu.findItem(R.id.menu_favorite_edit);
		bedMenuItem.setEnabled(!isEmpty);
		if (isEmpty) {
			bedMenuItem.setIcon(getResources().getDrawable(
					R.drawable.menu_editar));
			normalAdapter = false;
		}
	}
}
