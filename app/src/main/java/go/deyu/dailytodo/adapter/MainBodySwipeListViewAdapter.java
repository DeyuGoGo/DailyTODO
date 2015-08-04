package go.deyu.dailytodo.adapter;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.io.Serializable;
import java.util.ArrayList;

import go.deyu.dailytodo.R;
import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.fragment.TimePickerFragment;

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
        CheckBox finish_cb = (CheckBox) v.findViewById(R.id.cb_finish);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemDelete(position);
            }
        });
        NotificationMessage nm = mMessages.get(position);
        finish_cb.setChecked(nm.getState() == NotificationMessage.STATE_FINISH);
        finish_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int state = isChecked ? NotificationMessage.STATE_FINISH : NotificationMessage.STATE_NOT_FINISH;
                OnItemStateChanged(position , state);
            }
        });
        TextView time_TV = (TextView)v.findViewById(R.id.tv_main_body_time_picker);
        time_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(position);
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView message_Tv = (TextView) convertView.findViewById(R.id.tv_main_body_list_item);
        TextView time_TV = (TextView)convertView.findViewById(R.id.tv_main_body_time_picker);

        final NotificationMessage nm = mMessages.get(position);
        message_Tv.setText(nm.getMessage());
        time_TV.setText("每日開始提醒時間 " + nm.getHour() + ":" + nm.getMin());
        SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(R.id.swipe);
        swipeLayout.close();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    private void showTimePickerDialog(final int position) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle b = new Bundle();
        b.putSerializable(TimePickerFragment.KEY_ARGUMENTS_LISTENER, new OnTimeSetListenerSeri(position));
        newFragment.setArguments(b);
        FragmentActivity a = (FragmentActivity)mContext;
        newFragment.show(a.getSupportFragmentManager(), "timePicker");
    }



    public void OnItemDelete(int position){
        for(SwipeLayoutListener l : mListeners){
            if(l==null)continue;
            l.OnDeleteClick(position);
        }
    }

    public void OnItemStateChanged(int position , int state){
        for(SwipeLayoutListener l : mListeners){
            if(l==null)continue;
            l.OnStateChanged(position, state);
        }
    }

//    Time picker change call listener
    public void OnTimeSetChanged(@NonNull int position , @NonNull int hour ,@NonNull int min){
        for(SwipeLayoutListener l : mListeners){
            if(l==null)continue;
            l.OnTimeSetChanged(position, hour, min);
        }
    }

    public interface SwipeLayoutListener{
        public void OnDeleteClick(int position);
        public void OnStateChanged(int position , int state);
        public void OnTimeSetChanged(int position , int hour , int min);
    }

    class OnTimeSetListenerSeri implements TimePickerDialog.OnTimeSetListener , Serializable{
        private int position;
        public OnTimeSetListenerSeri (int position){
            this.position = position;
        }
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            OnTimeSetChanged(position , hourOfDay , minute);
        }
    }
}