package go.deyu.dailytodo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import go.deyu.dailytodo.R;

/**
 * Created by huangeyu on 15/5/18.
 */
public class SettingHeadFragment extends BaseMessageFragment {


    private HeadBodyFragmentReplaceable mHeadBodyFragmentReplaceable ;

    @OnClick(R.id.back_imgbtn)
    void backToSettingFragment(View v){
        mHeadBodyFragmentReplaceable.changeHeadFragment(new MainHeadFragment());
        mHeadBodyFragmentReplaceable.changeBodyFragment(new MainBodyFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_head, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHeadBodyFragmentReplaceable = (HeadBodyFragmentReplaceable)getActivity();
    }
}

