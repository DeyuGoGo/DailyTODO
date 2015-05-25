package go.deyu.dailytodo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

import go.deyu.dailytodo.R;
import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.receiver.MessageReceiver;

/**
 * Created by huangeyu on 15/5/19.
 */
public class MainBodySwipeListViewAdapter extends BaseSwipeAdapter {

    private Context mContext ;
    private LayoutInflater mLayoutInflater;
    private ArrayList<NotificationMessage> mMessages ;
    private ArrayList<SwipeLayoutListener> mListeners ;

    public MainBodySwipeListViewAdapter(Activity activity, ArrayList<NotificationMessage> messages){
        this.mContext = activity;
        this.mLayoutInflater = activity.getLayoutInflater();
        this.mMessages = messages;
        this.mListeners = new ArrayList<SwipeLayoutListener>();
    }

    public void registerSwipeLayoutListener(SwipeLayoutListener listener){
        if(listener==null)return;
        mListeners.add(listener);
    }

    public void unregisterSwipeLayoutListener(SwipeLayoutListener listener){
        if(listener==null) return;
        mListeners.remove(listener);
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
    public View generateView(final int position, ViewGroup viewGroup) {
        View v = mLayoutInflater.inflate(R.layout.main_body_swipe_list_item, null);
        Button deleteBtn =(Button)(v.findViewById(R.id.delete));
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemDelete(position);
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView message_Tv = (TextView) convertView.findViewById(R.id.tv_main_body_list_item);
        CheckBox finish_cb = (CheckBox) convertView.findViewById(R.id.cb_finish);
        final NotificationMessage nm = mMessages.get(position);
        message_Tv.setText(nm.getMessage());
        finish_cb.setChecked(nm.getState() == NotificationMessage.STATE_FINISH);
        finish_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int messageid = nm.getId();
                mContext.sendBroadcast(getStateChangeIntent(messageid, isChecked));
            }
        });
        SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(R.id.swipe);
        swipeLayout.close();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    private Intent getStateChangeIntent(int id , boolean isChecked){
        int state = isChecked ? NotificationMessage.STATE_FINISH : NotificationMessage.STATE_NOT_FINISH;
        Intent i = new Intent();
        i.setAction(MessageReceiver.ACTION_FINISH_MESSAGE);
        i.putExtra(MessageReceiver.EXTRA_INT_MESSAGE_ID, id);
        i.putExtra(MessageReceiver.EXTRA_INT_MESSAGE_STATE, state);
        return i ;
    }

    public void OnItemDelete(int position){
        for(SwipeLayoutListener l : mListeners){
            if(l==null)continue;
            l.OnDeleteClick(position);
        }
    }

    public interface SwipeLayoutListener{
        public void OnDeleteClick(int position);
    }
}