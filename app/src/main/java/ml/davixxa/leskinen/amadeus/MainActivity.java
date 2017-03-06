package ml.davixxa.leskinen.amadeus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.Voice;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    final String TAG = "Amadeus";
    MediaPlayer mediaPlayer;
    SpeechRecognizer sr;
    ImageView leskinen;
    AnimationDrawable animation;
    Handler handler;
    Boolean isLoop = false;
    Boolean isSpeaking = false;
    ArrayList<VoiceLine> voiceLines = new ArrayList<>();
    int antimatter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leskinen = (ImageView) findViewById(R.id.leskinen);
        leskinen.setImageResource(R.drawable.leskinen1a);
        handler = new Handler();
        setupLines();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            sr = SpeechRecognizer.createSpeechRecognizer(this);
            sr.setRecognitionListener(new Listener());
        }
    }

    public void leskinenClick(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            MainActivity host = (MainActivity) view.getContext();

            int permissionCheck = ContextCompat.checkSelfPermission(host, Manifest.permission.RECORD_AUDIO);


            if(!isLoop && !isSpeaking) {

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                    promptSpeechInput();

                } else {
                    speak(new VoiceLine(R.raw.leskinen_nice, Mood.HAPPY));
                }


            }

        } else if (!isLoop && !isSpeaking) {

            promptSpeechInput();

        }

        //mediaPlayer = mediaPlayer.create(getApplicationContext(), R.raw.leskinen_nice);
        //mediaPlayer.start();

    }


    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "ml.davixxa.leskinen.amadeus");
        sr.startListening(intent);


    }

    public void speak(VoiceLine line) {
        try {
            MediaPlayer m = MediaPlayer.create(getApplicationContext(), line.getId());
            final Visualizer v = new Visualizer(m.getAudioSessionId());

            Resources res = getResources();
            animation = (AnimationDrawable) Drawable.createFromXml(res, res.getXml(line.getMood()));


            if (m.isPlaying()) {
                m.stop();
                m.release();
                v.setEnabled(false);
                m = new MediaPlayer();
            }

            m.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    isSpeaking = true;
                    mp.start();
                    v.setEnabled(true);

                }


            });

            m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {

                    isSpeaking = false;
                    mp.release();
                    v.setEnabled(false);

                    runOnUiThread(new Runnable() {


                        public void run() {
                            leskinen.setImageDrawable(animation.getFrame(0));

                        }

                    });

                }

            });

            v.setEnabled(false);
            v.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            v.setDataCaptureListener(
            new Visualizer.OnDataCaptureListener() {
               public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {

                   int sum = 0;
                   for (int i = 1; i < bytes.length; i++) {

                       sum += bytes[i] + 128;

                   }
                   // The normalized volume
                   final float normalized = sum / (float) bytes.length;
                   runOnUiThread(new Runnable() {
                       public void run() {
                           if (normalized > 50) {
                               // Todo: Maybe choose sprite based on previous choice and volume instead of random
                               leskinen.setImageDrawable(animation.getFrame((int) Math.ceil(Math.random() * 2)));

                           } else {
                               leskinen.setImageDrawable(animation.getFrame(0));
                           }
                       }

                   });
               }

               public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {}

            }, Visualizer.getMaxCaptureRate() / 2, true, false);


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void answerSpeech(String input) {

        leskinen = (ImageView) findViewById(R.id.leskinen);
        List<String> choiceofSG = Arrays.asList("It's the choice of Steins Gate");
        Log.e(TAG, input);
        Random randomGen = new Random();
        if(input.contains("ナイス")) {
            switch (randomGen.nextInt(1)) {

                case 0:
                    speak(voiceLines.get(0));
                    break;
                case 1:
                    speak(voiceLines.get(0));
                    break;

            }
            //mediaPlayer = mediaPlayer.create(getApplicationContext(), R.raw.okabe_epk_1);
            //mediaPlayer.start();


        }


        if (input.contains("中二病")) {

            //mediaPlayer = mediaPlayer.create(getApplicationContext(), R.raw.okabe_chuunibyou_combined);
            //mediaPlayer.start();

        }

        if (input.contains("Steins Gate antimatter") || input.contains("antimatter") || input.contains("S G antimatter") || input.contains("St antimatter") || input.contains("St anti matter")) {
            antimatter += 1;
            switch(antimatter) {

                case 0:
                    // Nothing here
                    speak(voiceLines.get(1));
                    break;
                case 1:
                    // Nothing here
                    speak(voiceLines.get(2));
                    break;
                case 2:
                    // Nothing here
                    speak(voiceLines.get(3));
                    break;
                case 3:
                    // Oh yes! Something is here!
                    speak(voiceLines.get(4));
                    break;
                case 4:
                    // What is this?
                    speak(voiceLines.get(5));
                    break;
                case 5:
                    // Like a message!
                    speak(voiceLines.get(6));
                    break;
                case 6:
                    // S;G Antimatter Never!
                    speak(voiceLines.get(7));
                    break;
                case 7:
                    // S;G Antimatter Never!
                    speak(voiceLines.get(8));
                    break;
                case 8:
                    // S;G Antimatter Never!
                    speak(voiceLines.get(9));
                    break;
                case 9:
                    // S;G Antimatter Never!
                    speak(voiceLines.get(10));
                    break;
                case 10:
                    // S;G Antimatter Never!
                    speak(voiceLines.get(11));
                    antimatter = 0;
                    break;





            }


            // Play S;G Antimatter Neva!



        }

        if (input.contains("ルカコ")) {



        }

    }


    private void setupLines() {

        voiceLines.add(new VoiceLine(R.raw.leskinen_nice, Mood.HAPPY));
        voiceLines.add(new VoiceLine(R.raw.antimatter_nothinghere_1, Mood.SURPRISED));
        voiceLines.add(new VoiceLine(R.raw.antimatter_nothinghere_2, Mood.SAD));
        voiceLines.add(new VoiceLine(R.raw.antimatter_nothinghere_3, Mood.ANGRY));
        voiceLines.add(new VoiceLine(R.raw.antimatter_ohyes, Mood.THINKING));
        voiceLines.add(new VoiceLine(R.raw.antimatter_whatisthis, Mood.HAPPY));
        voiceLines.add(new VoiceLine(R.raw.antimatter_likeamessage, Mood.HAPPY));
        voiceLines.add(new VoiceLine(R.raw.antimatter_neva_1, Mood.SURPRISED));
        voiceLines.add(new VoiceLine(R.raw.antimatter_neva_2, Mood.SURPRISED));
        voiceLines.add(new VoiceLine(R.raw.antimatter_neva_3, Mood.SURPRISED));
        voiceLines.add(new VoiceLine(R.raw.antimatter_neva_4, Mood.ANGRY));
        voiceLines.add(new VoiceLine(R.raw.antimatter_neva_5, Mood.ANGRY));





    }



    public class Mood {
        static final int NORMAL = R.drawable.leskinen_1;
        static final int HAPPY = R.drawable.leskinen_2;
        static final int ANGRY = R.drawable.leskinen_3;
        static final int SURPRISED = R.drawable.leskinen_4;
        static final int MISCHIEVOUS = R.drawable.leskinen_5;
        static final int THINKING = R.drawable.leskinen_6;
        static final int SAD = R.drawable.leskinen_7;




    }

    private class Listener implements RecognitionListener {
        final String TAG = "VoiceRec.Listener";

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferRecieved");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            Log.d(TAG, "Error: " +  error);
            sr.cancel();
        }

        @Override
        public void onResults(Bundle results) {
            String input = "";
            Log.d(TAG, "onResults: " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            input += data.get(0);
            answerSpeech(input);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");

        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent: " + eventType);
        }
    }

}



//}
