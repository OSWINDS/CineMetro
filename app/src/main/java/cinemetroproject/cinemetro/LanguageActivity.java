package cinemetroproject.cinemetro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Locale;

/**
 * Created by kiki__000 on 16-Nov-14.
 */
public class LanguageActivity extends ActionBarActivity {

        private ListView listview;
        private ArrayAdapter<String> listAdapter;
        private String[] menu = new String[]{"Ελληνικά","English"};
        private Button saveButton;
        public static String language;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_language));
            setContentView(R.layout.activity_language);

            listview = (ListView)findViewById(R.id.language);

            listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,menu);
            listview.setAdapter(listAdapter);

            saveButton = (Button) findViewById(R.id.save);
            saveButton.setEnabled(false);
            saveButton.setOnClickListener(saveOnClickListener);

            Log.i("locale", language);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

                if (position == 1) {
                        Configuration c = new Configuration(getResources().getConfiguration());
                        c.locale = new Locale("en", "EN");
                        getResources().updateConfiguration(c, getResources().getDisplayMetrics());
                        DbAdapter.getInstance().changeLanguage(Language.ENGLISH);
                        language="en";
                        saveButton.setEnabled(true);
                    } else {
                        Configuration c = new Configuration(getResources().getConfiguration());
                        c.locale = new Locale("el", "EL");
                        getResources().updateConfiguration(c, getResources().getDisplayMetrics());
                        DbAdapter.getInstance().changeLanguage(Language.GREEK);
                        language="el";
                        saveButton.setEnabled(true);
                    }
                    SharedPreferences sPrefs = getSharedPreferences("myAppsPreferences", 0);
                    sPrefs.edit().putString("lang", language).commit();
                }

            });

        }



        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.language, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.

            switch (item.getItemId()) {

                case android.R.id.home:
                    onBackPressed();
                    return true;
            }

            return super.onOptionsItemSelected(item);
        }

    View.OnClickListener saveOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(LanguageActivity.this, MainMenu.class);
            LanguageActivity.this.startActivity(intent);
            finish();

        }};

    public static void setLocale(){


    }



}
