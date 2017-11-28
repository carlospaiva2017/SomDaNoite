package br.com.fafeltech.somdanoite.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.activity.DetailActivity;
import br.com.fafeltech.somdanoite.adapter.PesquisaLocalAdapter;
import br.com.fafeltech.somdanoite.helper.ParseErros;
import br.com.fafeltech.somdanoite.helper.RecyclerItemClickListener;
import br.com.fafeltech.somdanoite.model.Evento;

import static br.com.fafeltech.somdanoite.activity.MainActivity.LISTA_INICIAL;
import static br.com.fafeltech.somdanoite.activity.MainActivity.LOCAL_PESQUISA;
import static br.com.fafeltech.somdanoite.activity.MainActivity.MUSICO_PESQUISA;
import static br.com.fafeltech.somdanoite.activity.MainActivity.PESQUISA_TELA_INICIAL;
import static br.com.fafeltech.somdanoite.lists.Lists.pesquisaLocal;

public class PesquisaFragment extends BaseFragment {

    private TextView textoPesquisa;
    private RecyclerView recyclerView;
    private Bundle args;
    public Context context;
    private PesquisaLocalAdapter adapter;

    public PesquisaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);
        context = getActivity().getApplicationContext();
        textoPesquisa = (TextView) view.findViewById(R.id.text_titulo_pesquisa);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_pesquisa);
        args = getArguments();

        exibePesquisa();

        return view;
    }

    private void exibePesquisa() {
        int tipoPesquisa = args.getInt("pesquisa");
        abreRecyclerView();
        if (tipoPesquisa == MUSICO_PESQUISA) {
            abreRecyclerViewMusico();
        } else if (tipoPesquisa == LOCAL_PESQUISA) {
            pesquisaPorLocal();
        } else if (tipoPesquisa == PESQUISA_TELA_INICIAL) {
            pesquisaPorTermos();
        } else {
            snack("Não foi possível realizar a pesquisa. Reinicie o aplicativo e tente novamente");
        }
    }

    private void pesquisaPorTermos() {
        String termosPesquisa = args.getString("termosPesquisa");
        textoPesquisa.setText(getString(R.string.voce_pesquisou) + " " + termosPesquisa);

        ParseQuery<Evento> pesquisa1 = ParseQuery.getQuery(Evento.class);
        pesquisa1.whereContains("nome", termosPesquisa);
        ParseQuery<Evento> pesquisa2 = ParseQuery.getQuery(Evento.class);
        pesquisa2.whereContains("nome_local", termosPesquisa);
        ParseQuery<Evento> pesquisa3 = ParseQuery.getQuery(Evento.class);
        pesquisa3.whereContains("nome_artista", termosPesquisa);
        ParseQuery<Evento> pesquisa4 = ParseQuery.getQuery(Evento.class);
        pesquisa4.whereContains("content", termosPesquisa);
        ParseQuery<Evento> pesquisa5 = ParseQuery.getQuery(Evento.class);
        pesquisa5.whereContains("nome_upper_case", termosPesquisa);
        ParseQuery<Evento> pesquisa6 = ParseQuery.getQuery(Evento.class);
        pesquisa6.whereContains("nome_lower_case", termosPesquisa);
        ParseQuery<Evento> pesquisa7 = ParseQuery.getQuery(Evento.class);
        pesquisa7.whereContains("nome_local_upper_case", termosPesquisa);
        ParseQuery<Evento> pesquisa8 = ParseQuery.getQuery(Evento.class);
        pesquisa8.whereContains("nome_local_lower_case", termosPesquisa);
        ParseQuery<Evento> pesquisa9 = ParseQuery.getQuery(Evento.class);
        pesquisa9.whereContains("nome_artista_upper_case", termosPesquisa);
        ParseQuery<Evento> pesquisa10 = ParseQuery.getQuery(Evento.class);
        pesquisa10.whereContains("nome_artista_lower_case", termosPesquisa);
        ParseQuery<Evento> pesquisa11 = ParseQuery.getQuery(Evento.class);
        pesquisa11.whereContains("tags", termosPesquisa);
        ParseQuery<Evento> pesquisa = ParseQuery.or(Arrays.asList(pesquisa1, pesquisa2, pesquisa3, pesquisa4, pesquisa5, pesquisa6, pesquisa7, pesquisa8, pesquisa9, pesquisa10, pesquisa11));
        pesquisa.orderByAscending("date");
        pesquisa.findInBackground(new FindCallback<Evento>() {
            @Override
            public void done(List<Evento> objects, ParseException e) {
                if (e == null) {
                    pesquisaLocal.clear();
                    for (Evento evento : objects) {
                        pesquisaLocal.add(evento);
                    }
                    adapter.notifyDataSetChanged();
                    hideProgressDialog();
                } else {
                    ParseErros parseErros = new ParseErros();
                    String erro = parseErros.getErro(e.getCode());
                    snack(erro);
                    hideProgressDialog();
                }
            }
        });
    }

    private void abreRecyclerViewMusico() {
        showProgressDialog();
        String nomeArtista = args.getString("nomeArtista");
        textoPesquisa.setText(getString(R.string.proximas_apresentacoes) + " " + nomeArtista);

        ParseQuery<Evento> query = ParseQuery.getQuery(Evento.class);
        query.whereEqualTo("nome_artista", nomeArtista);
        query.orderByAscending("date");
        query.findInBackground(new FindCallback<Evento>() {
            @Override
            public void done(List<Evento> objects, ParseException e) {
                if (e == null) {
                    pesquisaLocal.clear();
                    for (Evento evento : objects) {
                        pesquisaLocal.add(evento);
                    }
                    adapter.notifyDataSetChanged();
                    hideProgressDialog();
                } else {
                    ParseErros parseErros = new ParseErros();
                    String erro = parseErros.getErro(e.getCode());
                    snack(erro);
                    hideProgressDialog();
                }
            }
        });
    }

    private void pesquisaPorLocal() {
        showProgressDialog();
        textoPesquisa.setText(getString(R.string.proximos_eventos) + " " + args.getString("nomeLocal"));

        String geoPoint = args.getString("geoPoint");
        ParseGeoPoint point = novoGeoPoint(geoPoint);

        ParseQuery<Evento> query = ParseQuery.getQuery(Evento.class);
//        query.whereWithinKilometers("local_mapa", point, 0.02);
        query.whereEqualTo("localId", args.getString("localId"));
        query.orderByAscending("date");
        query.findInBackground(new FindCallback<Evento>() {
            @Override
            public void done(List<Evento> objects, ParseException e) {
                if (e==null) {
                    pesquisaLocal.clear();
                    for (Evento evento : objects) {
                        pesquisaLocal.add(evento);
                    }
                    adapter.notifyDataSetChanged();
                    hideProgressDialog();
                } else {
                    e.printStackTrace();
                    toast(getString(R.string.page_error));
                    hideProgressDialog();
                }
            }
        });
    }

    private void abreRecyclerView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(context);
                adapter = new PesquisaLocalAdapter(getActivity());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                        getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Evento evento = pesquisaLocal.get(position);

                        Bundle args = new Bundle();
                        args.putString("nomeEvento", evento.getNome());
                        args.putString("nomeLocal", evento.getLocal());
                        args.putString("nomeArtista", evento.getNomeArtista());
                        args.putString("content", evento.getContent());
                        args.putString("userId", evento.getUserId());
                        args.putString("objectId", evento.getObjectId());
                        args.putString("localId", evento.getLocalId());

                        String geoPoint = String.valueOf(evento.getLocalMapa()).replace("ParseGeoPoint[", "").replace("]", "");
                        args.putString("geoPoint", geoPoint);
                        args.putInt("tipo", LISTA_INICIAL);

                        SimpleDateFormat desireFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String dateFormat = desireFormat.format(evento.getDate());
                        args.putString("dataEvento", dateFormat);

                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtras(args);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }
                ));
            }
        });
    }

    private ParseGeoPoint novoGeoPoint (String s) {
        String [] LtdLng = s.split(",");
        double ltd = convertStringToDoubleEnglish(LtdLng[0]);
        double lng = convertStringToDoubleEnglish(LtdLng[1]);
        return new ParseGeoPoint(ltd,lng);
    }
}
