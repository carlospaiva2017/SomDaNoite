package br.com.fafeltech.somdanoite.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.parse.ParseUser;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.fragment.MainFragment;
import br.com.fafeltech.somdanoite.helper.BaseActivity;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static int LISTA_INICIAL = 1;
    public static int EDITA_EVENTO = 99;
    public static int MUSICO_PESQUISA = 2222;
    public static int LOCAL_PESQUISA = 3333;
    public static int PESQUISA_TELA_INICIAL = 4444;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//        ParseFacebookUtils.initialize(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.admob_banner));
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (ParseUser.getCurrentUser() != null) {
            Bundle args = new Bundle();
            args.putInt("tipo", LISTA_INICIAL);
            iniciaFragment(args);
        } else {
            deslogarUsuario();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.search:
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(false);
                searchView.setIconified(false);
                searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Intent intent = new Intent(MainActivity.this, PesquisaActivity.class);
                        Bundle args = new Bundle();
                        args.putInt("pesquisa", PESQUISA_TELA_INICIAL);
                        args.putString("termosPesquisa", s);
                        intent.putExtras(args);
                        startActivity(intent);
                        showProgressDialog();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });
                break;
            case R.id.minha_conta:
                Intent intent1 = new Intent(this, MinhaContaActivity.class);
                startActivity(intent1);
                break;
            case R.id.criar_evento:
                Intent intent2 = new Intent(this, CriaEventoActivity.class);
                Bundle args2 = new Bundle();
                args2.putInt("tipo", 1);
                intent2.putExtras(args2);
                startActivity(intent2);
                break;
            case R.id.sobre_app:
                startActivity(new Intent(this, SobreAppActivity.class));
                break;
            case R.id.sair:
                showProgressDialog();
                deslogarUsuario();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        ParseUser.logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        hideProgressDialog();
    }

    private void iniciaFragment(Bundle args){
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.framelayout_main, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.minhas_listas:
                toast("Minhas Listas");
                break;
            case R.id.fale_conosco:
                toast("Fale Conosco");
                break;
            case R.id.info_app:
                toast("Info");
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }
}
