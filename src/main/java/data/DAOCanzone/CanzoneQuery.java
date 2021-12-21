package data.DAOCanzone;

public abstract class CanzoneQuery {
    public static String getQueryDoRetrieveSongsByPlaylist(){
        return "SELECT CAN.* FROM playlist PLA,raccoglie RAC,canzone CAN " +
                "WHERE PLA.titolo = RAC.titolo AND PLA.username = RAC.usernameUtente AND RAC.codiceCanzone = CAN.codice AND " +
                "PLA.username LIKE ? AND PLA.titolo LIKE ?";
    }

    public static String getQueryDoRetrieveSongsByAlbum(){
        return"SELECT CAN.* FROM canzone CAN WHERE CAN.codiceAlbum = ?;";
    }

    public static String getQueryDoRetrieveSinglesByArtista(){
        return "SELECT CAN.* FROM canzone CAN, scrivere SCR, artista ART " +
                "WHERE CAN.codice = SCR.codiceCanzone AND SCR.codFiscale= ART.codFiscale AND " +
                "CAN.codiceAlbum is null AND ART.nomeDArte LIKE ? GROUP BY CAN.codice;";
    }

    public static String getQueryDoRetrieveSongsByArtista(){
        return "SELECT CAN.* FROM canzone CAN, scrivere SCR, artista ART " +
                "WHERE CAN.codice = SCR.codiceCanzone AND SCR.codFiscale= ART.codFiscale AND " +
                "ART.nomeDArte LIKE ? GROUP BY CAN.codice;";
    }

    public static String getQueryDoRetrieveCanzonePopular(){
        return "SELECT CAN.*,COUNT(*) FROM preferenza PRE, canzone CAN WHERE PRE.codiceCanzone = CAN.codice GROUP BY codiceCanzone LIMIT 0,10;";
    }

    public static String getQueryDoRetrievePopularSongsWithArtista(){

        return "SELECT CAN.*,ART.* FROM " +
                "(SELECT CAN.codice FROM preferenza PRE, canzone CAN WHERE CAN.codice=PRE.codiceCanzone GROUP BY PRE.codiceCanzone ORDER BY COUNT(*) DESC LIMIT 0,10) AS CODCAN " +
                "INNER JOIN canzone CAN ON CAN.codice=CODCAN.codice " +
                "INNER JOIN scrivere SCR ON SCR.codiceCanzone=CAN.codice " +
                "INNER JOIN artista ART ON ART.codFiscale=SCR.codFiscale ";
    }

    public static String getQueryCanzoneUpdate(){
        return "UPDATE canzone SET titolo=?, anno=?, durata=?, prezzo=? WHERE codice=?";
    }

    public static String getQueryCanzoneByGenere(){
        return "SELECT DISTINCT CAN.* FROM canzone CAN, scrivere SCR, artista ART, genere GEN " +
                "WHERE ART.codFiscale=SCR.codFiscale AND SCR.codiceCanzone=CAN.codice AND GEN.codFiscale=ART.codFiscale AND GEN.nome LIKE ?;";
    }

    public static String getQueryTotaleGuadagni(){
        return "SELECT SUM(prezzo) totale FROM uc, canzone CAN WHERE uc.codCanzone=CAN.codice;";
    }

    public static String getQueryDoRetrieveCanzoniTopBuy(){
        return "SELECT CAN.*, COUNT(*) cont FROM uc, canzone CAN WHERE uc.codCanzone=CAN.codice GROUP BY CAN.codice ORDER BY cont;";
    }

    public static String getQueryCanzoneByGenere(String filterSQL){
        return "SELECT DISTINCT CAN.* FROM canzone CAN, scrivere SCR, artista ART, genere GEN " +
                "WHERE ART.codFiscale=SCR.codFiscale AND SCR.codiceCanzone=CAN.codice AND GEN.codFiscale=ART.codFiscale AND GEN.nome=? "+filterSQL+" LIMIT ?,?;";
    }

    public static String getQueryDoRetrieveAlbumByCodice(){
        return "SELECT CAN.* FROM canzone CAN WHERE CAN.codice=?";
    }

    public static String getQueryDoRetrieveCodiciCanzoniPreferite(){
        return "SELECT PRE.codiceCanzone FROM preferenza PRE WHERE PRE.usernameUtente=?;";
    }

    public static String getQueryDoInsertPreferenza(){
        return "INSERT INTO preferenza VALUES(?,?);";
    }

    public static String getQueryDoRetrieveCanzoniAcquistate(){
        return "SELECT * FROM (SELECT CAN.* FROM utente UTE, uc, canzone CAN " +
                "WHERE UTE.username=uc.username AND uc.codCanzone=CAN.codice AND UTE.username=? " +
                "UNION " +
                "SELECT CAN.* FROM ua, album ALB, canzone CAN" +
                " WHERE ua.codAlbum=ALB.codice AND CAN.codiceAlbum=ALB.codice AND ua.username=?) AS CAN;";
    }

    public static String getQueryDoRetriveCanzoneRandom() {
        return "SELECT * FROM canzone CAN ORDER BY RAND() LIMIT 0,7; ";
    }

    public static String getQueryDoRetriveCanzoniUltimeUscite() {
        return "Select * FROM canzone CAN order by CAN.anno desc LIMIT 0,7;";
    }

    public static String getQueryDoRetrieveCanzoneWithArtisti(){
        return "SELECT CAN.*, ART.* FROM canzone CAN, scrivere SCR, artista ART " +
                "WHERE CAN.codice=SCR.codiceCanzone AND ART.codFiscale=SCR.codFiscale AND CAN.codice=?";
    }

    public static String getQueryDoDeletePreferenza(){
        return "DELETE FROM preferenza WHERE codiceCanzone=? AND usernameUtente=?;";
    }

    public static String getQueryDoRetrieveCanzoniByCodici(int size){
        String query = "SELECT CAN.*, ART.* FROM scrivere SCR, artista ART, canzone CAN WHERE" +
                " ART.codFiscale=SCR.codFiscale AND SCR.codiceCanzone=CAN.codice AND  CAN.codice IN (";
        for(int i=0; i<size-1; i++)
            query += "?,";
        return query+"?);";
    }

    public static String getQueryDoInsertCanzoneAcquistata(){
        return "INSERT INTO uc VALUES(?,?);";
    }

    public static String getQueryDoRetrieveCodiciCanzoniAcquistate(){
        return "SELECT uc.codCanzone FROM uc WHERE username=? " +
                "UNION " +
                "SELECT CAN.codice FROM ua, album ALB, canzone CAN " +
                "WHERE ua.username=? AND ua.codAlbum=ALB.codice AND CAN.codiceAlbum=ALB.codice";
    }
}

