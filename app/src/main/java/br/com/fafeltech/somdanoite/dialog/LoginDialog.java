package br.com.fafeltech.somdanoite.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.activity.CadastroActivity;
import br.com.fafeltech.somdanoite.activity.FacebookLoginActivity;


public class LoginDialog extends AlertDialog {


    public LoginDialog(@NonNull Context context) {
        super(context);
    }

    public LoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public LoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(getApplicationContext());
//        ParseFacebookUtils.initialize(getApplicationContext());

        setContentView(R.layout.dialog_login);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        LoginButton facebookLogin = findViewById(R.id.fb_login_button);
        Button emailLogin = findViewById(R.id.email_login);
        facebookLogin.setReadPermissions("email", "public_profile", "user_friends");
        facebookLogin.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        facebookLogin.setOnClickListener(loginWithFacebook());

        emailLogin.setOnClickListener(loginWithEmail());

    }



    private View.OnClickListener loginWithEmail() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), CadastroActivity.class));
                dismiss();
            }
        };
    }

    private View.OnClickListener loginWithFacebook() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Clicou no Login com Facebook", Toast.LENGTH_LONG).show();
                getContext().startActivity(new Intent(getContext(), FacebookLoginActivity.class));
                dismiss();
            }
        };
    }
}
