package com.jos.dem.oauth2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

  private static final int RC_SIGN_IN = 9001;
  private static final String TAG = "Oauth2Google";

  private GoogleApiClient googleApiClient;
  private TextView statusTextView;
  private SignInButton signInButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    statusTextView = (TextView) findViewById(R.id.status);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    googleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();

    signInButton = (SignInButton) findViewById(R.id.sign_in_button);
    signInButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    signIn();
  }

  private void signIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }

  private void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      GoogleSignInAccount account = result.getSignInAccount();
      statusTextView.setText("Signed as: " + account.getEmail());
      signInButton.setVisibility(View.GONE);
    } else {
      statusTextView.setText("Signed in unsuccessfully");
    }
  }

}
