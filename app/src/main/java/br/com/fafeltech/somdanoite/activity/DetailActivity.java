package br.com.fafeltech.somdanoite.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.fragment.DetailFragment;
import br.com.fafeltech.somdanoite.helper.BaseActivity;

public class DetailActivity extends BaseActivity {

    private Bundle args;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        args = getIntent().getExtras();
        iniciaDetailFragment(args);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(interstitialListener());
    }

    private void iniciaDetailFragment(Bundle args) {
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.framelayout_detail, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Log.d("ADMOB", "Interstitial não está carregado ainda.");
            voltar();
        }
    }

    private AdListener interstitialListener () {
        return new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                voltar();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.i("ADMOB: ", "Não carregou Interstitial.");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i("ADMOB: ", "Interstitial carregado.");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                voltar();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        };
    }
}
