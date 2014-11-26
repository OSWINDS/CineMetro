package cinemetroproject.cinemetro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import cinemetroproject.cinemetro.R;

public class SignUp extends ActionBarActivity {

    Button check_bt,end_bt;
    EditText email,pass1,pass2;
    boolean ok = true;
    String username, passw1 , passw2;
    private ArrayList<User> users;
    TextView success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_sign_up));
        setContentView(R.layout.activity_sign_up);

        check_bt=(Button)findViewById(R.id.check_button);
        end_bt=(Button) findViewById(R.id.End_button);
        email=(EditText)findViewById(R.id.eMail);
        pass1=(EditText)findViewById(R.id.pass1);
        pass2=(EditText)findViewById(R.id.pass2);
        success=(TextView)findViewById(R.id.success);

        users=new ArrayList<User>();
        users=DbAdapter.getInstance().getUsers();
        //users.add(new User(1, "chara", "xxxxx"));

        ImageButton logo=(ImageButton)findViewById(R.id.logo);
        logo.setBackgroundResource(R.drawable.logo_background);

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
        username = email.getText().toString();
        passw1 = pass1.getText().toString();
        passw2 = pass2.getText().toString();

        ok=true;

        //Check internet connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connec != null &&
                (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) ||
                (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)) {
            //You are connected
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            //Not connected.
            Toast.makeText(SignUp.this, "You must be connected to the internet.", Toast.LENGTH_SHORT).show();
            ok = false;
            return;
        }


        //Check if user exits in database
        //not implemented yet
        //boolean found=false;

        for(User u : users){
            if(u.getUsername().equals(username)){
                Toast.makeText(SignUp.this, "User already exists.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //check if email is valid
        /*if(!username.contains("@")){
            Toast.makeText(SignUp.this, "Email must contain @", Toast.LENGTH_SHORT).show();
            ok=false;
            return;
        }*/

        //If password is too small
        if(passw1.length()<5){
            Toast.makeText(SignUp.this, "Password must be at least 5 characters.", Toast.LENGTH_SHORT).show();
            ok=false;
            return;
        }
        //if password confirmation doesn't match
        if(!passw1.equals(passw2)){
            Toast.makeText(SignUp.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            ok=false;
            return;
        }

        //if e-mail unique and password confirmed you can Sign up
        if((passw1 == passw2)){
            ok=true;
        }

        if (ok) {
            end_bt.setEnabled(true);
            success.setText("You are ready!!!\n" +
                    "Touch the End button to Sign Up!");
            //Toast.makeText(SignUp.this, "You are ready!!!\n" +
            //      "Touch the End button to Sign Up.", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void End_bt_Clicked(){
        //Check Again if The user cheat and changed smth or the internet connection has been broken.
        Check_bt_clicked();
        if(!ok){
            Toast.makeText(SignUp.this, "Undo the change and try again!", Toast.LENGTH_SHORT).show();
            end_bt.setEnabled(false);
            return;
        }

        User newUser=new User(username, passw1);
        DbAdapter.getInstance().addNewUser(newUser);
        DbAdapter.getInstance().setActiveUser(DbAdapter.getInstance().getUserByUsername(username));
        //DbAdapter.getInstance().setActiveUser(DbAdapter.getInstance().getUserByUsername(username));
        DbAdapter.getInstance().signUpUserToParse(DbAdapter.getInstance().getActiveUser());

        Toast.makeText(SignUp.this, "Congrats!\nYou successfully signed up!", Toast.LENGTH_SHORT).show();

        Intent intent;
        intent = new Intent(SignUp.this, ProfileActivity.class);

        startActivity(intent);
        this.finish();
    }
}