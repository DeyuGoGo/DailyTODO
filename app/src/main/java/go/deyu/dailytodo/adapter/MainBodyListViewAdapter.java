package go.deyu.dailytodo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import go.deyu.dailytodo.R;
import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.receiver.MessageReceiver;

/**
 * Created by huangeyu on 15/5/19.
 */
public class MainBodyListViewAdapter extends BaseAdapter{

    private Context mContext ;
    private LayoutInflater mLayoutInflater;
    private ArrayList<NotificationMessage> mMessages ;

    public MainBodyListViewAdapter(Activity activity ,ArrayList<NotificationMessage> messages ){
        this.mContext = activity;
        this.mLayoutInflater = activity.getLayoutInflater();
        this.mMessages = messages;
    }
    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMessages.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = genView();
        }
        viewHolder = (ViewHolder) convertView.getTag();
        NotificationMessage nm = mMessages.get(position);
        viewHolder.message_Tv.setText(nm.getMessage());
        viewHolder.finish_cb.setChecked(nm.getState() == NotificationMessage.STATE_FINISH);
        viewHolder.finish_cb.setTag(new Integer(nm.getId()));
        viewHolder.finish_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id  = (Integer)buttonView.getTag();
                mContext.sendBroadcast(getStateChangeIntent(id , isChecked));
            }
        });
        return convertView;
    }
    private Intent getStateChangeIntent(int id , boolean isChecked){
        int state = isChecked ? NotificationMessage.STATE_FINISH : NotificationMessage.STATE_NOT_FINISH;
        Intent i = new Intent();
        i.setAction(MessageReceiver.ACTION_FINISH_MESSAGE);
        i.putExtra(MessageReceiver.EXTRA_INT_MESSAGE_ID, id);
        i.putExtra(MessageReceiver.EXTRA_INT_MESSAGE_STATE, state);
        return i ;
    }
    private View genView(){
        // General ListView optimization code.
        View view = mLayoutInflater.inflate(R.layout.main_body_list_item, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.message_Tv = (TextView) view.findViewById(R.id.tv_main_body_list_item);
        viewHolder.finish_cb = (CheckBox) view.findViewById(R.id.cb_finish);
        view.setTag(viewHolder);
        return view;
    }
    private class ViewHolder {
        TextView message_Tv;
        CheckBox finish_cb;
    }
}
