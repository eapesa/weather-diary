package android_project.voyager.com.weatherdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android_project.voyager.com.weatherdiary.R;

/**
 * Created by eapesa on 7/22/15.
 */
public class EasterActivity extends Activity implements View.OnClickListener {

    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherdiary_easter_activity);

        mBackButton = (Button) findViewById(R.id.weatherdiary_easter_back_button);
        mBackButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EasterActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weatherdiary_easter_back_button:
                onBackPressed();
                break;
        }
    }
}
