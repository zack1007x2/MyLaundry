package app.zack.mylaundry.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.zack.mylaundry.R;
import app.zack.mylaundry.utils.CustomDialogBuilder;
import app.zack.mylaundry.utils.MySharedPer;

public class FragLogin extends BaseFragment implements View.OnClickListener{

    private Button btn_login;
    private TextView tv_title_register, tv_title_forget;
    private EditText et_email, et_pwd;
    private Dialog mLoginErrorDialog, mMessageDialog, mForgetDialog;
    MySharedPer perf;


    @Override
    public void onStart() {
        super.onStart();
        et_email.setText(null);
        et_pwd.setText(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {
        perf = new MySharedPer(mActivity);
        if(perf.isLogin()){
            mActivity.replaceFragment(R.layout.frag_machine_list);
        }
        btn_login = (Button)view.findViewById(R.id.btn_login);
        tv_title_register = (TextView)view.findViewById(R.id.tv_title_register);
        tv_title_forget = (TextView)view.findViewById(R.id.tv_title_forget);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_pwd = (EditText)view.findViewById(R.id.et_pwd);
        btn_login.setOnClickListener(this);
        tv_title_register.setOnClickListener(this);
        tv_title_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        mMessageDialog = new CustomDialogBuilder(mActivity, R.layout.custom_dialog_message_1_btn)
            .setCustomTitle(R.string.tv_title_notice)
            .setCustomMessage(R.string.tv_title_fill_in)
            .build();

        mLoginErrorDialog = new CustomDialogBuilder(mActivity, R.layout.custom_dialog_message_1_btn)
                .setCustomTitle(R.string.tv_title_error)
                .setCustomMessage(R.string.tv_title_login_error)
                .build();
        mForgetDialog = new CustomDialogBuilder(mActivity, R.layout.custom_dialog_message_1_btn)
                .setCustomMessage(R.string.tv_title_sorry)
                .build();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(TextUtils.isEmpty(et_email.getText().toString())||
                        TextUtils.isEmpty(et_pwd.getText().toString())){
                    Log.e("NULL","PWD EMAIL");
                    mMessageDialog.show();
                    break;
                }
                if(perf.varifyLogin(et_email.getText().toString(), et_pwd.getText().toString())){
                    mActivity.replaceFragment(R.layout.frag_machine_list);
                }else{
                    mLoginErrorDialog.show();
                }
                break;
            case R.id.tv_title_register:
                mActivity.replaceFragment(R.layout.frag_register);
                break;
            case R.id.tv_title_forget:
                mForgetDialog.show();
                break;
        }
    }

}
