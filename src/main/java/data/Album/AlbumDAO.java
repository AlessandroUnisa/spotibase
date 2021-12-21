package data.Album;

import data.utils.SingletonJDBC;
import data.Artista.ArtistaMapper;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class AlbumDAO {
    //metodi NON documentati per IS----------------------------------------------------------------------------------------------

    /**Ritorna gli album ultimi usciti*/
    public List<Album> doRetrieveAlbumUltimeUscite() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetriveAlbumUltimeUscite());
        ResultSet rs = st.executeQuery();
        ArrayList<Album> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new AlbumMapper().map(rs));
        return lista;
    }

    /**Ritorna la lista di album random*/
    public List<Album> doRetrieveAlbumRandom() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetriveAlbumRandom());
        ResultSet rs = st.executeQuery();
        ArrayList<Album> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new AlbumMapper().map(rs));

        return lista;
    }

    /**Ritorna la lista di album acquistati*/
    public List<Album> doRetrieveAlbumAcquistati(String username) throws SQLException {
        PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetrieveAlbumAquistati());
        statement.setString(1,username);
        ArrayList<Album> list = new ArrayList<>();
        AlbumMapper mapper = new AlbumMapper();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next())
            list.add(mapper.map(resultSet));
        return list;
    }

    /**Ritorna la lista dei codici di album acquistati*/
    public List<String> doRetrieveCodiciAlbumAcquistati(String username) throws SQLException {
        PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetrieveCodiciAlbumAquistati());
        statement.setString(1,username);
        ArrayList<String> list = new ArrayList<>();
        AlbumMapper mapper = new AlbumMapper();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next())
            list.add(resultSet.getString("ALB.codice"));
        return list;
    }


    /**Ritorna l album con il codice*/
    public Album doRetrieveAlbumByCodice(String codice) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetrieveAlbumByCodice());
        preparedStatement.setString(1,codice);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return new AlbumMapper().map(resultSet);
        else return null;
    }

    /**Ritorna l album con la lista di canzoni*/
    public Album doRetrieveAlbumWithSongs(String codice) throws SQLException {
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetrieveAlbumWithSongs());
            preparedStatement.setString(1,codice);
            ResultSet resultSet = preparedStatement.executeQuery();
            CanzoneMapper canzoneMapper = new CanzoneMapper();
            LinkedHashMap<String,Canzone> mappaCanzoni = new LinkedHashMap<>();
            Album album=null;
            if(resultSet.next()){
                album = new AlbumMapper().map(resultSet);
                album.setCanzoni(new ArrayList<>());
                Canzone canzone = canzoneMapper.map(resultSet);
                mappaCanzoni.put(canzone.getCodice(),canzone);
                while (resultSet.next()){
                    canzone = canzoneMapper.map(resultSet);
                    if(!mappaCanzoni.containsKey(canzone.getCodice()))
                        mappaCanzoni.put(canzone.getCodice(),canzone);
                }
            }
            album.setCanzoni(new ArrayList<>(mappaCanzoni.values()));
            return album;
    }

    /**Ritorna la lista di album dell atista*/
    public List<Album> fetchAlbumWithSongsByArtista(String cfArtista) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryFetchAlbumWithSongsByArtista());
        st.setString(1,cfArtista);
        ResultSet rs = st.executeQuery();

        Map<String, Album> albumMap = new LinkedHashMap<String, Album>();
        AlbumMapper albumMapper = new AlbumMapper();

        while (rs.next()){
            String idAlbum = rs.getString("ALB.codice");
            if(!albumMap.containsKey(idAlbum)){
                Album album = albumMapper.map(rs);
                album.setCanzoni(new ArrayList<>());
                albumMap.put(album.getCodice(), album);
            }
            albumMap.get(idAlbum).getCanzoni().add(new CanzoneMapper().map(rs));
        }

        return new ArrayList<>(albumMap.values());
     }

    /** Ritorna gli album popolari */
    public List<Album> doRetrieveAlbumPopular() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryAlbumPopular());
        ResultSet rs = st.executeQuery();
        ArrayList<Album> lista = new ArrayList<>();

        while(rs.next())
            lista.add(new AlbumMapper().map(rs));

        return lista;
    }


    /** Ritorna la lista di album che hanno quel nome */
    public List<Album> doRetrieveAlbumByName(String name) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryAlbumByName());
        String nameLike = "%"+name+"%";
        st.setString(1,nameLike);
        System.out.println(st.toString());
        ResultSet rs = st.executeQuery();
        ArrayList<Album> lista = new ArrayList<>();

        while (rs.next())
            lista.add(new AlbumMapper().map(rs));

        return lista;
    }

    /** Salva nel DB l'album*/
    public void doSave(Album album) throws SQLException {
        PreparedStatement ps = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryAlbumSave());
        ps.setString(1, album.getCodice());
        ps.setString(2, album.getNome());
        ps.setDouble(3, album.getAnno());
        ps.setString(4,album.getDescrizione());
        ps.setInt(5,album.getNumCanzoni());
        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("INSERT error.");
        }
    }

    /** Elimina l'album dal DB*/
    public void doDelete(String codeAlbum) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("DELETE FROM album WHERE codice=?");
        st.setString(1,codeAlbum);
        if(st.executeUpdate()!=1)
            throw new RuntimeException("delete error");
    }

    /** Modifica l'album */
    public boolean doUpdate(Album album) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryAlbumUpdate());
        st.setString(1,album.getNome());
        st.setInt(2,album.getAnno());
        st.setString(3,album.getDescrizione());
        st.setInt(4,album.getNumCanzoni());
        st.setString(5,album.getCodice());
        return st.executeUpdate()==1;
    }

    /** Ritorna tutti gli album*/
    public List<Album> doRetrieveAllAlbum() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM album ALB;");
        ResultSet rs = st.executeQuery();
        ArrayList<Album> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new AlbumMapper().map(rs));

        return lista;

    }
    public List<Album> doRetrieveAllAlbum(int offset, int numElements) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM album ALB ORDER BY ALB.nome LIMIT ?,?;");
        st.setInt(1,offset);
        st.setInt(2,numElements);
        ResultSet rs = st.executeQuery();
        ArrayList<Album> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new AlbumMapper().map(rs));

        return lista;

    }

    /** Ritorna gli album del genere passato*/
    public List<Album> doRetrieveAlbumByGenere(String genere) throws SQLException {
        PreparedStatement ps = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryAlbumByGenere());
        ps.setString(1,'%'+genere+'%');
        System.out.println(ps);
        List<Album> listaAlbum = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next())
            listaAlbum.add(new AlbumMapper().map(rs));

        return listaAlbum;
    }

    /**Ritorna il numero di album nel DB*/
    public int doRetrieveNumAlbum() throws SQLException {
        PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement("SELECT COUNT(*) NUM FROM album;");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
            return resultSet.getInt("NUM");
        else return 0;
    }

    /**Ritorna la somma del prezzo degli album acquistati*/
    public double doRetrieveTotaleGuadagno() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryTotaleGuadagni());
        ResultSet rs = st.executeQuery();
        if(!rs.next())
            return 0;
        else return rs.getDouble("totale");
    }

    /**Ritorna gli album piu acquistati*/
    public List<Album> doRetrieveAlbumTopBuy() throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetrieveAlbumTopBuy());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Album> lista = new ArrayList<>();
        while (resultSet.next())
            lista.add(new AlbumMapper().map(resultSet));
        return lista;
    }

    /**Ritorna la lista di album con la lista di artisti*/
    public List<Album> doRetrieveAlbumsByCodiciWithArtisti(List<String> codici) throws SQLException {
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetrieveCanzoniByCodici(codici.size()));
            for(int i=0; i<codici.size(); i++)
                preparedStatement.setString(1+i,codici.get(i));

            LinkedHashMap<String, Album> map = new LinkedHashMap<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Album album = new AlbumMapper().map(resultSet);
                if(!map.containsKey(album.getCodice())){
                    album.setArtisti(new ArrayList<>());
                    map.put(album.getCodice(), album);
                    album.getArtisti().add(new ArtistaMapper().map(resultSet));
                }else map.get(album.getCodice()).getArtisti().add(new ArtistaMapper().map(resultSet));
            }

            return new ArrayList<>(map.values());
    }

    /**Inserisce un acquisto album*/
    public boolean doInsertAlbumAcquistato(String username, String codice) throws SQLException {
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoInsertCanzoneAcquistata());
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,codice);
            return preparedStatement.executeUpdate()==1;
    }

    /**Ritorna gli album preferiti dall utente*/
    public List<String> doRetrieveaCodiciAlbumPreferiti(String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryDoRetrieveaCodiciAlbumPreferiti());
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> list = new ArrayList<>();
        while (resultSet.next())
            list.add(resultSet.getString("codiceAlbum"));
        return list;
    }

    /**Inserisce una preferenza per l album*/
    public boolean doInsertPreferenza(String codice, String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryInsertPreferenza());
        preparedStatement.setString(1,codice);
        preparedStatement.setString(2,username);
        return preparedStatement.executeUpdate()!=0;
    }

    /**Rimuove la preferenza per il DB*/
    public boolean doRemovePreferenza(String codice, String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AlbumQuery.getQueryRemovePreferenza());
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,codice);

        return preparedStatement.executeUpdate()!=0;
    }

}
