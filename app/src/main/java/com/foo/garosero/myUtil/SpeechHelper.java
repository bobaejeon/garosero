package com.foo.garosero.myUtil;

import static android.speech.tts.TextToSpeech.ERROR;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

public class SpeechHelper {
    private LottieAnimationView lottieAnimationView;
    TextToSpeech tts;
    Context context;

    public SpeechHelper(Context context, LottieAnimationView lottieAnimationView, TextToSpeech tts) {
        this.lottieAnimationView = lottieAnimationView;
        this.tts = tts;
        this.context = context;
    }

    public void speak(String text) {
        // set tts
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR){
                    int result = tts.setLanguage(Locale.KOREA); // 언어 선택
                    if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                        Log.e("TTS", "This Language is not supported");
                    }else{
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }else{
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });

        tts.setSpeechRate(1.2f); // set speech late

        // Broadcast receiver
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 어떤 동작으로 인해 알림이 수신되었는지
                String act = intent.getAction();
                // 알림이 TTS의 음성출력이 완료되어져 수신된 경우
                if (act.equals(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED)){
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    // wait 2000ms
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lottieAnimationView.setVisibility(View.GONE);
                            handler.removeMessages(0);
                        }
                    }, 150*text.replace(" ", "").length());
                }
            }
        };

        // intent filter
        IntentFilter filter = new IntentFilter();
        filter.addAction(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        context.registerReceiver(br, filter);
    }


}
