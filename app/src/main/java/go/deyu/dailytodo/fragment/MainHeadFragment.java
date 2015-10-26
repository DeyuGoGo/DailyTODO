package go.deyu.dailytodo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.OnClick;
import go.deyu.dailytodo.R;

/**
 * Created by huangeyu on 15/5/18.
 */
public class MainHeadFragment extends BaseMessageFragment {


    @OnClick(R.id.btn_add)
    void addMessage() {
        showAddDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_head, container, false);
    }

    private void showAddDialog() {
        final EditText et = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle("請輸入訊息").setIcon(
                android.R.drawable.ic_dialog_info).setView(
                et).setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.addMessage(et.getText().toString());
            }
        })
                .setNegativeButton("取消", null).show();
    }
}

