package app.zack.mylaundry.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;

import app.zack.mylaundry.Interface.IDialogValueListener;
import app.zack.mylaundry.R;

public class CustomDialogBuilder {

    private int mLayoutId;
    private Context mContext;
    private OnClickListener mPositiveOnClickListener;
    private IDialogValueListener mValueListener;
    private EditText saveDialogEdittext;
    private OnItemClickListener mItemClickListener;
    private String mCustomTitle, mCustomMessage;
    private boolean cancelable = true;
    private Runnable mAutoRunnable, mVersionInfoRunnable;
    private MyLogger Log = MyLogger.getLogger(getClass().getSimpleName());
    CalendarView calendarView;

    private Handler mHandler = new Handler();

    public CalendarView getCalendarView() {
        return calendarView;
    }



    public CustomDialogBuilder(Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
    }
    public CustomDialogBuilder setOnClickListener(OnClickListener listener) {
        mPositiveOnClickListener = listener;
        return this;
    }

    public CustomDialogBuilder setValueRetListener(IDialogValueListener valueListener) {
        mValueListener = valueListener;
        return this;
    }

    public CustomDialogBuilder setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
        return this;
    }

    public CustomDialogBuilder setCustomMessage(String message) {
        this.mCustomMessage = message;
        return this;
    }

    public CustomDialogBuilder setCustomMessage(int strId) {
        this.mCustomMessage = mContext.getResources().getString(strId);
        return this;
    }

    public CustomDialogBuilder setCustomTitle(String title) {
        this.mCustomTitle = title;
        return this;
    }

    public CustomDialogBuilder setCustomTitle(int titleStrId) {
        this.mCustomTitle = mContext.getResources().getString(titleStrId);
        return this;
    }

    public CustomDialogBuilder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }



    public Dialog build() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (mPositiveOnClickListener == null) {
            mPositiveOnClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            };
        }

        OnClickListener negativeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };

        switch (mLayoutId) {
            case R.layout.custom_dialog_loading:
                dialog.setContentView(mLayoutId);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                if (mCustomMessage != null && !TextUtils.isEmpty(mCustomMessage))
                    ((TextView) dialog.findViewById(R.id.tv_loading_dialog_message)).setText(mCustomMessage);
                else
                    ((TextView) dialog.findViewById(R.id.tv_loading_dialog_message)).setVisibility(View.GONE);
                break;
            case R.layout.custom_dialog_message_1_btn:
                dialog.setContentView(mLayoutId);
                dialog.findViewById(R.id.btConfirm).setOnClickListener(mPositiveOnClickListener);
                TextView tvTitle_1btn = ((TextView) dialog.findViewById(R.id.tvTitle));
                if(mCustomTitle==null || TextUtils.isEmpty(mCustomTitle))
                    tvTitle_1btn.setVisibility(View.GONE);
                else
                    tvTitle_1btn.setText(mCustomTitle);
                ((TextView) dialog.findViewById(R.id.tvContent)).setText(mCustomMessage);
                break;
            case R.layout.custom_dialog_message_2_btn:
                dialog.setContentView(mLayoutId);
                ((Button) dialog.findViewById(R.id.btConfirm)).setOnClickListener(mPositiveOnClickListener);
                TextView tvTitle = ((TextView) dialog.findViewById(R.id.tvTitle));
                if(mCustomTitle==null || TextUtils.isEmpty(mCustomTitle))
                    tvTitle.setVisibility(View.GONE);
                else
                    tvTitle.setText(mCustomTitle);
                ((TextView) dialog.findViewById(R.id.tvContent)).setText(mCustomMessage);
                ((Button) dialog.findViewById(R.id.btCancel)).setOnClickListener(negativeListener);
                break;

            case R.layout.custom_dialog_dates_picker:
                dialog.setContentView(mLayoutId);
                calendarView = (CalendarView) dialog.findViewById(R.id.calendarView);
                break;



        }

        return dialog;
    }
}
