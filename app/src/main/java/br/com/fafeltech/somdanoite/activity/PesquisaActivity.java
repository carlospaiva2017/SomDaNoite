package br.com.fafeltech.somdanoite.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.fragment.PesquisaFragment;

public class PesquisaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pesquisa);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        Bundle args = getIntent().getExtras();
        iniciaFragment(args);
    }

    private void iniciaFragment(Bundle args) {
        PesquisaFragment fragment = new PesquisaFragment();
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.framelayout_pesquisa, fragment)
                .commit();
    }
}
