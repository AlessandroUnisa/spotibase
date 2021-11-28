package model.Artista;

import org.json.simple.JSONObject;

public class ArtistaMapperJSON {
    public JSONObject map(Artista artista){
        JSONObject obj = new JSONObject();
        obj.put("codFiscale",artista.getCodFiscale());
        obj.put("nome",artista.getNome());
        obj.put("cognome",artista.getCognome());
        obj.put("nomeDArte",artista.getNomeDArte());
        return obj;
    }
}
