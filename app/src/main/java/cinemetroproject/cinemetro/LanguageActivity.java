package cinemetroproject.cinemetro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by kiki__000 on 16-Nov-14.
 */
public class LanguageActivity extends ActionBarActivity {

        private ListView listview;
        private ArrayAdapter<String> listAdapter;
        private String[] menu = new String[]{"Ελληνικά","English"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_language);

            listview = (ListView)findViewById(R.id.language);

            listAdapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,menu);
            listview.setAdapter(listAdapter);
            //listview.setSelector(android.R.color.holo_blue_bright);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

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

}
