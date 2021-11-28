package model.Artista;

public abstract class ArtistaQuery {
    public static String getQueryArtistaRandom(){
        return"SELECT * FROM artista ART GROUP BY ART.nomeDArte ORDER BY RAND() LIMIT 0,7;";
    }

    public static String getQueryArtistPopular(){
        return "SELECT ART.*,COUNT(*) FROM artista ART, seguire SEG " +
                "WHERE SEG.codFiscale=ART.codFiscale GROUP BY ART.nomeDArte LIMIT 0,10;";
    }

    public static String getQueryArtistaUpdate(){
        return "UPDATE artista SET nome=?, cognome=?, nomeDArte=? WHERE codFiscale=?";
    }

    public static String getQueryArtistaByGenere(){
        return "SELECT ART.* FROM artista ART, genere GEN WHERE GEN.codFiscale=ART.codFiscale AND GEN.nome=?";
    }

    public static String getQueryArtistiWithGenereBySong(){
        return "SELECT ART.*, GEN.* FROM scrivere SCR, artista ART, genere GEN WHERE SCR.codFiscale=ART.codFiscale " +
                "AND GEN.codFiscale=ART.codFiscale AND SCR.codiceCanzone=?;";
    }

    public static String getQueryArtistiWithGenereBySongGroupNomeDArte(){
        return "SELECT ART.*, GEN.* FROM scrivere SCR, artista ART, genere GEN WHERE SCR.codFiscale=ART.codFiscale " +
                "AND GEN.codFiscale=ART.codFiscale AND SCR.codiceCanzone=? GROUP BY ART.nomeDArte;";
    }

    public static String getQueryArtistaByAlbum() {
        return "SELECT  ART.* FROM artista ART, produzione PRO, album ALB WHERE ART.codFiscale=PRO.codFiscale " +
                "AND PRO.codiceAlbum=ALB.codice AND PRO.codiceAlbum=? GROUP BY PRO.codiceAlbum;";
    }

    public static String getQuerydoRetrieveArtistaByNomeArte(){
        return "SELECT ART.* FROM artista ART WHERE ART.nomeDArte=?";
    }

    public static String getQueryDoRetrieveArtistiBySong(){
        return "SELECT ART.* FROM artista ART, scrivere SCR WHERE SCR.codFiscale=ART.codFiscale AND SCR.codiceCanzone=?";
    }

    public static String getQueryDoRetrieveArtistiBySongGroupNomeDArte(){
        return "SELECT ART.* FROM artista ART, scrivere SCR WHERE SCR.codFiscale=ART.codFiscale AND SCR.codiceCanzone=? GROUP BY ART.nomeDArte";
    }


    public static String getQueryDoInsertPreferenza(int num){
        String query = "INSERT INTO seguire VALUES ";
        for(int i=0; i<num-1; i++)
            query+="(?,?), ";
        query+="(?,?);";
        return query;
    }

    public static String getQueryDoRemovePreferenza(int num){
        String query = "DELETE FROM seguire WHERE usernameUtente=? AND codFiscale IN (";
        for(int i=0; i<num-1; i++)
            query+="?,";
        query+="?);";
        return query;
    }

    public static String getQueryDoRetrieveArtistiPreferiti(){
        return "SELECT ART.* FROM seguire SEG, artista ART " +
                "WHERE ART.codFiscale=SEG.codFiscale AND SEG.usernameUtente=?";
    }

    public static String getQueryDoRetrieveNomiArteArtistiPreferiti(){
        return "SELECT ART.nomeDArte FROM seguire SEG, artista ART " +
                "WHERE SEG.codFiscale=ART.codFiscale AND SEG.usernameUtente=?;";
    }


}
