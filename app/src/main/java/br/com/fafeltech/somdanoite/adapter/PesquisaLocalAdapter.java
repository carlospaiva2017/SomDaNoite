package br.com.fafeltech.somdanoite.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.model.Evento;

import static br.com.fafeltech.somdanoite.lists.Lists.pesquisaLocal;

public class PesquisaLocalAdapter extends RecyclerView.Adapter<PesquisaLocalAdapter.eventoViewHolder> {

    public FragmentActivity mActivity;

    public PesquisaLocalAdapter(FragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public eventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_evento, parent, false);
        return new eventoViewHolder (view);
    }

    @Override
    public void onBindViewHolder(PesquisaLocalAdapter.eventoViewHolder holder, int position) {
        holder.bindView(position, mActivity);
    }

    @Override
    public int getItemCount() {
        return pesquisaLocal.size();
    }

    public static class eventoViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll = (LinearLayout) itemView.findViewById(R.id.ll_evento);
        ImageView image = (ImageView) itemView.findViewById(R.id.evento_imagem);
        TextView nomeEvento = (TextView) itemView.findViewById(R.id.nome_evento);
        TextView nomeArtista = (TextView) itemView.findViewById(R.id.nome_artista);
        TextView localEvento = (TextView) itemView.findViewById(R.id.local_evento);
        TextView dataEvento = (TextView) itemView.findViewById(R.id.data_evento);

        public eventoViewHolder(View itemView) {
            super(itemView);
        }

        public void bindView(int position, FragmentActivity activity) {

            if (pesquisaLocal.size()>0){

                Evento e = pesquisaLocal.get(position);

//                image.setAdjustViewBounds(true);
//                Picasso.with(activity)
//                        .load(e.getParseFile("imagem").getUrl())
//                        .fit()
//                        .into(image);

                nomeEvento.setText(e.getNome());
                nomeArtista.setText(e.getNomeArtista());
                localEvento.setText(e.getLocal());

                SimpleDateFormat desireFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String dateFormat = desireFormat.format(e.getDate());
                dataEvento.setText(dateFormat);
            }
        }
    }
}
