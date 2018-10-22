package com.mta.loci;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 4000;
    private static int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Launcher);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        login();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                // Get new Instance ID token
                                String token = task.getResult().getToken();
                                writeNewUser(fUser, token);
                            }
                        });
                startHomeActivity();
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Sign in canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent( SplashActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void login() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        if(fUser != null){
            startHomeActivity();
            Toast.makeText(this, fUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseUILogin();
        }
    }

    private void firebaseUILogin() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void writeNewUser(FirebaseUser fUser, String token) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance()
                                    .getReference("Users");
        LociUser lociUser = new LociUser(fUser.getUid(),fUser.getEmail(),fUser.getDisplayName(), token);
        usersRef.child(fUser.getUid()).setValue(lociUser);
    }
}


