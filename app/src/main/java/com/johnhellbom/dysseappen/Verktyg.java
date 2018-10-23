package com.johnhellbom.dysseappen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-06.
 */
public class Verktyg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verktyg);

        Toolbar toolbar = (Toolbar) findViewById(R.id.verktyg_toolbar);
        setSupportActionBar(toolbar);

        OffsetButton button = (OffsetButton) findViewById(R.id.verktyg_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fdb.nu/verktygslada/"));
                startActivity(browserIntent);
            }
        });

    }

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // endregion
}
