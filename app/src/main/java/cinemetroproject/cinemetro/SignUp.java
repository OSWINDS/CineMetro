package cinemetroproject.cinemetro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cinemetroproject.cinemetro.R;

public class SignUp extends ActionBarActivity {

    Button check_bt,end_bt;
    TextView dispText;
    EditText email,pass1,pass2;
    boolean ok = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        check_bt=(Button)findViewById(R.id.check_button);
        end_bt=(Button) findViewById(R.id.End_button);
        dispText=(TextView)findViewById(R.id.DisplayText);
        email=(EditText)findViewById(R.id.eMail);
        pass1=(EditText)findViewById(R.id.pass1);
        pass2=(EditText)findViewById(R.id.pass2);



        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_bt_clicked();
            }
        });

        end_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                End_bt_Clicked();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Check_bt_clicked(){
        String user =email.getText().toString();
        String passw1 = pass1.getText().toString();
        String passw2 = pass2.getText().toString();



        //Check internet connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connec != null &&
                (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) ||
                (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)) {
            //You are connected
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            //Not connected.
            dispText.setText("You must be connected to the internet");
            ok = false;
            return;
        }


        //Check if user exits in database
        //not implemented yet

        //If password is too small
        if(passw1.length()<5){
            dispText.setText("Password mast be at least 5 characters");
            ok=false;
            return;
        }
        //if password confirmation doesn't match
        if(!passw1.equals(passw2)){
            dispText.setText("Password doesn't match");
            ok=false;
            return;
        }

        //if e-mail unique and password confirmed you can Sign up

        if (ok) {
            end_bt.setEnabled(true);
            dispText.setText("You are ready!!! Touch  the End button to Sign Up ");
            return;
        }

    }

    public void End_bt_Clicked(){
        //Check Again if The user cheat and changed smth or the internet connection has been broken.
        Check_bt_clicked();
        if(!ok){
            dispText.setText("You cheater! Undo the change and try Again!");
            end_bt.setEnabled(false);
            return;
        }

        //Add new User to dataBase
        //not implemented yet
        dispText.setText("Congrats you successfully signed up!");
    }
}
