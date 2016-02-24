package mx.com.sportsworld.sw.activity;



/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.EmptyClubsInterface;
import mx.com.sportsworld.sw.activity.events.FavoriteBranchInterface;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.SpecificBranchTypeListFragment;
import mx.com.sportsworld.sw.fragment.FacebookBranchesFragment.OnFavoriteFaceBranchClickListener;
import mx.com.sportsworld.sw.fragment.SpecificBranchTypeListFragment.OnBranchClickListener;
import mx.com.sportsworld.sw.fragment.dialog.FragmentDialogBranches;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.service.MarkBranchAsFavoriteService;


// TODO: Auto-generated Javadoc

/**
 * The Class SpecificBranchTypeListActivity.
 */
public class SpecificBranchTypeListActivity extends
		AuthSherlockFragmentActivity implements OnBranchClickListener,EmptyClubsInterface,OnFavoriteFaceBranchClickListener,FavoriteBranchInterface {

	/** The Constant EXTRA_TYPE. */
	//public static final String EXTRA_TYPE = "com.sportsworld.android.extra.TYPE";
	public static final String EXTRA_TYPE = "com.upster.extra.TYPE";

	/** The Constant FRAG_TAG_SPECIFIC_BRANCH_TYPE. */
	private static final String FRAG_TAG_SPECIFIC_BRANCH_TYPE = "frag_tag_specific_branch_type";
	private Menu cMenu;
	private boolean normalAdapter = false;
	private SpecificBranchTypeListFragment branchFragment;
	private FragmentDialogBranches branchDialog;
	private static final int REQUEST_CODE_MARK_BRANCH_AS_FAVORITE = 1;
	private static final int REQUEST_CODE_REMOVE_MARK_BRANCH_AS_FAVORITE = 2;
	
	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			getSupportActionBar().setTitle(
				SportsWorldPreferences.getBranchType(this));
			if (isFinishing()) {
				return;
			}
			if(getIntent().getStringExtra(EXTRA_TYPE).equals("favorites")){
				
			}else{
			final String type = getIntent().getStringExtra(EXTRA_TYPE);
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = SpecificBranchTypeListFragment.newInstance(type, this);
			
			branchFragment = (SpecificBranchTypeListFragment) f;
			branchFragment.setOnBranchListener(this);
			
			ft.add(android.R.id.content, f, FRAG_TAG_SPECIFIC_BRANCH_TYPE);
			
			ft.commit();
			}
		}

	}

	/*
	 * Opens Branch Overview Activity
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.SpecificBranchTypeListFragment.OnBranchClickListener#onBranchClick(long)
	 */
	@Override
	public void onBranchClick(long id) {
		final Intent branchOverview = new Intent(this /* context */,
				BranchOverviewActivity.class);
		branchOverview.putExtra(BranchOverviewActivity.EXTRA_BRANCH_ID, id);
		startActivity(branchOverview);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		if(getIntent().getStringExtra(EXTRA_TYPE).equals("favorites")){
			Log.d("Tipo ", "tipo " + getIntent().getStringExtra(EXTRA_TYPE));
			getSupportMenuInflater().inflate(R.menu.menu_favorite, menu);
			cMenu = menu;
			
			final String type = getIntent().getStringExtra(EXTRA_TYPE);
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = SpecificBranchTypeListFragment.newInstance(type, this);
			
			branchFragment = (SpecificBranchTypeListFragment) f;
			branchFragment.setOnBranchListener(this);
			Log.i("LogIron", "seleccionaste alpha");
			SportsWorldPreferences.setSortListBranchPref(
			getApplicationContext().getApplicationContext(), null);
			ft.add(android.R.id.content, f, FRAG_TAG_SPECIFIC_BRANCH_TYPE);
			
			ft.commit();
			
			
			
		}else{
			MenuInflater menuInflater = new MenuInflater(getApplicationContext());
			menuInflater.inflate(R.menu.branch_sort_menu, menu);
		}
			Log.d("Sistemas", "menu para todos");
			return true;
	}
	
	

	/*
	 * Event for sorting
	 */
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(getIntent().getStringExtra(EXTRA_TYPE).equals("favorites")){
			if(getIntent().getStringExtra(EXTRA_TYPE).equals("favorites")){
				Log.i("LogIron", "seleccionaste alpha");
				SportsWorldPreferences.setSortListBranchPref(
						getApplicationContext().getApplicationContext(), null);
			}

			
			switch (item.getItemId()) {
			case R.id.menu_favorite_add:
				//if (!normalAdapter)
					openBranchesFragmentDialog();
					if (normalAdapter){
						MenuItem bedMenuItem = cMenu.findItem(R.id.menu_favorite_edit);
						
						bedMenuItem.setIcon(getResources().getDrawable(
						R.drawable.menu_editar));
						branchFragment.changeAdapter(normalAdapter);
						normalAdapter = !normalAdapter;
					}
				return true;
			case R.id.menu_favorite_edit:
				Log.d("Normal adapter", "" + normalAdapter);
				
				branchFragment.changeAdapter(normalAdapter);
				//branchFragment.changeAdapter(normalAdapter);
				if (normalAdapter)
					item.setIcon(getResources().getDrawable(R.drawable.menu_editar));
				else
					item.setIcon(getResources().getDrawable(R.drawable.menu_ok));

				normalAdapter = !normalAdapter;
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}

		}else{
		
		if (item.getItemId() == R.id.menu_sort_alpha) {
			Log.i("LogIron", "seleccionaste alpha");
			SportsWorldPreferences.setSortListBranchPref(
					getApplicationContext().getApplicationContext(), null);

		}
		if (item.getItemId() == R.id.menu_sort_distance) {
			Log.i("LogIron", "seleccionaste distance");
			SportsWorldPreferences.setSortListBranchPref(
					getApplicationContext().getApplicationContext(), "73");
		}
		
		SpecificBranchTypeListFragment fragmentType = (SpecificBranchTypeListFragment) getSupportFragmentManager()
				.findFragmentByTag(FRAG_TAG_SPECIFIC_BRANCH_TYPE);

		fragmentType.onSortChanged();
		}
		return true;
	}
	
	public void openBranchesFragmentDialog() {
		branchDialog = FragmentDialogBranches.newInstance();
		branchDialog.show(getSupportFragmentManager(), "tourInfoDialog");
	}

	@Override
	public void onBranchesEmty(boolean isEmpty) {
		// TODO Auto-generated method stub
		Log.d("", "nada2");
		MenuItem bedMenuItem = cMenu.findItem(R.id.menu_favorite_edit);
		bedMenuItem.setEnabled(!isEmpty);
		if (isEmpty) {
			bedMenuItem.setIcon(getResources().getDrawable(
					R.drawable.menu_editar));
			normalAdapter = true;
			//branchFragment.changeAdapter(normalAdapter);
		}
	}

	@Override
	public void onFavoriteFaceBranchClick(long id) {
		// TODO Auto-generated method stub
		final Intent goToShowClasses = new Intent(
				getApplicationContext() /* context */,
				GymCalendarActivity.class);
		goToShowClasses.putExtra(GymCalendarActivity.EXTRA_BRANCH_ID, id);
		startActivity(goToShowClasses);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("sis", "segundo");
		switch (requestCode) {

		case REQUEST_CODE_MARK_BRANCH_AS_FAVORITE:

//			
//				Toast.makeText(
//						this,
//						getResources().getString(
//								R.string.error_connection_server),
//						Toast.LENGTH_SHORT).show();
//
//			
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
			
//				Toast.makeText(
//						this,
//						getResources().getString(
//								R.string.error_connection_server),
//						Toast.LENGTH_SHORT).show();
//
//			
//				Toast.makeText(
//						this,
//						getResources().getString(
//								R.string.error_favorite_changes),
//						Toast.LENGTH_SHORT).show();

			if (branchDialog != null)
				branchDialog.dismiss();
			//branchFragment.onSortChanged();
			branchFragment.refreshListBranches();
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;

		}

	}

	@Override
	public void removeItem(long itemId) {
		// TODO Auto-generated method stub
		Log.d("Eliminar ", "Id " + itemId);
		removeMarkBranchAsFavoriteService(itemId);
	}
	
	private void removeMarkBranchAsFavoriteService(long idBranch) {

		final Intent data = new Intent();
		final Activity activity = this;
		final PendingIntent pendingIntent = activity
				.createPendingResult(
						REQUEST_CODE_REMOVE_MARK_BRANCH_AS_FAVORITE, data, 0 /* flags */);
		final Intent markBranchAsFavoriteService = new Intent(activity,
				MarkBranchAsFavoriteService.class);
		markBranchAsFavoriteService.putExtra(
				MarkBranchAsFavoriteService.EXTRA_BRANCH_ID, idBranch);
		markBranchAsFavoriteService.putExtra(
				MarkBranchAsFavoriteService.EXTRA_FAVORITE, false);
		markBranchAsFavoriteService
				.putExtra(MarkBranchAsFavoriteService.EXTRA_PENDING_INTENT,
						pendingIntent);
		activity.startService(markBranchAsFavoriteService);
		
	}

}
