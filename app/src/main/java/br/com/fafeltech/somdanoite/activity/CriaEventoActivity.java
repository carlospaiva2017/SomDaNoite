package br.com.fafeltech.somdanoite.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.GetCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.helper.BaseActivity;
import br.com.fafeltech.somdanoite.helper.Masking;
import br.com.fafeltech.somdanoite.helper.Permissao;
import br.com.fafeltech.somdanoite.model.Evento;

import static br.com.fafeltech.somdanoite.activity.MainActivity.EDITA_EVENTO;

public class CriaEventoActivity extends BaseActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private java.text.SimpleDateFormat simpleDateFormat;
    private EditText textoNomeEvento;
    private EditText dataEvento;
    private EditText horaEvento;
    private EditText content;

    private GoogleApiClient googleApiClient;
    private TextView selecionaLocal;
    private ImageView iconeLocal;
    private String nomeLocal;
    private ParseGeoPoint geoPoint;
    private TextView editNomeLocal;
    private String localId;

    private Bundle args;

    public static int PLACE_PICKER_REQUEST = 1100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_evento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_criar_evento);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.botao_criar_evento));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        // para o Google Places
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        textoNomeEvento = (EditText) findViewById(R.id.texto_nome_evento);
        dataEvento = (EditText) findViewById(R.id.data_evento);
        horaEvento = (EditText) findViewById(R.id.hora_evento);
        content = (EditText) findViewById(R.id.descricao_evento);

        selecionaLocal = (TextView) findViewById(R.id.texto_seleciona_local);
        iconeLocal = (ImageView) findViewById(R.id.img_seleciona_local);
        editNomeLocal = (TextView) findViewById(R.id.edit_nome_local);

        selecionaLocal.setOnClickListener(selecionaLocal());
        iconeLocal.setOnClickListener(selecionaLocal());
        editNomeLocal.setOnClickListener( editarNomeLocal() );

        Masking maskDate = new Masking("##/##/####", dataEvento);
        Masking maskHora = new Masking("##:##", horaEvento);
        dataEvento.addTextChangedListener(maskDate);
        horaEvento.addTextChangedListener(maskHora);

        Button criarEvento = (Button) findViewById(R.id.botao_criar_evento);
        Button excluiEvento = (Button) findViewById(R.id.botao_excluir_evento);

        args = getIntent().getExtras();
        if (args.getInt("tipo") == EDITA_EVENTO) {
            textoNomeEvento.setText(args.getString("nomeEvento"));
            selecionaLocal.setText(args.getString("nomeLocal"));
            dataEvento.setText(args.getString("dataEvento").substring(0, 10));
            horaEvento.setText(args.getString("dataEvento").substring(11,16));
            content.setText(args.getString("content"));
            criarEvento.setText(R.string.altera_evento);
            excluiEvento.setVisibility(View.VISIBLE);
        }

        TextView voltar = (TextView) findViewById(R.id.texto_voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (args.getInt("tipo") == EDITA_EVENTO) {
                    retornaDetailFragment(args);
                }else {
                    voltar();
                }
            }
        });

        criarEvento.setOnClickListener(criaEvento());
        excluiEvento.setOnClickListener(excluiEvento());
    }

    private View.OnClickListener excluiEvento() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<Evento> query = ParseQuery.getQuery("Evento");
                query.getInBackground(args.getString("objectId"), new GetCallback<Evento>() {
                    @Override
                    public void done(Evento object, com.parse.ParseException e) {
                        if ( e == null) {
                            object.deleteInBackground();
                            toast("Evento excluído!");
                            voltar();
                        }else {
                            toast("Erro ao excluir Evento");
                        }
                    }
                });
            }
        };
    }

    private View.OnClickListener editarNomeLocal() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CriaEventoActivity.this);
                builder.setIcon(R.drawable.ic_action_location);
                builder.setTitle("Editar nome do Local do Evento");
                builder.setMessage("Digite o nome para o local do Evento");

                final EditText editNovoNome = new EditText(CriaEventoActivity.this);
                builder.setView(editNovoNome);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String novoNome = editNovoNome.getText().toString();
                        selecionaLocal.setText(novoNome);
                        nomeLocal = novoNome;
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
    }

    private View.OnClickListener criaEvento (){
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                final String nomeEvento = textoNomeEvento.getText().toString();
                final String conteudo = content.getText().toString();

                if (args.getInt("tipo") == EDITA_EVENTO) {
                    showProgressDialog();
                    try {
                        final Date date = simpleDateFormat.parse(dataEvento.getText().toString()+" "+horaEvento.getText().toString());

                        ParseQuery<Evento> query = ParseQuery.getQuery("Evento");

                        query.getInBackground(args.getString("objectId"), new GetCallback<Evento>() {
                            @Override
                            public void done(Evento object, com.parse.ParseException e) {
                                nomeLocal = selecionaLocal.getText().toString();
                                if ( e == null) {
                                    object.setNome(nomeEvento);
                                    object.setLocal(nomeLocal);
                                    object.setDate(date);
                                    object.setContent(conteudo);
                                    if (geoPoint != null){
                                        object.setLocalMapa(geoPoint);
                                    }
                                    object.saveInBackground();
                                    toast("Evento alterado com Sucesso!!");
                                    hideProgressDialog();

                                    insereNovosArgumentos(object);
                                }else {
                                    toast("Erro ao salvar Evento");
                                    hideProgressDialog();
                                }
                            }
                        });

                    } catch (ParseException e) {
                        e.printStackTrace();
                        hideProgressDialog();
                    }

                }else {
                    showProgressDialog();
                    try {
                        Date date = simpleDateFormat.parse(dataEvento.getText().toString()+" "+horaEvento.getText().toString());
                        if (nomeLocal == null){
                            toast("Selecione o local do Evento");
                            hideProgressDialog();
                        } else {
                            String nomeArtista = ParseUser.getCurrentUser().getString("nome_artista");

                            String nomeUpperCase = nomeEvento.toUpperCase();
                            String nomeLowerCase = nomeEvento.toLowerCase();
                            String nomeLocalUpperCase = nomeLocal.toUpperCase();
                            String nomeLocalLowerCase = nomeLocal.toLowerCase();
                            String nomeArtistaUpperCase = nomeArtista.toUpperCase();
                            String nomeArtistaLowerCase = nomeArtista.toLowerCase();

                            String nomeTag = convertSpecialCharacters(nomeEvento) + " " + convertSpecialCharacters(nomeUpperCase) + " " + convertSpecialCharacters(nomeLowerCase);
                            String nomeLocalTag = convertSpecialCharacters(nomeLocal) + " " + convertSpecialCharacters(nomeLocalUpperCase) + " " + convertSpecialCharacters(nomeLocalLowerCase);
                            String nomeArtistaTag = convertSpecialCharacters(nomeArtista) + " " + convertSpecialCharacters(nomeArtistaUpperCase) + " " + convertSpecialCharacters(nomeArtistaLowerCase);
                            String tags = nomeTag + " " + nomeLocalTag + " " + nomeArtistaTag;

                            Evento evento = new Evento();
                            evento.add(nomeEvento,
                                    nomeUpperCase,
                                    nomeLowerCase,
                                    nomeLocal,
                                    nomeLocalUpperCase,
                                    nomeLocalLowerCase,
                                    geoPoint,
                                    localId,
                                    date,
                                    nomeArtista,
                                    nomeArtistaUpperCase,
                                    nomeArtistaLowerCase,
                                    ParseUser.getCurrentUser().getObjectId(),
                                    conteudo,
                                    tags);

                            toast("Evento cadastrado com Sucesso!!");
                            hideProgressDialog();
                            voltar();
                            finish();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        toast("Não foi possível cadastrar o evento!");
                        hideProgressDialog();
                    }
                }
            }
        };
    }

    private void insereNovosArgumentos(Evento evento) {
        args.putString("nomeEvento", evento.getNome());
        args.putString("nomeLocal", evento.getLocal());
        args.putString("nomeArtista", evento.getNomeArtista());
        args.putString("content", evento.getContent());
        args.putString("userId", evento.getUserId());
        args.putString("objectId", evento.getObjectId());

        SimpleDateFormat desireFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateFormat = desireFormat.format(evento.getDate());
        args.putString("dataEvento", dateFormat);

        retornaDetailFragment(args);
        finish();
    }

    private View.OnClickListener selecionaLocal(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                String [] permissoes = {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

                Permissao.validaPermissoes(1, CriaEventoActivity.this, permissoes);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    try {
                        PlacePicker.IntentBuilder intentBuilder =
                                new PlacePicker.IntentBuilder();
                        startActivityForResult(intentBuilder.build(CriaEventoActivity.this), PLACE_PICKER_REQUEST);

                    } catch (Exception e) {
                        e.printStackTrace();
                        toast("Erro ao capturar localização...");
                    }
                } else {
                    toast("Você precisa autorizar o uso de GPS para enviar sua localização...");
                    hideProgressDialog();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                localId = place.getId();
                if (place.getName() != null) {
                    nomeLocal = place.getName().toString();
                    selecionaLocal.setText(nomeLocal);
                } else {
                    selecionaLocal.setText(getString(R.string.local_sem_nome));
                }
                geoPoint = new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                iconeLocal.setVisibility(View.GONE);
                editNomeLocal.setVisibility(View.VISIBLE);
                toast("Local adicionado com sucesso: " + place.getName());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void retornaDetailFragment(Bundle args) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }
}
