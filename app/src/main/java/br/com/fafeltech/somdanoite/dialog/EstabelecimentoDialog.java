package br.com.fafeltech.somdanoite.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.activity.CadastroActivity;
import br.com.fafeltech.somdanoite.helper.Masking;
import br.com.fafeltech.somdanoite.helper.Permissao;

import static br.com.fafeltech.somdanoite.activity.CriaEventoActivity.PLACE_PICKER_REQUEST;


public class EstabelecimentoDialog extends BaseDialog implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public Context context;
    private ImageView imagemLocal;
    private TextView nomeLocal;
    private CheckBox checkBox1;
    private Spinner spinnerDia1;
    private Spinner spinnerDia2;
    private EditText editHora1;
    private EditText editHora2;

    private ParseGeoPoint geoPoint;
    private String localId;
    private String getNomeLocal;
    private boolean todosOsDias = false;
    private GoogleApiClient googleApiClient;
    private Activity activity;

    private String [] dias = new String[] {
            " ",
            "Segunda    ",
            "Terça    ",
            "Quarta    ",
            "Quinta    ",
            "Sexta    ",
            "Sábado    ",
            "Domingo    "
    };

    public EstabelecimentoDialog(Context context) {
        super(context);
    }

    public EstabelecimentoDialog(Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    public EstabelecimentoDialog(Context context, Activity activity, ParseGeoPoint geoPoint, String localId, String nomeLocal) {
        super(context);
        this.localId = localId;
        this.geoPoint = geoPoint;
        this.getNomeLocal = nomeLocal;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_estabelecimento);
        setCanceledOnTouchOutside(true);

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        imagemLocal = (ImageView) findViewById(R.id.imagem_local);
        nomeLocal = (TextView) findViewById(R.id.nome_local);
        TextView selecionaOutro = (TextView) findViewById(R.id.edit_nome_local);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox_dialog_estabelecimento1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkbox_dialog_estabelecimento2);
        TextView textoCheckBox1 = (TextView) findViewById(R.id.texto_checkbox1);
        spinnerDia1 = (Spinner) findViewById(R.id.spinner_dia1);
        spinnerDia2 = (Spinner) findViewById(R.id.spinner_dia2);
        editHora1 = (EditText) findViewById(R.id.edit_hora1);
        editHora2 = (EditText) findViewById(R.id.edit_hora2);
        Button btContinuar = (Button) findViewById(R.id.bt_continuar_dialog_estabelecimento);

        Masking maskHora1 = new Masking("##:##", editHora1);
        Masking maskHora2 = new Masking("##:##", editHora2);
        editHora1.addTextChangedListener(maskHora1);
        editHora2.addTextChangedListener(maskHora2);

        imagemLocal.setOnClickListener(selecionaLocal());
        selecionaOutro.setOnClickListener(selecionaLocal());

        ArrayAdapter<String> adapterDias = new ArrayAdapter<String>(
                getContext(),
                R.layout.text_spinner,
                dias
        );

        spinnerDia1.setAdapter(adapterDias);
        spinnerDia2.setAdapter(adapterDias);

        if (geoPoint == null) {
            nomeLocal.setVisibility(View.GONE);
            checkBox1.setVisibility(View.GONE);
            textoCheckBox1.setVisibility(View.GONE);
            selecionaOutro.setVisibility(View.GONE);
            imagemLocal.setVisibility(View.VISIBLE);

        } else {
            nomeLocal.setVisibility(View.VISIBLE);
            imagemLocal.setVisibility(View.GONE);
            checkBox1.setVisibility(View.VISIBLE);
            textoCheckBox1.setVisibility(View.VISIBLE);
            selecionaOutro.setVisibility(View.VISIBLE);

            nomeLocal.setText(getNomeLocal);
        }

        checkBox2.setOnClickListener(defineDias());

        btContinuar.setOnClickListener(onClickContinuar());

    }

    private View.OnClickListener defineDias() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todosOsDias = !todosOsDias;
                if (todosOsDias) {
                    spinnerDia1.setSelection(1);
                    spinnerDia2.setSelection(1);
                } else {
                    spinnerDia1.setSelection(0);
                    spinnerDia2.setSelection(0);
                }
            }
        };
    }

    private View.OnClickListener onClickContinuar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagemLocal.getVisibility() == View.VISIBLE) {
                    toast("Clique na imagem para selecionar a localização do estabelecimento.");
                } else if (spinnerDia1.getSelectedItemPosition() == 0 || spinnerDia2.getSelectedItemPosition() == 0) {
                    toast("Selecione os dias de funcionamento do estabelecimento.");
                } else if (editHora1.getText().length() < 1 || editHora2.getText().length() < 1) {
                    toast("Digite o horário de funcionamento do estabelecimento.");
                } else {
                    ParseUser user = ParseUser.getCurrentUser();
                    if (checkBox1.isChecked()) {
                        user.put("nome", nomeLocal.getText().toString());
                    }
                    String dia1 = spinnerDia1.getSelectedItem().toString();
                    String dia2 = spinnerDia2.getSelectedItem().toString();
                    String [] _dia1 = dia1.split(" ");
                    String [] _dia2 = dia2.split(" ");
                    String horarioFuncionamento = "De " + _dia1[0] +
                            " a " + _dia2[0] + ", das " +
                            editHora1.getText().toString() + " às " +
                            editHora2.getText().toString() + " horas";
                    user.put("horario_funcionamento", horarioFuncionamento);
                    user.put("local_mapa", geoPoint);
                    user.put("localId", localId);
                    user.saveInBackground();
                    toast("Cadastro efetuado com sucesso.");
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

                Permissao.validaPermissoes(1, activity, permissoes);

                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    try {
                        PlacePicker.IntentBuilder intentBuilder =
                                new PlacePicker.IntentBuilder();
                        activity.startActivityForResult(intentBuilder.build(activity), PLACE_PICKER_REQUEST);

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
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }
}
