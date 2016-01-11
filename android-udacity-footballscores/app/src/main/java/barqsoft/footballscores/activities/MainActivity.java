package barqsoft.footballscores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import barqsoft.footballscores.R;
import barqsoft.footballscores.fragments.PagerFragment;

public class MainActivity extends AppCompatActivity {
    public static int selectedMatchId;
    public static int currentFragment = 2;
    private static String LOG_TAG = "MainActivity"; //NON-NLS
    private static final String SAVE_TAG = "Save Test"; //NON-NLS
    private static String PAGER_FRAGMENT_TAG = "PAGER_FRAGMENT_TAG"; //NON-NLS
    private static String PAGER_CURRENT_FRAGMENT_KEY = "PAGER_CURRENT_FRAGMENT_KEY"; //NON-NLS
    private static String SELECTED_MATCH_KEY = "SELECTED_MATCH_KEY"; //NON-NLS
    private PagerFragment pagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Reached MainActivity onCreate"); //NON-NLS
        if (savedInstanceState == null) {
            pagerFragment = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, pagerFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(SAVE_TAG, "will save"); //NON-NLS
        Log.v(SAVE_TAG, "fragment: " + String.valueOf(pagerFragment.mPagerHandler.getCurrentItem())); //NON-NLS
        Log.v(SAVE_TAG, "selected id: " + selectedMatchId); //NON-NLS

        outState.putInt(PAGER_CURRENT_FRAGMENT_KEY, pagerFragment.mPagerHandler.getCurrentItem());
        outState.putInt(SELECTED_MATCH_KEY, selectedMatchId);
        getSupportFragmentManager().putFragment(outState, PAGER_FRAGMENT_TAG, pagerFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(SAVE_TAG, "will retrive"); //NON-NLS
        Log.v(SAVE_TAG, "fragment: " + String.valueOf(savedInstanceState.getInt(PAGER_CURRENT_FRAGMENT_KEY))); //NON-NLS
        Log.v(SAVE_TAG, "selected id: " + savedInstanceState.getInt(SELECTED_MATCH_KEY)); //NON-NLS

        currentFragment = savedInstanceState.getInt(PAGER_CURRENT_FRAGMENT_KEY);
        selectedMatchId = savedInstanceState.getInt(SELECTED_MATCH_KEY);
        pagerFragment = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, PAGER_FRAGMENT_TAG);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
