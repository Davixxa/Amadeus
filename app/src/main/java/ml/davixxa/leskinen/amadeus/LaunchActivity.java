package ml.davixxa.leskinen.amadeus;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LaunchActivity extends AppCompatActivity {
    ImageView connectTBI, cancelTBI;
    TextView statusTBI;
    ImageView connect;
    AnimationDrawable logo;
    ImageView imageViewLogo;

    Boolean isPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        connect = (ImageView) findViewById(R.id.imgConnect);



    }

    public void connectClick(View view) {
        try {
            isPressed = true;
            //connect.setImageDrawable(R.drawable.connect_normal);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.tone);

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {


                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    //Set status
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);


                }
            });

        } catch (Exception e) {

            e.printStackTrace();

        }


    }
}
