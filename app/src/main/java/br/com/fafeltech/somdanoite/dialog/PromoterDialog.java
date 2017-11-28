package br.com.fafeltech.somdanoite.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

import com.parse.ParseUser;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.activity.CadastroActivity;

public class PromoterDialog extends BaseDialog {
    public PromoterDialog(Context context) {
        super(context);
    }

    private AutoCompleteTextView digitaNome;
    private CheckBox checkBox;
    private boolean selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_promoter);
        setCanceledOnTouchOutside(true);

        digitaNome = (AutoCompleteTextView) findViewById(R.id.nome_promoter_dialog);
        checkBox = (CheckBox) findViewById(R.id.checkbox_dialog_promoter);
        Button bt = (Button) findViewById(R.id.bt_continuar_dialog_promoter);

        checkBox.setOnClickListener(onClickCheckBox());
        bt.setOnClickListener(continuar());

    }

    private View.OnClickListener continuar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (digitaNome.getText().length() < 1) {
                    toast("Digite um nome para exibição aos usuários.");
                } else {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("nome_promoter", digitaNome.getText().toString());
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

    private View.OnClickListener onClickCheckBox() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = !selected;
                ParseUser user = ParseUser.getCurrentUser();
                String nomeUsuario = user.getString("nome");
                if (selected) {
                    digitaNome.setText(nomeUsuario);
                } else {
                    digitaNome.getText().clear();
                }

            }
        };
    }
}
