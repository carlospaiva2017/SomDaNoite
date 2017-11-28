package br.com.fafeltech.somdanoite.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Evento")
public class Evento extends ParseObject {

    public Evento() {
        super();
    }

    public void add(
                    String nome,
                    String nomeUpperCase,
                    String nomeLowerCase,
                    String nomeLocal,
                    String nomeLocalUpperCase,
                    String nomeLocalLowerCase,
                    ParseGeoPoint geoPoint,
                    String localId,
                    Date date,
                    String nomeArtista,
                    String nomeArtistaUpperCase,
                    String nomeArtistaLowerCase,
                    String userId,
                    String content,
                    String tags
                        )
    {
        put("nome", nome);
        put("nome_upper_case", nomeUpperCase);
        put("nome_lower_case", nomeLowerCase);
        put("nome_local", nomeLocal);
        put("nome_local_upper_case", nomeLocalUpperCase);
        put("nome_local_lower_case", nomeLocalLowerCase);
        put("local_mapa", geoPoint);
        put("localId", localId);
        put("date", date);
        put("nome_artista", nomeArtista);
        put("nome_artista_upper_case", nomeArtistaUpperCase);
        put("nome_artista_lower_case", nomeArtistaLowerCase);
        put("userId", userId);
        put("content", content);
        put("tags", tags);
        saveInBackground();
    }

    public String getNome() {
        return getString("nome");
    }

    public void setNome(String nome) {
        put("nome", nome);
    }

    public String getLocal() {
        return getString("nome_local");
    }

    public void setLocal(String nomeLocal) {
        put("nome_local", nomeLocal);
    }

    public Date getDate() { return getDate("date"); }

    public void setDate (Date date) { put("date", date); }

    public void setNomeArtista (String nomeArtista) { put("nome_artista", nomeArtista); }

    public String getNomeArtista() { return getString("nome_artista"); }

    public String getUserId() { return getString("userId"); }

    public boolean selected;

    public ParseGeoPoint getLocalMapa() { return getParseGeoPoint("local_mapa"); }

    public void setLocalMapa(ParseGeoPoint geoPoint) { put("local_mapa", geoPoint); }

    public String getContent() { return getString("content"); }

    public void setContent(String content) { put("content", content); }

    public String getLocalId() { return getString("localId"); }

    public void setLocalId ( String localId) { put("localId", localId); }
}
