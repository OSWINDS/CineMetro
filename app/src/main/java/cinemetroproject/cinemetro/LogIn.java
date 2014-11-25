package cinemetroproject.cinemetro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class LogIn extends ActionBarActivity {

    Button logbt, signbt;
    ImageButton logo;
    EditText username, password;
    //User connectedUser;
    boolean readyToSignUp;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        logbt = (Button) findViewById(R.id.logIn);
        signbt = (Button) findViewById(R.id.SignUp_button);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);

        logo=(ImageButton)findViewById(R.id.logo);
        logo.setBackgroundResource(R.drawable.logo_background);

        users=new ArrayList<User>();
        users=DbAdapter.getInstance().getUsers();

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

        //connectedUser=null;
        readyToSignUp=false;

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
        boolean readyToLogIn = false;


        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connec != null &&
                (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) ||
                (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)) {
            //You are connected
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            //Not connected.
            Toast.makeText(LogIn.this, "You must be connected to the internet.", Toast.LENGTH_SHORT).show();
            //success.setText("You must be connected to the internet.");
            readyToLogIn = false;
            return;
        }


        //Check if user exits in database
        //Check if password match users passWord
        //if match, send Data to database
        boolean found=false;
        for(User u : users){
            if(user.equals(u.getUsername())){
                found=true;
            }
        }

        if(!found){
            Toast.makeText(LogIn.this, "User does not exist.", Toast.LENGTH_SHORT).show();
            readyToLogIn=false;
            return;
        }

        if(DbAdapter.getInstance().getUserByUsername(user).checkPassword(pass)){
            readyToLogIn=true;
        }else{
            Toast.makeText(LogIn.this, "Wrong password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (readyToLogIn){
            DbAdapter.getInstance().setActiveUser(DbAdapter.getInstance().getUserByUsername(user));
            Intent intent;
            intent = new Intent(LogIn.this, ProfileActivity.class);

            startActivity(intent);
            this.finish();
        }
    }

    public void signUpClicked() {
        Intent intent;
        intent = new Intent(LogIn.this, SignUp.class);
        startActivity(intent);
    }


}