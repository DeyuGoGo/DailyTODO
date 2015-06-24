package go.deyu.dailytodo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.SQLException;

import go.deyu.dailytodo.fragment.MainBodyFragment;
import go.deyu.dailytodo.fragment.MainHeadFragment;
import go.deyu.dailytodo.model.MessageModel;
import go.deyu.util.LOG;


public class MainActivity extends FragmentActivity {

    private MessageModel messageModel;
    private final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            messageModel = new MessageModel(this);
        } catch (SQLException e){
            LOG.d(TAG , "new model fail : " + e);
        }
        if(messageModel==null){
            Toast.makeText(this,"哎呀，出錯了",Toast.LENGTH_LONG).show();
            return ;
        }
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_head_container) != null && findViewById(R.id.fragment_body_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            MainHeadFragment HeadFragment = new MainHeadFragment();
            MainBodyFragment BodyFragment = new MainBodyFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            HeadFragment.setArguments(getIntent().getExtras());
            BodyFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_head_container, HeadFragment)
                    .add(R.id.fragment_body_container, BodyFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public MessageModel getMessageModel(){
        return messageModel;
    }
}
