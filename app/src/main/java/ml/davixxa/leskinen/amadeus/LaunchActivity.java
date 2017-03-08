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
    TextView status;
    ImageView connect, cancel;
    AnimationDrawable logo;
    ImageView imageViewLogo;

    Boolean isPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        connect = (ImageView) findViewById(R.id.imgConnect);
        cancel = (ImageView) findViewById(R.id.imgCancel);
        status = (TextView) findViewById(R.id.tvStatus);
        connect.setImageResource(R.drawable.connect_normal);
        connect.setImageResource(R.drawable.cancel_normal);


    }

    public void cancelClick(View view) {

        cancel.setImageResource(R.drawable.cancel_selected);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        

    }

    public void connectClick(View view) {
        try {
            isPressed = true;
            connect.setImageResource(R.drawable.connect_selected);
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
                    connect.setImageResource(R.drawable.connect_normal);


                }
            });

        } catch (Exception e) {

            e.printStackTrace();

        }


    }


    protected void onResume() {
        if (isPressed) {
            status.setText(R.string.disconnected);
        } else  {
            //else if {
            //}
            status.setText(R.string.connect_to_leskinen);

        }
        isPressed = false;
        cancel.setImageResource(R.drawable.cancel_normal);
        connect.setImageResource(R.drawable.connect_normal);
        super.onResume();

    }

}
