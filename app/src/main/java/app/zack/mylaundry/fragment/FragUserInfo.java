package app.zack.mylaundry.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import app.zack.mylaundry.R;
import app.zack.mylaundry.utils.MySharedPer;

public class FragUserInfo extends BaseFragment implements View.OnClickListener{

    Button btnLogot, btnUpdate, btnCancel;
    MySharedPer perf;
    EditText etAccPhone, etAccEmail, etAccName, etAccAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user_info, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        perf = new MySharedPer(mActivity);
        btnLogot = (Button)view.findViewById(R.id.btLogout);
        btnUpdate = (Button)view.findViewById(R.id.btUpdate);
        btnCancel = (Button)view.findViewById(R.id.btCancel);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnLogot.setOnClickListener(this);
        etAccPhone = (EditText)view.findViewById(R.id.etAccPhone);
        etAccEmail = (EditText)view.findViewById(R.id.etAccEmail);
        etAccName = (EditText)view.findViewById(R.id.etAccName);
        etAccAddress = (EditText)view.findViewById(R.id.etAccAddress);
        try {
            JSONArray infos = new JSONArray(perf.getUserInfo());
            etAccName.setText(infos.getString(1));
            etAccPhone.setText(infos.getString(2));
            etAccEmail.setText(infos.getString(3));
            etAccAddress.setText(infos.getString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btLogout:
                perf.logout();
                mActivity.replaceFragment(R.layout.frag_login);
                break;
            case R.id.btUpdate:
                perf.setUserInfo(etAccName.getText().toString().trim()
                        , etAccPhone.getText().toString().trim()
                        , etAccEmail.getText().toString().trim()
                        , etAccAddress.getText().toString().trim()
                );
                mActivity.replaceFragment(R.layout.frag_machine_list);
                break;
            case R.id.btCancel:
                mActivity.replaceFragment(R.layout.frag_machine_list);
                break;
        }
    }
}
