package be.ugent.oomt.labo4;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Created by elias on 12/01/15.
 */
public class DetailActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            DetailFragment details = new DetailFragment();
            details.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}
