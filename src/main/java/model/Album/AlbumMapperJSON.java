package model.Album;

import org.json.simple.JSONObject;

public class AlbumMapperJSON {
    public JSONObject map(Album album) {
        JSONObject obj = new JSONObject();
        obj.put("codice", album.getCodice());
        obj.put("nome", album.getNome());
        obj.put("anno", album.getAnno());
        obj.put("descrizione", album.getDescrizione());
        obj.put("numCanzoni", album.getNumCanzoni());
        return obj;
    }
}
