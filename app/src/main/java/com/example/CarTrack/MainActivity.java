package com.example.CarTrack;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//package com.example.CarTrack;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageButton google_btn;
    private SignInButton signInButton;
    static final int RC_SIGN_IN = 321;
    private Button loginbt, signupbt;
    EditText password_Et;
    String password_entered="";
    private FirebaseAuth mAuth;
    LayoutInflater inflater;
    View v;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //Permission Manager Object
    PermissionManager permissionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();


        mAuth = FirebaseAuth.getInstance();


        //PERMISSION MANAGER CODE CALL
        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        loginbt =  findViewById(R.id.login);
        signupbt =  findViewById(R.id.signup);
        signupbt.setOnClickListener(this);
        loginbt.setOnClickListener(this);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        dialog();
        //signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);

        google_btn = (ImageButton) findViewById(R.id.sign_in_button);







        // FirebaseFirestore db = FirebaseFirestore.getInstance();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googlesignin();
    }

    private void googlesignin() {

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 321);
            }
        });

        //  GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {


            //redirect user to the next activity
        } else {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);

            updatefirestore_user(account);

//            startActivity(new Intent(MainActivity.this, Main2Activity.class));

            // Signed in successfully, show authenticated UI.


            //   Toast.makeText(this, "Successfully Signed In", Toast.LENGTH_SHORT).show();


            //Start ur new activity here


            //startActivity(new Intent(MainActivity.this,OTP.class));


            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show();
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //    Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //   Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }


    private void updatefirestore_user(GoogleSignInAccount account) {
        if (account != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            final  String personEmail = account.getEmail();
            String phone_number = "";
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Map<String, Object> user = new HashMap<>();
            user.put("UserName", personName);
            user.put("Email", personEmail);
            // user.put("Phone Number",phone_number);


            // Add a new document with a generated ID
            db.collection("users").document(personEmail)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Firestore updated ", Toast.LENGTH_SHORT).show();

                            editor.putString("email", personEmail);
                            editor.commit(); // commit changes

                            startActivity(new Intent(MainActivity.this, MainActivity1.class));

                            //  Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error writing document", e);

                            Toast.makeText(MainActivity.this, "Firestore not updated", Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }

    @Override
    public void onClick(View view) {
        if (view == loginbt) {
            // startActivity(new Intent(MainActivity.this, Login.class));
        }

        if (view == signupbt) {
            //  startActivity(new Intent(MainActivity.this, Signup.class));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);

        //FOR DEBUGGING
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        ArrayList<String> denied = permissionManager.getStatus().get(0).denied;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void  dialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        // Get the layout inflater

        builder.setCancelable(false);


        inflater = getLayoutInflater();
        v=inflater.inflate(R.layout.password_dialog, null);


        //  builder.getContext().setTheme(R.style.AppTheme);
        final AlertDialog  alert= builder.create();
        //alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        password_Et= (EditText) v.findViewById(R.id.password_edit);

                        // Log.d("correct_password_edit", passowrd.toString());
                        password_entered = password_Et.getText().toString();
                        showToast("Entered Password: "+password_entered);
                        Log.d("correct_password",password_entered);
                        String original= "1234";

                        if (password_entered.equals("bmc_car"))
                        {
                            // Log.d("correct_password","heyyy");
                            dialog.cancel();
                            check_current_user();


                        }
                        else {
                            //Log.d(user_text,"string is empty");
                            String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                            builder.setTitle("Error");
                            builder.setMessage(message);
                            builder.setCancelable(false);
                            // builder.setPositiveButton("Cancel", null);
                            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog();
                                }
                            });
                            builder.create().show();

                        }
                    }
                });

        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();
    }

    private void check_current_user() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser !=null)
        {
            startActivity(new Intent(this,MainActivity1.class));
            finish();
        }

    }

    public void showToast(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

}
