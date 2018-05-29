package cm.uds.iutfv.gi.lir.controleacces;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.ProgressBar;


import cm.uds.iutfv.gi.lir.blundell.tut.task.LoadingTask;
import cm.uds.iutfv.gi.lir.blundell.tut.task.LoadingTask.LoadingTaskFinishedListener;
import cm.uds.iutfv.gi.lir.controleacces.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity implements LoadingTaskFinishedListener {

    MediaPlayer splashSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashSound = MediaPlayer.create(getApplicationContext(), R.raw.splash_sound);
        splashSound.start();


        // Show the splash screen
        setContentView(R.layout.activity_splash);
        // Find the progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);
        // Start your loading
        new LoadingTask(progressBar, this).execute("www.google.co.uk"); // Pass in whatever you need a url is just an example we don't use it in this tutorial
    }

    // This is the callback for when your async task has finished
    @Override
    public void onTaskFinished() {
        completeSplash();
    }

    private void completeSplash(){
        splashSound.release();
        startApp();
        finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }

    private void startApp() {
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        splashSound.release();
        finish();
    }
}
