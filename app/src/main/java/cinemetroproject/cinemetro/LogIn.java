package cinemetroproject.cinemetro;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogIn extends ActionBarActivity {

    Button logbt, signbt;
    EditText username, password;
    TextView success, information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logbt = (Button) findViewById(R.id.logIn);
        signbt = (Button) findViewById(R.id.SignUp_button);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);
        success = (TextView) findViewById(R.id.succesText);
        information = (TextView) findViewById(R.id.information);

        logbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonClickedtoLogIn();
            }
        });

        signbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpClicked();
            }
        });

        information.setText("Dispalay the new Achivments here!");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_in, menu);
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


    private void ButtonClickedtoLogIn() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        boolean ok = true;


        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connec != null &&
                (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) ||
                (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)) {
            //You are connected
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            //Not connected.
            success.setText("You must be connected to the internet");
            ok = false;
            return;
        }


        //Check if user exits in database
        //Check if password match users passWord
        //if match, send Data to database

        if (ok) {
            success.setText("Success, database Updated :)");
            return;
        }

    }

    public void signUpClicked() {

        Intent intent;
        intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }


}

