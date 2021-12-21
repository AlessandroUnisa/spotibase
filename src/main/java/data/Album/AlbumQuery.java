package data.Album;

public abstract class AlbumQuery {
    public static  String getQueryDoRetriveAlbumRandom(){
        return "SELECT * FROM album ALB ORDER BY RAND() LIMIT 0,7;";
    }
    public static String getQueryDoRetrieveAlbumWithSongs(){
        return "SELECT * FROM album ALB, canzone CAN WHERE ALB.codice=CAN.codiceAlbum AND ALB.codice=?;";
    }

    public static String getQueryFetchAlbumWithSongsByArtista(){
        return "SELECT ALB.*, CAN.* FROM "+
                "((produzione PRO JOIN album ALB ON PRO.codiceAlbum=ALB.codice) " +
                "JOIN canzone CAN ON CAN.codiceAlbum=ALB.codice) " +
                "WHERE PRO.codFiscale=? ORDER BY ALB.codice;";
    }

    public static String getQueryAlbumPopular(){
        return "SELECT ALB.*,COUNT(*)" +
                " FROM album ALB, interesse INTE " +
                " WHERE INTE.codiceAlbum=ALB.codice GROUP BY ALB.codice;";
    }

    public static String getQueryAlbumByName(){
        return "SELECT ALB.* FROM album ALB WHERE ALB.nome LIKE ?";
    }

    public static String getQueryAlbumSave(){
        return "INSERT INTO album (codice,nome, anno, descrizione, numCanzoni) VALUES(?,?,?,?,?);";
    }

    public static String getQueryAlbumUpdate(){
        return "UPDATE album SET nome=?, anno=?, descrizione=?, numCanzoni=? WHERE codice=?";
    }

    public static String getQueryAlbumByGenere(){
        return "SELECT DISTINCT ALB.* FROM album ALB, produzione PRO, artista ART, genere GE " +
                "WHERE ART.codFiscale=PRO.codFiscale AND PRO.codiceAlbum=ALB.codice AND GE.codFiscale=ART.codFiscale AND GE.nome LIKE ?;";
    }

    public static String getQueryTotaleGuadagni(){
        return "SELECT SUM(prezzo) totale FROM ua, album ALB WHERE ua.codAlbum=ALB.codice;";
    }

    public static String getQueryDoRetrieveAlbumTopBuy(){
        return "SELECT ALB.*, COUNT(*) cont FROM ua, album ALB WHERE ua.codAlbum=ALB.codice GROUP BY ALB.codice ORDER BY cont; ";
    }



    public static String getQueryDoRetrieveAlbumByCodice(){
        return "SELECT ALB.* FROM album ALB WHERE ALB.codice=?;";
    }

    public static String getQueryDoRetrieveAlbumAquistati(){
        return "SELECT ALB.* FROM ua, album ALB WHERE  " +
                " ua.codAlbum=ALB.codice AND ua.username=?;";
    }

    public static String getQueryDoRetrieveCodiciAlbumAquistati(){
        return "SELECT ALB.codice FROM ua, album ALB WHERE  " +
                " ua.codAlbum=ALB.codice AND ua.username=?;";
    }

    public static String getQueryDoRetriveAlbumUltimeUscite() {
        return "Select * FROM album ALB order by ALB.anno desc LIMIT 0,7;";
    }

    public static String getQueryDoRetrieveCanzoniByCodici(int size){
        String query = "SELECT ALB.*, ART.* FROM produzione PRO, album ALB, artista ART WHERE PRO.codiceAlbum=ALB.codice" +
                " AND PRO.codFiscale=ART.codFiscale AND ALB.codice IN (";
        for(int i=0; i<size-1; i++)
            query += "?,";
        return query+"?);";
    }

    public static String getQueryDoInsertCanzoneAcquistata(){
        return "INSERT INTO ua VALUES(?,?)";
    }

    public static String getQueryDoRetrieveaCodiciAlbumPreferiti(){
        return "SELECT * FROM interesse WHERE usernameUtente=?";
    }

    public static String getQueryRemovePreferenza(){
        return "DELETE FROM interesse WHERE usernameUtente=? AND codiceAlbum=?";
    }

    public static String getQueryInsertPreferenza(){
        return "INSERT INTO interesse VALUES(?,?)";
    }
}
