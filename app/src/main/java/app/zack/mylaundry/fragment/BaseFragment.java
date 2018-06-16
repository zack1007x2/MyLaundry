package app.zack.mylaundry.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.TextView;

import app.zack.mylaundry.MainActivity;

/**
 * Created by Zack on 15/6/16.
 */
public class BaseFragment extends Fragment {
    public MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof MainActivity)
            mActivity = (MainActivity)getActivity();
    }
    public void update(String data, int id){
        ((TextView)this.getView().findViewById(id)).setText(data);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

}
