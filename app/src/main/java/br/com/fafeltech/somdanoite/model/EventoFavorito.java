package br.com.fafeltech.somdanoite.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("EventoFavorito")
public class EventoFavorito extends ParseObject {

    public EventoFavorito() { super(); }

    public void add (
            String nomeEvento,
            String eventoId,
            String userId
    ) {
        put("nome_evento", nomeEvento);
        put("eventoId", eventoId);
        put("userId", userId);
    }

    public String getNomeEvento () { return getString("nome_evento");}

    public String getEventoId() { return getString("eventoId");}

    public String getUserId() {return getString("userId");}

    public void setNomeEvento (String nomeEvento) {put("nome_evento", nomeEvento);}

    public void setEventoId (String eventoId) {put("eventoId", eventoId);}

    public void setUserId (String userId) {put("userId", userId);}

}
