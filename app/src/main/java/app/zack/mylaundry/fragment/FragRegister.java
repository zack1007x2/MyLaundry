package app.zack.mylaundry.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.zack.mylaundry.R;
import app.zack.mylaundry.utils.CustomDialogBuilder;
import app.zack.mylaundry.utils.MySharedPer;

public class FragRegister extends BaseFragment implements View.OnClickListener{

    private TextView tv_title_login;
    private Button btn_register;
    private EditText et_email, et_pwd, et_pwd_comfirm;
    private Dialog mRegisterErrorDialog, mMessageDialog, mPWDnotSameDialog;

    MySharedPer perf;

    @Override
    public void onStart() {
        super.onStart();
        et_email.setText(null);
        et_pwd.setText(null);
        et_pwd_comfirm.setText(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_register, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        perf = new MySharedPer(mActivity);
        tv_title_login = (TextView)view.findViewById(R.id.tv_title_login);
        btn_register = (Button)view.findViewById(R.id.btn_register);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_pwd = (EditText)view.findViewById(R.id.et_pwd);
        et_pwd_comfirm = (EditText)view.findViewById(R.id.et_pwd_comfirm);
        tv_title_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        mRegisterErrorDialog = new CustomDialogBuilder(mActivity, R.layout.custom_dialog_message_1_btn)
                .setCustomTitle(R.string.tv_title_error)
                .setCustomMessage(R.string.tv_title_register_error)
                .build();

        mMessageDialog = new CustomDialogBuilder(mActivity, R.layout.custom_dialog_message_1_btn)
                .setCustomTitle(R.string.tv_title_notice)
                .setCustomMessage(R.string.tv_title_fill_in)
                .build();

        mPWDnotSameDialog = new CustomDialogBuilder(mActivity, R.layout.custom_dialog_message_1_btn)
                .setCustomMessage(R.string.tv_error_pwd_comfirm)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_register:
                if(TextUtils.isEmpty(et_email.getText().toString())||
                        TextUtils.isEmpty(et_pwd.getText().toString())||
                        TextUtils.isEmpty(et_pwd_comfirm.getText().toString())){
                    mMessageDialog.show();
                    break;
                }
                if(!et_pwd_comfirm.getText().toString().equals(et_pwd.getText().toString())){
                    mPWDnotSameDialog.show();
                    break;
                }

                if(perf.checkIDExist(et_email.getText().toString())){
                    perf.register(et_email.getText().toString(), et_pwd.getText().toString());
                    mActivity.replaceFragment(R.layout.frag_machine_list);
                }else{
                    mRegisterErrorDialog.show();
                }
                break;
            case R.id.tv_title_login:
                mActivity.replaceFragment(R.layout.frag_login);
                break;
        }
    }
}
