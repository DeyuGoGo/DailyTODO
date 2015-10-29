package go.deyu.dailytodo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import go.deyu.dailytodo.data.NotificationMessageRM;
import go.deyu.dailytodo.fragment.HeadBodyFragmentReplaceable;
import go.deyu.dailytodo.fragment.MainBodyFragment;
import go.deyu.dailytodo.fragment.MainHeadFragment;
import go.deyu.dailytodo.model.MessageModelInterface;
import go.deyu.dailytodo.model.MessageModelRM;
import go.deyu.util.LOG;


public class MainActivity extends FragmentActivity implements HeadBodyFragmentReplaceable {

    private MessageModelInterface<NotificationMessageRM> messageModel;
    private final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            messageModel = new MessageModelRM(this);
        } catch (Exception e){
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
                    .add(R.id.fragment_body_container, BodyFragment , "BodyFragment")
                    .commit();
        }
        showAd();
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

    public MessageModelInterface<NotificationMessageRM> getMessageModel(){
        return messageModel;
    }

    /**
     * do change HeadFragment
     *
     * @param headfragment fragment want to replace old fragment
     */
    public void changeHeadFragment(Fragment headfragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_head_container, headfragment);
        transaction.commit();
    }

    /**
     * do change BodyFragment
     *
     * @param bodyfragment fragment want to replace old fragment
     */

    public void changeBodyFragment(Fragment bodyfragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_body_container, bodyfragment);
        transaction.commit();
    }

    private void showAd(){

        String My_DeviceId = getResources().getString(R.string.my_device_id_md5);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice(My_DeviceId)        // All emulators
                .build();
        if(!request.isTestDevice(this))
            mAdView.loadAd(request);
    }

}
