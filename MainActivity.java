package com.example.settleup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    GoogleSignInClient mGoogleSignInClient;

    private EditText mEmail, forgotTextLink;
    private EditText mPass;

    private ProgressDialog mDialog;

    //Firebase..

    private FirebaseAuth mAuth;

    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    TextView textView;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        super.onCreate(savedInstanceState);
        //loadLocale();
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        mDialog=new ProgressDialog(this);

        //change actionbar title

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

//        Button changeLang=findViewById(R.id.changeMyLang);
//        changeLang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //show AlertDialog to display list of language, one can be selected
//
//                showChangeLanguageDialog();
//            }
//        });

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        mDialog = new ProgressDialog(this);

        loginDetails();


        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        googleApiClient=new GoogleApiClient.Builder(this)
//                .enableAutoManage(this,this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
//                .build();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Toast.makeText(this, "User email : "+personEmail, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "User name : "+personName, Toast.LENGTH_SHORT).show();
            }

            // startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
            //startActivity(new Intent(getApplicationContext(),DashBoardFragment.class));
            startActivity(new Intent(MainActivity.this,MainActivity.class));



            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Log.d("Message" , e.toString());
        }
    }

//    private void showChangeLanguageDialog() {
//
//        //array of languages to display in alert dialog
//
//        final String[] listItems={"ગુજરાતી","हिंदी","English"};
//
//        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
//        mBuilder.setTitle("Choose Language...");
//        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(which == 0){
//                    //Gujarati
//                    setLocale("gu");
//                    recreate();
//                }
//                else if(which == 1){
//                    //Hindi
//                    setLocale("hi");
//                    recreate();
//                }
//                else if(which == 2){
//                    //English
//                    setLocale("en");
//                    recreate();
//                }
//
//                //dismiss alert dialog when language selected
//
//                dialog.dismiss();
//
//            }
//        });
//
//        AlertDialog mDialog=mBuilder.create();
//        //show alert dialog
//        mDialog.show();
//
//    }
//
//    private void setLocale(String lang) {
//        Locale locale=new Locale(lang);
//        Locale.setDefault(locale);
//        Configuration config=new Configuration();
//        config.locale=locale;
//        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//        //saved data to shared preferences
//        SharedPreferences.Editor editor=getSharedPreferences("Settle Up", MODE_PRIVATE).edit();
//        editor.putString("My_Lang", lang);
//        editor.apply();
//    }

    //load language saved in shared preferences

//    public void loadLocale(){
//        SharedPreferences prefs=getSharedPreferences("Settle Up", Activity.MODE_PRIVATE);
//        String language=prefs.getString("My_Lang", "");
//        setLocale(language);
//    }

    private void loginDetails() {

        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView mForgetPassword = findViewById(R.id.forget_password);
        TextView mSignupHere = findViewById(R.id.signup_reg);

        btnLogin.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();
            String pass = mPass.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email Required..");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                mPass.setError("Password Required..");
                return;
            }

            mDialog.setMessage("Processing..");
            mDialog.show();

            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    mDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    Toast.makeText(getApplicationContext(), "Login Successfull..", Toast.LENGTH_SHORT).show();
                } else {
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Login Failed..", Toast.LENGTH_SHORT).show();
                }
            });

        });

        //Registration Activity

        mSignupHere.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationActivity.class)));

        //Reset Password Activity..

        mForgetPassword.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ReseatActivity.class)));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

//        if(user != null){
//            startActivity(new Intent(MainActivity.this, IncomeFragment.class));
//        }

    }
}