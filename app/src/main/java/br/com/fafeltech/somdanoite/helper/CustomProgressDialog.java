package br.com.fafeltech.somdanoite.helper;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import br.com.fafeltech.somdanoite.R;

public class CustomProgressDialog extends AlertDialog {

    private CharSequence mensagem;
    private int tipo = 0;

    public CustomProgressDialog(@NonNull Context context) {
        super(context);
    }

    public CustomProgressDialog(@NonNull Context context, int tipo) {
        super(context, tipo);
        this.tipo = tipo;
    }

    public CustomProgressDialog(@NonNull Context context, CharSequence mensagem) {
        super(context);
        this.mensagem = mensagem;
    }

    public CustomProgressDialog(@NonNull Context context, CharSequence mensagem, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mensagem = mensagem;
    }

    public CustomProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_dialog);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (tipo == 0) {
            tipo = 1;
        }

        if (mensagem != null && mensagem.length() > 0){
            ((TextView) findViewById(R.id.dialog_textview)).setText(mensagem);
            findViewById(R.id.dialog_textview).setVisibility(View.VISIBLE);
        }

        initProgress();
    }

    private void initProgress() {
        switch (tipo) {
            case 1:
                findViewById(R.id.pd_modelo_1).setVisibility(View.VISIBLE);
                break;
            case 2:
                findViewById(R.id.pd_modelo_2).setVisibility(View.VISIBLE);
                break;
            case 3:
                findViewById(R.id.pd_modelo_3).setVisibility(View.VISIBLE);
                break;
            case 4:
                findViewById(R.id.pd_modelo_4).setVisibility(View.VISIBLE);
                break;
            case 5:
                findViewById(R.id.pd_modelo_5).setVisibility(View.VISIBLE);
                break;
            case 6:
                findViewById(R.id.pd_modelo_6).setVisibility(View.VISIBLE);
                break;
            case 7:
                findViewById(R.id.pd_modelo_7).setVisibility(View.VISIBLE);
                break;
            case 8:
                findViewById(R.id.pd_modelo_8).setVisibility(View.VISIBLE);
                break;
        }
    }
}
