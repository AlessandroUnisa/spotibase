package data.DAOPlaylist;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistMapper {
    public Playlist map(ResultSet rs) throws SQLException {
        Playlist playlist = new Playlist();
        playlist.setTitolo(rs.getString("PLA.titolo"));
        playlist.setUsername(rs.getString("PLA.username"));
        playlist.setNote(rs.getString("PLA.note"));
        //playlist.setDurata(rs.getDouble("PLA.durata"));
        //playlist.setDataCreazione(rs.getDate("PLA.dataCreazione").toLocalDate());
        return playlist;
    }
}
