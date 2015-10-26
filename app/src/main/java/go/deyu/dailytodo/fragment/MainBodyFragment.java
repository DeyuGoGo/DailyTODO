package go.deyu.dailytodo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import butterknife.InjectView;
import butterknife.OnItemLongClick;
import go.deyu.dailytodo.R;
import go.deyu.dailytodo.adapter.MainBodySwipeListViewAdapter;
import go.deyu.dailytodo.data.NotificationMessageRM;
import go.deyu.dailytodo.model.OnMessageChangeListener;
import go.deyu.util.LOG;

/**
 * Created by huangeyu on 15/5/18.
 */
public class MainBodyFragment extends BaseMessageFragment implements OnMessageChangeListener {

    private final String TAG = getClass().getSimpleName();
    private final Handler mUIHandler;
    private final int WHAT_MESSAGE_CHANGE = 0x0001;
    private final int WHAT_MESSAGE_ERROR = 0x0099;
    private MainBodySwipeListViewAdapter adapter;
    private MainBodySwipeListViewAdapter.SwipeLayoutListener listener ;

    @InjectView(R.id.fragment_main_body_listview)ListView bodyListView;

    @OnItemLongClick(R.id.fragment_main_body_listview)
    boolean deleteMessage(int position){
        LOG.d(TAG, "deleteMessage position : " + position);
        model.deleteMessage((int)adapter.getItemId(position));
        return true;
    }


    public MainBodyFragment(){
        mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case WHAT_MESSAGE_CHANGE:
                        refreshMessage();
                        break;
                    case WHAT_MESSAGE_ERROR:
                        if(getActivity()!=null)Toast.makeText(getActivity() , "Error", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
        listener = new MainBodySwipeListViewAdapter.SwipeLayoutListener() {
            @Override
            public void OnDeleteClick(int position) {
                deleteMessage(position);
            }
            @Override
            public void OnStateChanged(int position, int state) {
                LOG.d(TAG, "changeMessageState position : " + position + " state : " + state);
                model.changeMessageState((int)adapter.getItemId(position) , state);
            }
            @Override
            public void OnTimeSetChanged(int position, int hour, int min) {
                LOG.d(TAG , "changeMessageTime position " + position  + " hour : " + hour + " min : " + min);
                model.changeMessageAlarmTime((int)adapter.getItemId(position) , hour , min);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_body , container , false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MainBodySwipeListViewAdapter(getActivity(), model.getMessages());
        bodyListView.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
        adapter.registerSwipeLayoutListener(listener);
        model.registerListener(this);
        for(NotificationMessageRM m : model.getMessages()){
            LOG.d(TAG,"message " + m);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mUIHandler.sendEmptyMessage(WHAT_MESSAGE_CHANGE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.unregisterSwipeLayoutListener(listener);
    }

    @Override
    public void onChange() {
        mUIHandler.sendEmptyMessage(WHAT_MESSAGE_CHANGE);
    }

    @UiThread
    private void refreshMessage(){
        model.refreshMessage();
        adapter.notifyDataSetChanged();
    }

}
