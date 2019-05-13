package com.cse110.ucsd.flashbackmusicproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;
import com.cse110.ucsd.flashbackmusicproject.utility.NameGenerator;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login Activity";

    private static final int RC_SIGN_IN = 1234;

    private Button signout;
    private SignInButton  signin;

    private GoogleSignInClient client;

    private static final NameGenerator nGenerator = new NameGenerator();

    private String name, email, code, login, pseudoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(Dictionary.CLIENT_ID)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope("https://www.googleapis.com/auth/contacts.readonly"))
                .build();

        client = GoogleSignIn.getClient(this, gso);

        signin = findViewById(R.id.sign_in_button);
        signout = findViewById(R.id.sign_out_button);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = client.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();

        //Check if user has already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHAREDPREF_NAME, MODE_PRIVATE);

        name = sharedPreferences.getString("name", null);

        if(name != null){
            Intent signInIntent = client.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                name = account.getDisplayName();
                email = account.getEmail();
                code = account.getServerAuthCode();
                login = "true";
                pseudoName = nGenerator.getName();
                next();
            } catch (ApiException e) {
                Log.w(TAG, "SignInResult: Failed code=" + e.getStatusCode());
            }
        }

    }

    public void next() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (name == null) {
            name = nGenerator.getName();
            email = "";
            code = "";
            login = "false";
            pseudoName = "";
        }

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHAREDPREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("token", code);
        editor.putString("pseudo", pseudoName);
        editor.putString("google", login);
        editor.apply();

        this.startActivity(intent);
    }

    public void updateUI() {
        if (name != null) {
            signout.setVisibility(View.VISIBLE);
            signin.setVisibility(View.INVISIBLE);
        } else {
            signout.setVisibility(View.INVISIBLE);
            signin.setVisibility(View.VISIBLE);
        }
    }

    public void signout(View view) {
        client.signOut();
        updateUI();
    }

    public void noSignin(View view) {
        next();
    }

}
