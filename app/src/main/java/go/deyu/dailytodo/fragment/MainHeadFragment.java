package go.deyu.dailytodo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindString;
import butterknife.OnClick;
import go.deyu.dailytodo.R;

/**
 * Created by huangeyu on 15/5/18.
 */
public class MainHeadFragment extends BaseMessageFragment {


    private HeadBodyFragmentReplaceable mHeadBodyFragmentReplaceable ;
    @BindString(R.string.input_message)String str_Input_Message;
    @BindString(R.string.confirm)String str_Confirm;
    @BindString(R.string.cancel)String str_Cancel;

    @OnClick(R.id.setting_imgbtn)
    void changeToSettingFragment(View v){
        mHeadBodyFragmentReplaceable.changeHeadFragment(new SettingHeadFragment());
        mHeadBodyFragmentReplaceable.changeBodyFragment(new SettingBodyFragment());
    }

    @OnClick(R.id.add_imgbtn)
    void addMessage() {
        showAddDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_head, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHeadBodyFragmentReplaceable = (HeadBodyFragmentReplaceable)getActivity();
    }

    private void showAddDialog() {
        final EditText et = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle(str_Input_Message).setIcon(
                R.drawable.note).setView(
                et).setPositiveButton(str_Confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.addMessage(et.getText().toString());
            }
        })
                .setNegativeButton(str_Cancel, null).show();
    }
}

