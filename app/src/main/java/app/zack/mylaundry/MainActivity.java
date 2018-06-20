package app.zack.mylaundry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import app.zack.mylaundry.fragment.BaseFragment;
import app.zack.mylaundry.fragment.FragLogin;
import app.zack.mylaundry.fragment.FragMachineList;
import app.zack.mylaundry.fragment.FragRegister;
import app.zack.mylaundry.fragment.FragChart;
import app.zack.mylaundry.fragment.FragUserInfo;

public class MainActivity extends AppCompatActivity  {

    private FragmentManager fragmentManager;
    private Fragment mFragment;
    private FragmentTransaction fragmentTransaction;
    private SparseArray<BaseFragment> navigateMap = new SparseArray<BaseFragment>();
    private Fragment mCurFragment;

    public static final boolean DEBUG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

    }


    private void initFragment() {

        fragmentManager = getSupportFragmentManager();

        navigateMap.clear();
        mapNaviToFragment(R.layout.frag_login, new FragLogin());
        mapNaviToFragment(R.layout.frag_register, new FragRegister());
        mapNaviToFragment(R.layout.frag_machine_list, new FragMachineList());
        mapNaviToFragment(R.layout.frag_user_info, new FragUserInfo());
        mapNaviToFragment(R.layout.frag_report, new FragChart());
        replaceFragment(R.layout.frag_login);

    }


    private void mapNaviToFragment(int id, BaseFragment fragment) {
        if(navigateMap.get(id)==null)
            navigateMap.put(id, fragment);
        else
            throw new IllegalArgumentException("Key Already Exist");
    }

    public void replaceFragment(int viewid) {
        String tag = String.valueOf(viewid);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentframe, navigateMap.get(viewid), tag);
        fragmentTransaction.commit();
        mCurFragment = navigateMap.get(viewid);
    }
    public Fragment getCurrentFragment() {
        return mCurFragment;
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
    public void onBackPressed() {
        if(mCurFragment.getClass() == FragRegister.class){
            replaceFragment(R.layout.frag_login);
        }else if(mCurFragment.getClass() != FragMachineList.class|| mCurFragment.getClass() != FragLogin.class){
            replaceFragment(R.layout.frag_machine_list);
        }
    }


}
