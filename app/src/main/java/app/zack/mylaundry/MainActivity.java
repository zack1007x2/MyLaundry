package app.zack.mylaundry;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import app.zack.mylaundry.fragment.BaseFragment;
import app.zack.mylaundry.fragment.FragLogin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private Fragment mFragment;
    private FragmentTransaction fragmentTransaction;
    private SparseArray<BaseFragment> navigateMap = new SparseArray<BaseFragment>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void initFragment() {

        fragmentManager = getSupportFragmentManager();

        navigateMap.clear();
        mapNaviToFragment(R.layout.frag_login, new FragLogin());

        hideorshow(fragmentManager, R.layout.frag_login);

    }


    private void mapNaviToFragment(int id, BaseFragment fragment) {
        View view = findViewById(id);

        view.setOnClickListener(this);
        view.setSelected(false);
        navigateMap.put(id, fragment);
    }

    private void hideorshow(FragmentManager fm, int id) {

        String tag = String.valueOf(id);
        fragmentTransaction = fm.beginTransaction();
        if (null == fm.findFragmentByTag(tag)) {
            fragmentTransaction.replace(R.id.content_main, navigateMap.get(id), tag);
        } else {
            fragmentTransaction.show(navigateMap.get(id));
        }
        fragmentTransaction.commit();
        for (int i = 0, size = navigateMap.size(); i < size; i++) {
            int curId = navigateMap.keyAt(i);
            if (curId == id) {
                mFragment = navigateMap.get(id);
                findViewById(id).setSelected(true);
            } else {
                findViewById(curId).setSelected(false);
            }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (navigateMap.indexOfKey(id) >= 0) {

            if (!view.isSelected()) {
                hideorshow(getSupportFragmentManager(), id);
            } else {
                Log.i("Main", " ignore --- selected !!! ");
            }
        }
    }
}
