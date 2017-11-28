package br.com.fafeltech.somdanoite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.dialog.LoginDialog;
import br.com.fafeltech.somdanoite.helper.ParseErros;

public class LoginActivity extends BaseActivity {

    private EditText editUsuario;
    private EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsuario = (EditText) findViewById(R.id.edit_usuario);
        editSenha = (EditText) findViewById(R.id.edit_senha);
        TextView textoCadastrar = (TextView) findViewById(R.id.textView_cadastrar);
        TextView esqueceuSenha = (TextView) findViewById(R.id.esqueci_senha);
        Button botaoLogar = (Button) findViewById(R.id.botao_logar);

        //verificar se usuário esta logado
        veridicarUsuarioLogado();

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = editUsuario.getText().toString();
                String senha = editSenha.getText().toString();

                verificaUsuario( usuario, senha );
            }
        });

        textoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroUsuario();
            }
        });
        esqueceuSenha.setOnClickListener( resetarSenha() );
    }

    private View.OnClickListener resetarSenha() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_reseta_senha, null, false);
                final EditText digitaEmail = (EditText) view.findViewById(R.id.digita_email_cadastro);
                Button confirmaResetaSenha = (Button) view.findViewById(R.id.bt_reseta_senha_dialog);
                Button cancelar = (Button) view.findViewById(R.id.bt_cancelar_reseta_senha_dialog);

                final AlertDialog dialog = criarDialog(view);

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                confirmaResetaSenha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgressDialog();
                        String emailInformado = digitaEmail.getText().toString();

                        ParseUser.requestPasswordResetInBackground(emailInformado, new RequestPasswordResetCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    dialog.dismiss();
                                    hideProgressDialog();
                                    AlertDialog ok = dialogOK("Um e-mail foi enviado para o endereço informado, com uma nova senha. Favor verificar sua caixa de entrada.");
                                    ok.show();
                                } else {
                                    dialog.dismiss();
                                    hideProgressDialog();
                                    ParseErros parseErros = new ParseErros();
                                    String erro = parseErros.getErro(e.getCode());
                                    toast("Sua solicitação apresentou um erro. Tente novamente. Erro: " + erro );
                                }
                            }
                        });
                    }
                });

                dialog.show();
            }
        };
    }

    private void verificaUsuario(String usuario, String senha) {

        showProgressDialog();

        ParseUser.logInInBackground(usuario, senha, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if ( e == null ) { //deu certo
                    toast("Login realizado com sucesso!");
                    hideProgressDialog();
                    abrirTelaPrincipal();
                } else { //deu errado
                    hideProgressDialog();
                    toast("Erro ao tentar logar!");
                    Log.e("Erro:", e.getMessage() + " " + e.getCode() );
                }
            }
        });
    }

    public void abrirCadastroUsuario(){
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.show();
    }

    private void veridicarUsuarioLogado() {
        if (ParseUser.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
