package br.com.fafeltech.somdanoite.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.activity.DetailActivity;
import br.com.fafeltech.somdanoite.adapter.EventoAdapter;
import br.com.fafeltech.somdanoite.helper.RecyclerItemClickListener;
import br.com.fafeltech.somdanoite.model.Evento;

import static br.com.fafeltech.somdanoite.activity.MainActivity.LISTA_INICIAL;
import static br.com.fafeltech.somdanoite.lists.Lists.eventos;

public class MainFragment extends BaseFragment {

    private RecyclerView recyclerView;
    public Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EventoAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        context = getActivity().getApplicationContext();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sr_main);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inflarRecyclerView();
            }
        });

        inflarRecyclerView();

        return view;
    }

    private void inflarRecyclerView() {
        int tipo = getArguments().getInt("tipo");
        if (tipo == LISTA_INICIAL) { preencherListaProduto(); }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(context);
                adapter = new EventoAdapter(getActivity());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                        getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Evento evento = eventos.get(position);

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

    private void preencherListaProduto() {
        showProgressDialog();
        ParseQuery<Evento> query = ParseQuery.getQuery(Evento.class);
        query.orderByAscending("date");

        query.findInBackground(new FindCallback<Evento>() {
            @Override
            public void done(List<Evento> objects, ParseException e) {
                if (e == null) { //sucesso
                    if (objects.size() > 0) {
                        eventos.clear();
                        for (Evento evento: objects) {
                            eventos.add(evento);
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        hideProgressDialog();
                    } else {
                        toast(getString(R.string.no_records));
                        swipeRefreshLayout.setRefreshing(false);
                        hideProgressDialog();
                    }
                } else { //erro
                    e.printStackTrace();
                    toast(getString(R.string.page_error));
                    swipeRefreshLayout.setRefreshing(false);
                    hideProgressDialog();
                }
            }
        });
    }
}
