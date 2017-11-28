package br.com.fafeltech.somdanoite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Objects;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.dialog.ArtistaDialog;
import br.com.fafeltech.somdanoite.dialog.EstabelecimentoDialog;
import br.com.fafeltech.somdanoite.dialog.PromoterDialog;
import br.com.fafeltech.somdanoite.fragment.BaseFragment;
import br.com.fafeltech.somdanoite.helper.ParseErros;

import static br.com.fafeltech.somdanoite.R.id.container;
import static br.com.fafeltech.somdanoite.activity.CriaEventoActivity.PLACE_PICKER_REQUEST;

public class CadastroActivity extends BaseActivity {

    private static ViewPager mViewPager;

    private static int categoria = 0;
    private static String nome;
    private static String email;
    private static boolean cadastroOK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            if (args.getInt("tipo") == 9009) {
                cadastroOK = true;
            }
        }

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);

        if (cadastroOK) {
            mViewPager.setCurrentItem(3);
        }
    }

    public static class PlaceholderFragment extends BaseFragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (sectionNumber) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_cadastro1, container, false);
                    iniciaViews(rootView, sectionNumber);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_cadastro2, container, false);
                    iniciaViews(rootView, sectionNumber);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_cadastro3, container, false);
                    iniciaViews(rootView, sectionNumber);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_cadastro4, container, false);
                    iniciaViews(rootView, sectionNumber);
                    break;
                default: return null;
            }
            return rootView;
        }

        private void iniciaViews(View rootView, int sectionNumber) {
            switch (sectionNumber) {
                case 1:
                    Button bt1 = (Button) rootView.findViewById(R.id.bt_email1);
                    Button bt2 = (Button) rootView.findViewById(R.id.bt_email2);
                    Button bt3 = (Button) rootView.findViewById(R.id.bt_email3);
                    Button bt4 = (Button) rootView.findViewById(R.id.bt_email4);

                    bt1.setOnClickListener( setCategoria(1) );
                    bt2.setOnClickListener( setCategoria(2) );
                    bt3.setOnClickListener( setCategoria(3) );
                    bt4.setOnClickListener( setCategoria(4) );

                    break;

                case 2:
                    final AutoCompleteTextView textoNome = (AutoCompleteTextView) rootView.findViewById(R.id.texto_nome);
                    final AutoCompleteTextView textoEmail = (AutoCompleteTextView) rootView.findViewById(R.id.texto_email);

                    Button continuar1 = (Button) rootView.findViewById(R.id.bt_continuar1);

                    continuar1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (textoNome.getText().length() < 1) {
                                snack("Digite seu nome.");
                            } else if (textoEmail.getText().length() < 1) {
                                snack("Digite seu e-mail");
                            } else {
                                nome = textoNome.getText().toString();
                                email = textoEmail.getText().toString();
                                mViewPager.setCurrentItem(2);
                            }
                        }
                    });
                    break;

                case 3:
                    final AutoCompleteTextView textoUsuario = (AutoCompleteTextView) rootView.findViewById(R.id.texto_usuario);
                    final AutoCompleteTextView textoSenha = (AutoCompleteTextView) rootView.findViewById(R.id.texto_senha);
                    final AutoCompleteTextView confirmaSenha = (AutoCompleteTextView) rootView.findViewById(R.id.texto_confirma_senha);

                    Button continuar2 = (Button) rootView.findViewById(R.id.bt_continuar2);

                    continuar2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showProgressDialog();

                            if (textoSenha.length() < 6) {
                                textoSenha.getText().clear();
                                confirmaSenha.getText().clear();
                                textoSenha.requestFocus();
                                hideProgressDialog();
                                snack(getString(R.string.senha_caracteres_minimos));
                            } else if (!Objects.equals(textoSenha.getText().toString(), confirmaSenha.getText().toString())) {
                                textoSenha.getText().clear();
                                confirmaSenha.getText().clear();
                                textoSenha.requestFocus();
                                hideProgressDialog();
                                snack(getString(R.string.senha_nao_confere));
                            } else {

                                if (categoria == 0){
                                    mViewPager.setCurrentItem(1);
                                    snack("Selecione uma categoria de cadastro");
                                } else if (nome.isEmpty()) {
                                    mViewPager.setCurrentItem(2);
                                    snack("Preencha seu nome completo.");
                                } else if (email.isEmpty()) {
                                    snack("Preencha seu e-mail.");
                                } else {

                                    ParseUser usuario = new ParseUser();

                                    usuario.setUsername(textoUsuario.getText().toString());
                                    usuario.put("nome", nome);
                                    usuario.put("nome_artista", " ");
                                    usuario.put("categoria", categoria);
                                    usuario.setEmail(email);
                                    usuario.setPassword(textoSenha.getText().toString());

                                    //Salvar dados do usuário
                                    usuario.signUpInBackground(new SignUpCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                if (categoria == 1) {
                                                    ArtistaDialog artistaDialog = new ArtistaDialog(getActivity());
                                                    artistaDialog.show();
                                                } else if (categoria == 2) {
                                                    EstabelecimentoDialog estabelecimentoDialog = new EstabelecimentoDialog(getContext(), getActivity());
                                                    estabelecimentoDialog.show();
                                                } else if (categoria == 3) {
                                                    PromoterDialog promoterDialog = new PromoterDialog(getActivity());
                                                    promoterDialog.show();
                                                } else {
                                                    hideProgressDialog();
                                                    toast("Cadastro efetuado com sucesso");
                                                    cadastroOK = true;
                                                    SectionsPagerAdapter adapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
                                                    mViewPager.setAdapter(adapter);
                                                    mViewPager.setCurrentItem(3);
                                                }
                                            } else {
                                                hideProgressDialog();
                                                ParseErros parseErros = new ParseErros();
                                                String erro = parseErros.getErro(e.getCode());
                                                if (e.getCode() == 202) {
                                                    textoUsuario.getText().clear();
                                                    snack(erro);
                                                    textoUsuario.requestFocus();
                                                } else if (!erro.isEmpty()) {
                                                    snack(erro);
                                                    if (e.getCode() == 203 || e.getCode() == 204) {
                                                        mViewPager.setCurrentItem(1);
                                                    }
                                                } else {
                                                    snack(String.valueOf(e.getCode()));
                                                }
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                    break;

                case 4:
                    Button iniciaApp = (Button) rootView.findViewById(R.id.bt_iniciar_app);
                    TextView texto1 = (TextView) rootView.findViewById(R.id.texto1);
                    TextView texto2 = (TextView) rootView.findViewById(R.id.texto2);
                    TextView texto3 = (TextView) rootView.findViewById(R.id.texto3);
                    TextView texto4 = (TextView) rootView.findViewById(R.id.texto4);

                    if (cadastroOK) {
                        texto2.setText(getString(R.string.agora_voc_j_pode_utilizar_o_aplicativo_som_da_noite));

                        iniciaApp.setVisibility(View.VISIBLE);
                        texto1.setVisibility(View.VISIBLE);
                        texto2.setVisibility(View.VISIBLE);
                        texto3.setVisibility(View.VISIBLE);
                        texto4.setVisibility(View.VISIBLE);

                        iniciaApp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

                    } else {
                        texto2.setText(R.string.conclua_cadastro);
                        texto2.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }

        private View.OnClickListener setCategoria(final int i) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    categoria = i;
                    mViewPager.setCurrentItem(1);
                }
            };
        }
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return "";
            }
            return null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String localId = place.getId();
                ParseGeoPoint geoPoint = new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                String nomeLocal;
                if (place.getName() != null) {
                    nomeLocal = place.getName().toString();
                } else {
                    nomeLocal = "Pesquisa não retornou nome";
                }
                EstabelecimentoDialog dialog = new EstabelecimentoDialog(this, this, geoPoint, localId, nomeLocal);
                dialog.show();
            } else {
                toast("Erro ao adicionar local.");
            }
        }
    }
}
