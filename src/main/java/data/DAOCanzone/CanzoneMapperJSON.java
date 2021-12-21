package data.DAOCanzone;

import org.json.simple.JSONObject;

public class CanzoneMapperJSON {
    public JSONObject map(Canzone canzone){
        JSONObject obj = new JSONObject();
        obj.put("codice",canzone.getCodice());
        obj.put("titolo",canzone.getTitolo());
        obj.put("durata",canzone.getDurata());
        obj.put("anno",canzone.getAnno());
        obj.put("prezzo",canzone.getPrezzo());
        return obj;
    }
}
