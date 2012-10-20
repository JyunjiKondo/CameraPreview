
package jp.gr.java_conf.jyukon.camerapreview;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends FragmentActivity implements OnNavigationListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SELECTED_FRAGMENT = "selectedFragment";
    private ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getActionBar();
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.fragment_list, R.layout.spinner_dropdown_item);
        mActionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
        mActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        if (savedInstanceState != null) {
            mActionBar.setSelectedNavigationItem(savedInstanceState.getInt(SELECTED_FRAGMENT, 0));
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            FragmentManager manager = getFragmentManager();
            MissingCameraFragmentDialog dialog = new MissingCameraFragmentDialog();
            dialog.show(manager, "dialog");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SELECTED_FRAGMENT, mActionBar.getSelectedNavigationIndex());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        String[] fragments = getResources().getStringArray(R.array.fragment_list);
        Log.i(TAG,
                String.format("pos:%d id:%d %s", itemPosition, itemId, fragments[itemPosition]
                        + "Fragment"));
        String packageName = getClass().getPackage().getName();
        try {
            String fragmentTag = fragments[itemPosition];
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if (fragment == null) {
                fragment = (Fragment) Class.forName(
                        packageName + "." + fragments[itemPosition] + "Fragment").newInstance();
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment, fragmentTag);
            ft.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
