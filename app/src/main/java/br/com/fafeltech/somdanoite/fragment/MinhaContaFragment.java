package br.com.fafeltech.somdanoite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.Objects;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.activity.MinhaContaActivity;

public class MinhaContaFragment extends BaseFragment implements MinhaContaActivity.OnBackPressedListener {

    private TextView nome;
    private TextView nomeArtista;
    private TextView nomeUsuario;
    private TextView categoria;
    private TextView email;
    private ParseUser user;

    private EditText editNomeArtista;
    private Spinner spinner;
    private EditText editEmail;

    private String mNome;
    private String mNomeArtista;
    private String mNomeUsuario;
    private int mCategoriaInt;
    private String mEmail;
    private String mCategoria = "";

    private Button btEditaDados;
    private Button btAlteraSenha;
    private Button btSalvaAlteracoes;

    public MinhaContaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);
        ((MinhaContaActivity) getActivity()).setOnBackPressedListener(this);
        nome = (TextView) view.findViewById(R.id.nome_mc);
        nomeArtista = (TextView) view.findViewById(R.id.nome_artista_mc);
        nomeUsuario = (TextView) view.findViewById(R.id.nome_usuario_mc);
        categoria = (TextView) view.findViewById(R.id.categoria_mc);
        email = (TextView) view.findViewById(R.id.email_mc);
        Context context = getActivity().getApplicationContext();
        editNomeArtista = (EditText) view.findViewById(R.id.nome_artista_ec);
        spinner = (Spinner) view.findViewById(R.id.spinner_categoria_ec);
        editEmail = (EditText) view.findViewById(R.id.email_ec);
        user = ParseUser.getCurrentUser();

        mNome = user.get("nome").toString();
        mNomeArtista = user.get("nome_artista").toString();
        mNomeUsuario = user.getUsername();
        mCategoriaInt = (int) user.get("categoria");
        mEmail = user.getEmail();

        String[] categorias = new String[] {
                " ",
                getString(R.string.profissional_da_m_sica),
                getString(R.string.estabelecimento),
                getString(R.string.promotor_de_eventos),
                getString(R.string.usuario)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.text_spinner,
                categorias
        );
        spinner.setAdapter(adapter);
        spinner.setSelection(user.getInt("categoria"));

        btEditaDados = (Button) view.findViewById(R.id.bt_edita_dados_mc);
        btAlteraSenha = (Button) view.findViewById(R.id.bt_altera_senha_mc);
        btSalvaAlteracoes = (Button) view.findViewById(R.id.bt_salva_dados_ec);

        btEditaDados.setOnClickListener( iniciaEditarConta() );
        btAlteraSenha.setOnClickListener( iniciaAlteraSenha() );
        btSalvaAlteracoes.setOnClickListener( salvaDados() );

        visualizarDados();

        return view;
    }

    private View.OnClickListener salvaDados() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categoria = spinner.getSelectedItemPosition();

                if (categoria == 0) {
                    toast(getString(R.string.sem_categoria_selecionada));
                } else {
                    showProgressDialog();
                    user.put("nome_artista", editNomeArtista.getText().toString());
                    user.setEmail(editEmail.getText().toString());
                    user.put("categoria", categoria);

                    user.saveInBackground();

                    toast("Dados salvos com sucesso.");
                    retornaViews();
                }
            }
        };
    }

    private View.OnClickListener iniciaAlteraSenha() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarSenha();
            }
        };
    }

    private View.OnClickListener iniciaEditarConta() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudaViews();
            }
        };
    }

    private void visualizarDados() {

        switch (mCategoriaInt){
            case 1:
                mCategoria = getString(R.string.profissional_da_m_sica);
                break;
            case 2:
                mCategoria = getString(R.string.estabelecimento);
                break;
            case 3:
                mCategoria = getString(R.string.promotor_de_eventos);
                break;
            case 4:
                mCategoria = getString(R.string.usuario);
                break;
        }

        nome.setText(mNome);
        nomeArtista.setText(mNomeArtista);
        nomeUsuario.setText(mNomeUsuario);
        categoria.setText(mCategoria);
        email.setText(mEmail);
    }

    private void mudaViews() {
        nomeArtista.setVisibility(View.GONE);
        editNomeArtista.setVisibility(View.VISIBLE);
        editNomeArtista.setText(mNomeArtista);

        email.setVisibility(View.GONE);
        editEmail.setVisibility(View.VISIBLE);
        editEmail.setText(mEmail);

        categoria.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        btEditaDados.setVisibility(View.GONE);
        btAlteraSenha.setVisibility(View.GONE);
        btSalvaAlteracoes.setVisibility(View.VISIBLE);
    }

    private void retornaViews() {

        ParseUser newUser = ParseUser.getCurrentUser();

        mNome = newUser.get("nome").toString();
        mNomeArtista = newUser.get("nome_artista").toString();
        mNomeUsuario = newUser.getUsername();
        mCategoriaInt = (int) newUser.get("categoria");
        mEmail = newUser.getEmail();

        switch (mCategoriaInt) {
            case 1:
                mCategoria = getString(R.string.profissional_da_m_sica);
                break;
            case 2:
                mCategoria = getString(R.string.estabelecimento);
                break;
            case 3:
                mCategoria = getString(R.string.promotor_de_eventos);
                break;
            case 4:
                mCategoria = getString(R.string.usuario);
                break;
        }

        editNomeArtista.setVisibility(View.GONE);
        nomeArtista.setVisibility(View.VISIBLE);
        nomeArtista.setText(mNomeArtista);

        editEmail.setVisibility(View.GONE);
        email.setVisibility(View.VISIBLE);
        email.setText(mEmail);

        spinner.setVisibility(View.GONE);
        categoria.setVisibility(View.VISIBLE);
        categoria.setText(mCategoria);

        btSalvaAlteracoes.setVisibility(View.GONE);
        btEditaDados.setVisibility(View.VISIBLE);
        btAlteraSenha.setVisibility(View.VISIBLE);

        hideProgressDialog();
    }

    private void alterarSenha() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_altera_senha, null, false);

        final EditText editSenhaAntiga = (EditText) view.findViewById(R.id.digita_senha_atual);
        final EditText editSenhaNova = (EditText) view.findViewById(R.id.digita_senha_nova);
        final EditText editConfirmaSenha = (EditText) view.findViewById(R.id.confirma_senha_nova);
        Button alteraSenha = (Button) view.findViewById(R.id.bt_altera_senha_dialog);
        Button cancelar = (Button) view.findViewById(R.id.bt_cancelar_dialog);

        final AlertDialog dialog = criarDialog(view);

        alteraSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSenhaAntiga.getText().toString().isEmpty()) {
                    toast(getString(R.string.digita_senha_atual));
                } else if (editSenhaNova.getText().toString().isEmpty()) {
                    toast(getString(R.string.digita_senha_nova));
                } else if (editConfirmaSenha.getText().toString().isEmpty()) {
                    toast(getString(R.string.confirma_nova_senha));
                } else if (editSenhaAntiga.getText().toString()
                        .equals(editSenhaNova.getText().toString())) {
                    toast(getString(R.string.senhas_diferentes));
                } else if (!Objects.equals(editSenhaNova.getText().toString(),
                        editConfirmaSenha.getText().toString())) {
                    toast(getString(R.string.senha_nao_confirmada));
                    editSenhaNova.getText().clear();
                    editConfirmaSenha.getText().clear();
                    editSenhaNova.requestFocus();
                } else {
                    user.setPassword(editSenhaNova.getText().toString());
                    user.saveInBackground();
                    toast("Senha alterada com sucesso!");
                    dialog.dismiss();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setTitle(getString(R.string.alterar_senha));
        dialog.setIcon(R.drawable.ic_action_lock_closed);
        dialog.show();
    }

    @Override
    public void doBack() {
        if (btSalvaAlteracoes.getVisibility() == View.VISIBLE){
            retornaViews();
        } else {
            ((MinhaContaActivity) getActivity()).voltar();
        }
    }
}
