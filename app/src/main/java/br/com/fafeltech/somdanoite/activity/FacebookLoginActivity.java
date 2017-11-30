package br.com.fafeltech.somdanoite.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.fafeltech.somdanoite.R;

/**
 * Created by Thalles on 30/11/2017.
 */

public class FacebookLoginActivity extends Activity {


    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fb_login);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Fazer o cadastro no parse
                        //nome , email , tipo
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(FacebookLoginActivity.this, "Cadastro Cancelado!",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(FacebookLoginActivity.this, "Houve Erro ao Fazer o login!!!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
