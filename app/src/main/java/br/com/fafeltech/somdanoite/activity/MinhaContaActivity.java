package br.com.fafeltech.somdanoite.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.fragment.MinhaContaFragment;

public class MinhaContaActivity extends BaseActivity {

    protected OnBackPressedListener onBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_conta);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.minha_conta);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        iniciaMinhaContaFragment();
    }

    private void iniciaMinhaContaFragment() {
        MinhaContaFragment fragment = new MinhaContaFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.framelayout_conta, fragment)
                .commit();
    }

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }
}
