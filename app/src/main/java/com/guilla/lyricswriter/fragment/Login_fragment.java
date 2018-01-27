package com.guilla.lyricswriter.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LoginClient.LoginClientPresenter;
import com.guilla.lyricswriter.LoginClient.LoginClientPresenterImpl;
import com.guilla.lyricswriter.LoginClient.LoginClientView;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.fragment.pro.Pro_Login_fragment;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.HashMap;
import java.util.Map;

import static com.guilla.lyricswriter.Application.mAuth;
import static com.guilla.lyricswriter.Application.mKinveyClient;
import static com.guilla.lyricswriter.FacebookConnectivity.DownloadImageBitmap;
import static com.guilla.lyricswriter.Utils.Utils.replaceFragmentWithAnimation;
import static com.guilla.lyricswriter.activity.LoginActivity.callbackManager;
import static com.guilla.lyricswriter.activity.LoginActivity.db;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.mGoogleApiClient;


/**
 * Created by Moi on 14/11/15.
 */
public class Login_fragment extends Fragment implements View.OnClickListener, LoginClientView, GoogleApiClient.OnConnectionFailedListener {
    private TwitterLoginButton fake_twitterbt;
    private View inflate;
    private TextView connexion;
    private ImageView facebook;
    private ImageView twitter;
    private LoginClientPresenter presenter;
    private Button access_pro;
    private Button access_charite;
    private LoginButton loginButton;
    private User UserSession;
    public static ProgressBar progressBar;
    private String PictureProfil=null;
    private GoogleApiClient mGoogleApiClient;
    private  int RC_SIGN_IN=0;

