package go.deyu.dailytodo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import go.deyu.dailytodo.MainActivity;
import go.deyu.dailytodo.model.MessageModel;

/**
 * Created by huangeyu on 15/5/20.
 */
public class BaseMessageFragment extends Fragment {
    protected MessageModel model ;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof MainActivity){
            model = ((MainActivity)getActivity()).getMessageModel();
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }
}
