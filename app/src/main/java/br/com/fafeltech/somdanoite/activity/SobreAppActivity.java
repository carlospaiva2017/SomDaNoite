package br.com.fafeltech.somdanoite.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import br.com.fafeltech.somdanoite.R;

public class SobreAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_app);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sobre_app);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
}
