package com.emm.elephorm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Inikaam on 18/06/2015.
 */
public abstract class AppActivity extends AppCompatActivity {

    private String TAG = AppActivity.class.getSimpleName();

    protected SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     *
     * @param activity
     */
    public void customStartActivity(Class activity) {
        Intent intent = new Intent(this, activity);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Retourne la valeur du champ
     * @param id Identifiant du champ
     * @return Contenu du champ
     */
    public String getFieldValue(int id) {
        TextView valueView = (TextView) findViewById(id);

        return valueView.getText().toString();
    }
}

