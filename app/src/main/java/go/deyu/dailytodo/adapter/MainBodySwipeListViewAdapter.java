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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import go.deyu.dailytodo.R;
import go.deyu.dailytodo.data.NotificationMessageRM;
import go.deyu.dailytodo.fragment.TimePickerFragment;
import go.deyu.util.LOG;

/**
 * Created by huangeyu on 15/5/19.
 */
public class MainBodySwipeListViewAdapter extends BaseSwipeAdapter {

    private final String TAG = getClass().getSimpleName() ;
    private Context mContext ;
    private LayoutInflater mLayoutInflater;
    private List<NotificationMessageRM> mMessages ;
    private ArrayList<SwipeLayoutListener> mListeners ;


    public MainBodySwipeListViewAdapter(Activity activity, List<NotificationMessageRM> messages){
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
    public View generateView(int position, ViewGroup viewGroup) {
        ViewHolder holder;
        View view = mLayoutInflater.inflate(R.layout.main_body_swipe_list_item, viewGroup, false);
        holder = new ViewHolder(view);
        view.setTag(holder);
        setViewListener(holder);
        LOG.d(TAG, "position "+ position + " generateView " + "message id : " + ((NotificationMessageRM)getItem(position)).getId());
        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        NotificationMessageRM nm = mMessages.get(position);
        ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        setViewValue(viewHolder, position, nm);
        viewHolder.swipeLayout.close();
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

    private void setViewValue(ViewHolder viewHolder , int position , NotificationMessageRM nm ){
        viewHolder.setPosition(position);
//        in swipelayout ellipsize is not work ... so do it my self
        if(nm.getMessage().length()>7){
            viewHolder.message_Tv.setText(nm.getMessage().substring(0,6) + "...");
        }else{
            viewHolder.message_Tv.setText(nm.getMessage());
        }
        viewHolder.time_TV.setText( nm.getHour() + ":" + nm.getMin());
        LOG.d(TAG,"setViewValue nm.getState : " + nm.getState());
        viewHolder.finish_cb.setOnCheckedChangeListener(null);
        viewHolder.finish_cb.setChecked(nm.getState()==NotificationMessageRM.STATE_FINISH ? true : false );
        viewHolder.finish_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int state = isChecked ? NotificationMessageRM.STATE_FINISH : NotificationMessageRM.STATE_NOT_FINISH;
                OnItemStateChanged((int) buttonView.getTag(), state);
            }
        });
    }

    private void setViewListener(ViewHolder viewHolder){
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOG.d(TAG, "position delete OnClick");
                OnItemDelete((int) v.getTag());
            }
        });
        View.OnClickListener ShowTimeDialogListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog((int)v.getTag());
            }
        };
        viewHolder.TimeButton.setOnClickListener(ShowTimeDialogListener);
        viewHolder.time_TV.setOnClickListener(ShowTimeDialogListener);
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


    static class ViewHolder {
        @Bind(R.id.tv_main_body_list_item) TextView message_Tv;
        @Bind(R.id.tv_main_body_time_picker) TextView time_TV;
        @Bind(R.id.cb_finish)CheckBox finish_cb;
        @Bind(R.id.delete) Button deleteBtn;
        @Bind(R.id.swipe) SwipeLayout swipeLayout;
        @Bind(R.id.imgbtn_time)ImageButton TimeButton;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
        public void setPosition(int position){
            deleteBtn.setTag(position);
            finish_cb.setTag(position);
            TimeButton.setTag(position);
            time_TV.setTag(position);
        }
    }
}