    public static Login_fragment newInstance() {
        Login_fragment myFragment = new Login_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_login_client, container, false);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("346564130321-l8l7s5h2np1niqkf6f80jhqr9r3jl20s.apps.googleusercontent.com")
                .requestEmail()
                .build();
         mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) inflate.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        twitter = (ImageView) inflate.findViewById(R.id.twitter_login);
        fake_twitterbt = (TwitterLoginButton) inflate.findViewById(R.id.fake_twitterbutton);
        progressBar= (ProgressBar)inflate.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        facebook = (ImageView) inflate.findViewById(R.id.facebook_login);
        access_pro = (Button) inflate.findViewById(R.id.pro_login_acces);
        access_charite = (Button) inflate.findViewById(R.id.charite_login_acces);
        loginButton=(LoginButton)inflate.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        fake_twitterbt.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        loginButton.setFragment(this);

        twitter.setOnClickListener(this);
        facebook.setOnClickListener(this);
        access_pro.setOnClickListener(this);
        access_charite.setOnClickListener(this);

        presenter = new LoginClientPresenterImpl(this);

        fake_twitterbt.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("TAG", "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException exception) {
                Log.w("TAG", "twitterLogin:failure", exception);
                //   updateUI(null);
            }


        });
        return inflate;
    }

    boolean boolFacebook = false;
    boolean boolTwitter = false;

    @Override
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        switch (view.getId()) {
            case R.id.pro_login_acces:
                mKinveyClient.user().logout().execute();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Utils.AnimationSlideFragment(getActivity(),fm,R.id.login, Pro_Login_fragment.newInstance("pro"),"Login_Pro_fragment", Gravity.RIGHT,true);
                }else {
                    replaceFragmentWithAnimation(R.id.login, Pro_Login_fragment.newInstance("pro"),getFragmentManager(),"Login_Pro_fragment");
                }
                break;

            case R.id.charite_login_acces:
                mKinveyClient.user().logout().execute();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Utils.AnimationSlideFragment(getActivity(),fm,R.id.login, Pro_Login_fragment.newInstance("charite"),"Login_Pro_fragment", Gravity.RIGHT,true);
                }else {
                    replaceFragmentWithAnimation(R.id.login, Pro_Login_fragment.newInstance("charite"),getFragmentManager(),"Login_Pro_fragment");
                }

                break;

            case R.id.twitter_login:
                fake_twitterbt.performClick();
                break;
            case R.id.facebook_login:
                loginButton.performClick();
                break;

            case R.id.login_button:
                boolFacebook = true;
                boolTwitter = false;
                twitter.setImageResource(R.drawable.twitter_unclick);
                facebook.setImageResource(R.drawable.facebook_click);
                Connexion();
                break;

            case R.id.fake_twitterbutton:
                boolFacebook = false;
                boolTwitter = true;
                twitter.setImageResource(R.drawable.twitter_click);
                facebook.setImageResource(R.drawable.facebook_unclick);
              //  Connexion();
                break;

            case R.id.sign_in_button:
                signInGoogle();
                break;

        }
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUsernameError() {

    }

    @Override
    public void setPasswordError() {

    }

    @Override
    public void navigateToHome() {
        if (progressBar!=null){
            progressBar.setVisibility(View.INVISIBLE);
        }
        IsUserSession();
         Intent intent=new Intent(getActivity(), ProfilActivity.class);
            startActivity(intent);
            getActivity().finish();
        Log.d("DB", "navigateToHome");

    }


    @Override
    public void noNetworkConnectivity() {
        Utils.showDialog3(getActivity(), getString(R.string.dialog_network), getString(R.string.network), true, new Utils.Click() {
            @Override
            public void Ok() {
            }

            @Override
            public void Cancel() {

            }
        });
    }

    private void Connexion() {
        if (!Utils.isConnected(getContext())) {
            noNetworkConnectivity();
        } else {
            if (!boolFacebook && !boolTwitter) {
                Toast.makeText(getContext(), "Veuillez séléctionner un moyen de connexion par Facebook ou Twitter", Toast.LENGTH_LONG).show();
            } else {
                if (!boolFacebook) {
                    //loginToTwitter(getActivity());
                  presenter.Login_Client(getActivity(), getActivity(),fake_twitterbt, "twitter", LoginActivity.callbackManager);

                } else {
                    presenter.Login_Client(getActivity(), getActivity(),loginButton, "facebook", LoginActivity.callbackManager);
                }
            }
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        fake_twitterbt.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
           Log.d("googleName",acct.getDisplayName());
            firebaseAuthWithGoogle(acct);
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
           // updateUI(false);
        }
    }

    private void IsUserSession(){
        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("status", "User");
        editor.commit();
    }

    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("client_numCode", "");         // getting String
        return accountDisconnect;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if (progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (progressBar!=null){
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } else {

                            final FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TokenSession",user.getDisplayName());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getDisplayName())
                                    .setPhotoUri(user.getPhotoUrl())
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Write a message to the database
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                ref.child("user").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (progressBar!=null){
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                        if(dataSnapshot.exists()){
                                                            UserSession = new User(user.getUid(), user.getDisplayName(),user.getEmail(),"",getFCMToken(getActivity()));
                                                            ObjectId_ToPref(user.getUid());
                                                            Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                                                            hopperUpdates.put(getFCMToken(getActivity()),getFCMToken(getActivity()));
                                                            final DatabaseReference c1v1=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("notificationTokens");
                                                            c1v1.setValue(hopperUpdates);
                                                            final DatabaseReference c1v2=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("token");
                                                            c1v2.setValue(getFCMToken(getActivity()));
                                                            final DatabaseReference c1v3=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("_name");
                                                            c1v3.setValue(user.getDisplayName());
                                                            final DatabaseReference c1v4=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("_id");
                                                            c1v4.setValue(user.getUid());
                                                            db.deleteAllUser();
                                                            db.addUserProfil(UserSession);
                                                            navigateToHome();//                                                            setIsFacebook(context,true);

                                                            Log.d("DB", "User updated");

                                                            // use "username" already exists
                                                            // Let the user know he needs to pick another username.
                                                        } else {
                                                            ObjectId_ToPref(user.getUid());
                                                            UserSession = new User(user.getUid(), user.getDisplayName(),user.getEmail(),"",getFCMToken(getActivity()));
                                                            DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                                                            c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                    Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                                                                    hopperUpdates.put(getFCMToken(getActivity()),getFCMToken(getActivity()));
                                                                    final DatabaseReference c1v2=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("notificationTokens");
                                                                    c1v2.setValue(hopperUpdates);

                                                                 //   setIsFacebook(context,true);

                                                                    if (db.getUserCount()<=0){
                                                                        db.addUserProfil(UserSession);
                                                                        navigateToHome();
                                                                        Log.d("DB", "User Added");

                                                                    }else {
                                                                        db.deleteAllUser();
                                                                        db.addUserProfil(UserSession);
                                                                navigateToHome();                                                                        Log.d("DB", "User updated");
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        if (progressBar!=null){
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                });

                                                Log.d("TAG", "User profile updated.");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d("", "handleTwitterSession:" + session);
        if (progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);
        Log.d("TokenSession", session.getAuthToken().token);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getDisplayName())
                                    .setPhotoUri(user.getPhotoUrl())
                                    .build();

                            Log.d("TokenSession",user.getDisplayName());

                            new ProfilePhotoAsync(user.getPhotoUrl().toString()).execute();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                ref.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.child(user.getUid()).exists()) {
                                                            ObjectId_ToPref(user.getUid());
                                                            String picurl = dataSnapshot.child(user.getUid()).child("picurl").getValue().toString();
                                                            UserSession = new User(user.getUid(), user.getDisplayName(), "", picurl, getFCMToken(getActivity()));
                                                            Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                                                            hopperUpdates.put(getFCMToken(getActivity()), getFCMToken(getActivity()));
                                                            final DatabaseReference c1v1;
                                                            c1v1=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("notificationTokens");
                                                            c1v1.setValue(hopperUpdates);
                                                            final DatabaseReference c1v2;
                                                            c1v2=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("token");
                                                            c1v2.setValue(getFCMToken(getActivity()));
                                                            final DatabaseReference c1v3=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("_name");
                                                            c1v3.setValue(user.getDisplayName());
                                                            final DatabaseReference c1v4=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("_id");
                                                            c1v4.setValue(user.getUid());

                                                            setIsFacebook(getActivity(),false);

                                                            if (db.getUserCount() <= 0) {
                                                                db.addUserProfil(UserSession);

                                                                navigateToHome();
                                                                Log.d("DB", "User Added");

                                                            } else {
                                                                db.deleteAllUser();
                                                                db.addUserProfil(UserSession);
                                                                navigateToHome();
                                                                Log.d("DB", "User updated");
                                                            }

                                                        } else {
                                                            Log.d("TAG", "user not exists");
                                                            ObjectId_ToPref(user.getUid());
                                                            UserSession = new User(user.getUid(), user.getDisplayName(), user.getEmail(), PictureProfil != null ? PictureProfil : null, getFCMToken(getActivity()));
                                                            final DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                                                            c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                    Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                                                                    hopperUpdates.put(getFCMToken(getActivity()), getFCMToken(getActivity()));
                                                                    final DatabaseReference c1v2=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("notificationTokens");
                                                                    c1v2.setValue(hopperUpdates);

                                                                    setIsFacebook(getActivity(),false);

                                                                    if (db.getUserCount() <= 0) {
                                                                        db.addUserProfil(UserSession);
                                                                        navigateToHome();
                                                                        Log.d("DB", "User Added");

                                                                    } else {
                                                                        db.deleteAllUser();
                                                                        db.addUserProfil(UserSession);
                                                                        navigateToHome();
                                                                        Log.d("DB", "User updated");
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        if (progressBar!=null){
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                });
                                                // Write a message to the database
                                                //Add user to localDB

                                                //
                                                Log.d("TAG", "User profile updated.");
                                            }
                                        }
                                    });
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        String url;
        Bitmap pictureFB;
        public ProfilePhotoAsync(String url) {
            this.url = url;
        }
        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
           pictureFB = DownloadImageBitmap(url);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PictureProfil = Utils.BitmapToString(pictureFB);
        }
    }






        @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
      //  updateUI(currentUser);
    }

    private void ObjectId_ToPref(String ObjectId){
        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("User_ObjectId", ObjectId);  // Saving objectid in preference
        editor.commit();
    }

    private String getFCMToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("TokenFireBase", "novalue");         // getting String
        return accountDisconnect;
    }

    private void setIsFacebook(Context context,Boolean ObjectId){
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFacebook", ObjectId);  // Saving objectid in preference
        editor.commit();
    }
}