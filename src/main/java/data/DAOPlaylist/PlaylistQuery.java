package data.DAOPlaylist;
/**Questa classe contiene le query rigurdante le playlist
 *
 */
public abstract class PlaylistQuery {
    public static String getQueryPlaylistSave(){
        return "INSERT INTO playlist (titolo,username,note) VALUES(?,?,?);";
    }

   /* public static String getQueryPlaylistSave(){
        return "INSERT INTO playlist(titolo,username,note, durata,dataCreazione) VALUES(?,?,?,?,?);";
    }*/

    public static String getQueryPlaylistUpdate(){
        return "UPDATE playlist SET titolo=?, note=?, durata=?, dataCreazione=? WHERE titolo=? AND username=?;";
    }

    public static String getQueryDoRetrievePlaylistWithSongs(){
        return "SELECT PLA.*, CAN.* FROM playlist PLA LEFT JOIN raccoglie RAC ON PLA.titolo = RAC.titolo AND PLA.username=RAC.usernameUtente " +
                " LEFT JOIN canzone CAN ON CAN.codice=RAC.codiceCanzone WHERE PLA.username=? AND PLA.titolo=?;";
    }

    public static String getQueryDoRemoveSong(){
        return "DELETE FROM raccoglie WHERE titolo=? AND usernameUtente=? AND codiceCanzone=?;";
    }

    public static String getQueryDoRetrievePlaylistByUtente(){
        return "SELECT * FROM playlist PLA WHERE PLA.username = ?";
    }

    public static String getQueryDoInsertSong(){
        return "INSERT INTO raccoglie VALUES (?,?,?);";
    }

    public static String getQueryIsPresent(){
        return "SELECT * FROM raccoglie WHERE codiceCanzone=? AND titolo=? AND usernameUtente=?;";
    }

    public static String getQueryIsPresentPlaylist(){
        return "SELECT * FROM playlist WHERE titolo=? AND username=?;";
    }

    public static String getQueryDoInsertPlaylist(){
        return "INSERT INTO playlist VALUES(?,?,?,?,?);";
    }

    public static String getQuerydoRetrieveNumPlaylistOfUtente(){
        return "SELECT COUNT(*) num FROM playlist WHERE username=?";
    }
}