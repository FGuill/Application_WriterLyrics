package com.guilla.lyricswriter.fragment.pro;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.guilla.lyricswriter.LoginPro.LoginPresenter;
import com.guilla.lyricswriter.LoginPro.LoginProPresenterImpl;
import com.guilla.lyricswriter.LoginPro.LoginProView;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.ProfilProActivity;
import com.guilla.lyricswriter.fonts.TextViewNotoSansRegular;
import com.guilla.lyricswriter.fragment.sharity.Charity_Inscription_container_fragment;

import static com.guilla.lyricswriter.Utils.Utils.replaceFragmentWithAnimation;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Login_fragment extends Fragment implements LoginProView,View.OnClickListener {

    private View inflate;
    private ProgressBar progress;
    private EditText username;
    private EditText password;
    private TextViewNotoSansRegular login_BT;
    private TextViewNotoSansRegular inscription;
    private LoginPresenter presenter;
    private static String type;
    private TextView forgot_password;
    private ImageView logo;
    private LottieAnimationView animationView;

    public static Pro_Login_fragment newInstance(String type) {
        Pro_Login_fragment myFragment = new Pro_Login_fragment();
        Bundle args = new Bundle();
        args.putString("type",type);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_login_pro, container, false);

        type=getArguments().get("type").toString();
        logo=(ImageView)inflate.findViewById(R.id.logopro);
        inscription=(TextViewNotoSansRegular)inflate.findViewById(R.id.inscription);
        forgot_password=(TextView)inflate.findViewById(R.id.forgotpassword);
        login_BT = (TextViewNotoSansRegular) inflate.findViewById(R.id.login_BT);
        username = (EditText) inflate.findViewById(R.id.username_login);
        password = (EditText) inflate.findViewById(R.id.password_login);
        animationView = (LottieAnimationView) inflate.findViewById(R.id.animation_view);
        animationView.setAnimation("loading.json");

        inscription.setOnClickListener(this);
        login_BT.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        presenter = new LoginProPresenterImpl(this);

        if (type.equals("charite")){
            logo.setImageResource(R.drawable.logocharite);
        }else if (type.equals("pro")){
            logo.setImageResource(R.drawable.logopro);

        }

        return inflate;
    }

    @Override
    public void onClick(View view) {
            switch(view.getId()) {
                case R.id.inscription:
                    if (type.equals("pro")){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Utils.AnimationSlideFragment(getActivity(),getFragmentManager(),R.id.login, Pro_Inscription_fragment.newInstance(type),"Inscription_Pro_fragment", Gravity.RIGHT,true);
                        }else {
                            replaceFragmentWithAnimation(R.id.login, Pro_Inscription_fragment.newInstance(type),getFragmentManager(),"Inscription_Pro_fragment");
                        }
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Utils.AnimationSlideFragment(getActivity(),getFragmentManager(),R.id.login, Charity_Inscription_container_fragment.newInstance(),"Charity_Inscription_container_fragment", Gravity.RIGHT,true);
                        }else {
                            replaceFragmentWithAnimation(R.id.login, Charity_Inscription_container_fragment.newInstance(),getFragmentManager(),"Charity_Inscription_container_fragment");
                        }
                    }
                    break;

                case R.id.login_BT:
                    if (type.equals("charite")) {
                        presenter.validateCredentials(getActivity(),"charite",username.getText().toString(), password.getText().toString());

                    }else {
                        presenter.validateCredentials(getActivity(),"pro",username.getText().toString(), password.getText().toString());
                    }
                    break;

                case R.id.forgotpassword:
                    FragmentManager fm= getActivity().getSupportFragmentManager();
                    Passord_forgotten_Container_fragment dialog=new Passord_forgotten_Container_fragment();
                    dialog.show(fm,"dialog");

                  /*  Utils.ForgottenPasswordDialog(getActivity(), false, new Utils.ProcessEmail() {
                        @Override
                        public void SetEmail(String email) {

                            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(),"Un email à envoyé",Toast.LENGTH_LONG);
                                    } else {
                                        Toast.makeText(getActivity(),"erreur: l'email n'a pu être envoyé",Toast.LENGTH_LONG);
                                    }
                                }
                            });
                        }

                        @Override
                        public void Cancel() {
                        }
                    });*/
                    break;
        }
    }


    @Override public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
        login_BT.setVisibility(View.INVISIBLE);
        animationView.setVisibility(View.VISIBLE);
        animationView.loop(true);
        animationView.playAnimation();
    }

    @Override public void hideProgress() {
        login_BT.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.INVISIBLE);
    }

    @Override public void setUsernameError() {
       // username_TextInputLayout.setError("Entrer votre utilisateur");
    }

    @Override public void setPasswordError() {
      //  password_TextInputLayout.setError("Entrer votre mot de passe");
    }

    @Override
    public void setUserError() {
        Toast.makeText(getActivity(),"Username or password invalid",Toast.LENGTH_LONG).show();
    }

    @Override public void navigateToHome() {
        IsPro();
        startActivity(new Intent(getActivity(), ProfilProActivity.class));
        getActivity().finish();
    }

    private void IsPro(){
        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("status", "Business");
        editor.commit();
    }
}