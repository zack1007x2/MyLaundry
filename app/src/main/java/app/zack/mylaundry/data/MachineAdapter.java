package app.zack.mylaundry.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.zack.mylaundry.R;

public class MachineAdapter extends BaseAdapter {

    private List<Machine> mMachineList = new ArrayList<Machine>();
    private Context context;
    private Machine machinedata;

    public MachineAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Machine> machineList){
        this.mMachineList = machineList;
    }

    @Override
    public int getCount() {
        return mMachineList.size();
    }

    @Override
    public Machine getItem(int position) {
        return mMachineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.machine, null);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.machine, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvContent_title = (TextView) convertView
                    .findViewById(R.id.tvContent_title);
            viewHolder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tvTitle);
            viewHolder.tvDateTitle = (TextView) convertView
                    .findViewById(R.id.tvDateTitle);
            viewHolder.ImgContent = (ImageView) convertView
                    .findViewById(R.id.ImgLogo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(mMachineList.size()==0)
            return convertView;
        machinedata = mMachineList.get(position);

        viewHolder.tvContent_title.setText(context.getResources().getIdentifier("txt"+machinedata.getMachineName(),"string", context.getPackageName()));
        viewHolder.tvDateTitle.setText("Rssi : "+machinedata.getRssi());
        viewHolder.tvTitle.setText(machinedata.getMachineName());
        viewHolder.ImgContent.setImageResource(context.getResources().getIdentifier("img"+machinedata.getMachineName(),"mipmap", context.getPackageName()));
        return convertView;
    }

    private class ViewHolder {
        TextView tvContent_title;
        TextView tvTitle, tvDateTitle;
        ImageView ImgContent;
    }


}
