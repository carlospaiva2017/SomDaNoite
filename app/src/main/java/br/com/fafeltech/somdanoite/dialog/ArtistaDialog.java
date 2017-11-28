package br.com.fafeltech.somdanoite.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

import com.parse.ParseUser;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.activity.CadastroActivity;

public class ArtistaDialog extends BaseDialog {

    private AutoCompleteTextView nomeArtista;
    private AutoCompleteTextView descricaoArtista;
    private CheckBox checkBox;
    private boolean selectedCB = false;

    public ArtistaDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_artista);
        setCanceledOnTouchOutside(true);

        nomeArtista = (AutoCompleteTextView) findViewById(R.id.nome_artista_dialog);
        descricaoArtista = (AutoCompleteTextView) findViewById(R.id.descricao_artista_dialog);
        checkBox = (CheckBox) findViewById(R.id.checkbox_dialog_artista);

        nomeArtista.setFocusable(true);
        checkBox.setOnClickListener( clickCheckBox());

        Button botao = (Button) findViewById(R.id.bt_continuar_dialog_artista);
        botao.setOnClickListener( cadastraDadosArtista());
    }

    private View.OnClickListener clickCheckBox() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser user = ParseUser.getCurrentUser();
                String nomeArtistico;
                nomeArtistico = user.getString("nome");
                selectedCB = !selectedCB;
                if (selectedCB) {
                    nomeArtista.setText(nomeArtistico);
                } else {
                    nomeArtista.getText().clear();
                }
            }
        };
    }

    private View.OnClickListener cadastraDadosArtista() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(nomeArtista.getText().length() > 0) ) {
                    toast("Preencha um nome artístico.");
                } else {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("nome_artista", nomeArtista.getText().toString());
                    if (descricaoArtista.getText().length() > 0) {
                        user.put("descricao_artista", descricaoArtista.getText().toString());
                    }
                    user.saveInBackground();
                    Bundle args = new Bundle();
                    args.putInt("tipo", 9009);
                    Intent intent = new Intent(getContext(), CadastroActivity.class);
                    intent.putExtras(args);
                    getContext().startActivity(intent);
                    dismiss();
                }
            }
        };
    }
}
