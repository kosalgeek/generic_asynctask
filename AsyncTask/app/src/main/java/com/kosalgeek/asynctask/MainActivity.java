package com.kosalgeek.asynctask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PostResponseAsyncTask task = new PostResponseAsyncTask(this);
        task.execute("http://10.0.3.2/client/home.php");
    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
    }
}
