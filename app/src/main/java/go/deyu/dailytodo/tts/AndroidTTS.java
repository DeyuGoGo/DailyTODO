package go.deyu.dailytodo.tts;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by huangeyu on 15/6/24.
 */
public class AndroidTTS implements TTStoSpeak , TextToSpeech.OnInitListener {

    private TextToSpeech TTS;
    private int status = 1 ;
    private Handler mHandler;
    private final String TAG = getClass().getSimpleName();
    private final Context mContext;


    public AndroidTTS(Context context){
        mContext = context;
        mHandler = new Handler();
        TTS = new TextToSpeech(mContext, this) ;
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {
        this.status = status;
        TTS.setLanguage(Locale.ENGLISH);
    }

    public void speak(String message){
        mHandler.post(new SpeakRunnable(message));
    }

    class SpeakRunnable implements Runnable{
        private String message;
        private int reTry = 0;
        public SpeakRunnable(String message){
            this.message = message;
        }
        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            if(status == 1 || reTry > 3 ) {
                if(reTry > 3)
                    return;
                reTry+=1;
                mHandler.postDelayed(this, 3000);
                return;
            }
            if(status == 0){
                TTS.speak(message, TextToSpeech.QUEUE_ADD, null,"UnqineIDCanBeTrace");
            }
        }
    }

//    TODO unbind TTS
    @Override
    public void close() {
        TTS.stop();
        TTS.shutdown();
    }
}
