package go.deyu.dailytodo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;

import butterknife.InjectView;
import butterknife.OnItemLongClick;
import go.deyu.dailytodo.R;
import go.deyu.dailytodo.adapter.MainBodyListViewAdapter;
import go.deyu.dailytodo.data.NotificationMessage;
import go.deyu.dailytodo.model.MessageModel;
import go.deyu.util.LOG;

/**
 * Created by huangeyu on 15/5/18.
 */
public class MainBodyFragment extends BaseMessageFragment implements MessageModel.OnMessageChangeListener {

    private final String TAG = getClass().getSimpleName();
    private final Handler mUIHandler;
    private final int WHAT_MESSAGE_CHANGE = 0x0001;
    private final int WHAT_MESSAGE_ERROR = 0x0099;
    private MainBodyListViewAdapter adapter;

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
                        if(getActivity()!=null)Toast.makeText(getActivity() , "Oh~No~Some Error", Toast.LENGTH_SHORT).show();
                        break;

                }
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
        adapter = new MainBodyListViewAdapter(getActivity(), model.getMessages());
        bodyListView.setAdapter(adapter);
        model.registerListener(this);
        for(NotificationMessage m : model.getMessages()){
            LOG.d(TAG,"message " + m);
        }
    }

    @Override
    public void onChange() {
        mUIHandler.sendEmptyMessage(WHAT_MESSAGE_CHANGE);
    }

    private void refreshMessage(){
        try {
            model.refreshMessage();
        } catch (SQLException e) {
            LOG.d(TAG , "refresh error : " + e );
            mUIHandler.sendEmptyMessage(WHAT_MESSAGE_ERROR);
        }
        adapter.notifyDataSetChanged();
    }
}
