package br.com.fafeltech.somdanoite.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.activity.CriaEventoActivity;
import br.com.fafeltech.somdanoite.activity.PesquisaActivity;

import static br.com.fafeltech.somdanoite.activity.MainActivity.EDITA_EVENTO;
import static br.com.fafeltech.somdanoite.activity.MainActivity.LOCAL_PESQUISA;
import static br.com.fafeltech.somdanoite.activity.MainActivity.MUSICO_PESQUISA;

public class DetailFragment extends BaseFragment {

    public Context context;
    private TextView tituloEvento;
    private TextView dNomeEvento;
    private TextView dNomeArtista;
    private TextView dLocalEvento;
    private TextView dDataEvento;
    private TextView dConteudo;
    private ImageView editEvento;
    private Bundle args;
    private ImageView favorito;
    private boolean selectedFavorito = false;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        context = getActivity().getApplicationContext();
        tituloEvento = (TextView) view.findViewById(R.id.titulo_evento);
        dNomeEvento = (TextView) view.findViewById(R.id.detail_nome_evento);
        dNomeArtista = (TextView) view.findViewById(R.id.detail_nome_artista);
        dLocalEvento = (TextView) view.findViewById(R.id.detail_local_evento);
        dDataEvento = (TextView) view.findViewById(R.id.detail_data_evento);
        dConteudo = (TextView) view.findViewById(R.id.detail_conteudo_evento);
        editEvento = (ImageView) view.findViewById(R.id.edit_evento);
        favorito = (ImageView) view.findViewById(R.id.image_favorite);

        favorito.setOnClickListener( setFavorite());

        args = getArguments();

        if (selectedFavorito) {
            favorito.setImageDrawable(getActivity().getDrawable(R.drawable.ic_action_star_10));
            favorito.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.amarelo)));
        } else {
            favorito.setImageDrawable(getActivity().getDrawable(R.drawable.ic_action_star_0));
            favorito.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.gelo)));
        }

        preencheCardView();

        dNomeArtista.setOnClickListener( iniciaPesquisaMusico() );
        dLocalEvento.setOnClickListener( iniciaPesquisaLocal() );

        return view;
    }

    private View.OnClickListener setFavorite() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFavorito = !selectedFavorito;
                if (selectedFavorito) {
                    favorito.setImageDrawable(getActivity().getDrawable(R.drawable.ic_action_star_10));
                    favorito.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.amarelo)));
                } else {
                    favorito.setImageDrawable(getActivity().getDrawable(R.drawable.ic_action_star_0));
                    favorito.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.gelo)));
                }
            }
        };
    }

    private View.OnClickListener iniciaPesquisaLocal() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putInt("pesquisa", LOCAL_PESQUISA);
                Intent intent = new Intent(getActivity(), PesquisaActivity.class);
                intent.putExtras(args);
                startActivity(intent);
                getActivity().finish();
            }
        };
    }

    private View.OnClickListener iniciaPesquisaMusico() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putInt("pesquisa", MUSICO_PESQUISA);
                Intent intent = new Intent(getActivity(), PesquisaActivity.class);
                intent.putExtras(args);
                startActivity(intent);
                getActivity().finish();
            }
        };
    }

    private void preencheCardView() {

        dNomeArtista.setPaintFlags(dNomeArtista.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        dLocalEvento.setPaintFlags(dLocalEvento.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tituloEvento.setText(args.getString("nomeEvento"));
        dNomeEvento.setText(args.getString("nomeEvento"));
        dNomeArtista.setText(args.getString("nomeArtista"));
        dLocalEvento.setText(args.getString("nomeLocal"));
        dDataEvento.setText(args.getString("dataEvento"));
        dConteudo.setText(args.getString("content"));

        String userId = args.getString("userId");

        if (ParseUser.getCurrentUser().getObjectId().equals(userId)) {
            editEvento.setVisibility(View.VISIBLE);
            editEvento.setOnClickListener(editarEvento());
        }
    }

    private View.OnClickListener editarEvento() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putInt("tipo", EDITA_EVENTO);
                Intent intent = new Intent(getActivity(), CriaEventoActivity.class);
                intent.putExtras(args);
                startActivity(intent);
                getActivity().finish();
            }
        };
    }
}
