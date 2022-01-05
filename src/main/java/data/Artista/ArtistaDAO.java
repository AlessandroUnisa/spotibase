package data.Artista;

import data.Genere.GenereMapper;
import data.utils.SingletonJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ArtistaDAO {

    Connection connection;
    public  ArtistaDAO(){
        this.connection = SingletonJDBC.getConnection();
    }
    public ArtistaDAO(Connection connection) {
        this.connection = connection;
    }
    //metodi NON documentati per IS----------------------------------------------------------------------------------------------

    /**Ritorna l artista con il nome*/
    public List<Artista> doRetrieveArtistaByNomeArte(String nome) throws SQLException {//ritorna una list perche ci sono i gruppi
        PreparedStatement statement = connection.prepareStatement(ArtistaQuery.getQuerydoRetrieveArtistaByNomeArte());
        statement.setString(1,nome);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Artista> list = new ArrayList<>();
        ArtistaMapper artistaMapper = new ArtistaMapper();
        while (resultSet.next())
            list.add(artistaMapper.map(resultSet));
        return  list;
    }


    /**Ritorna una lista di artisti random*/
    public List<Artista> doRetrieveArtistRandom() throws SQLException {
        PreparedStatement st = connection.prepareStatement(ArtistaQuery.getQueryArtistaRandom());
        ResultSet rs = st.executeQuery();
        ArrayList<Artista> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new ArtistaMapper().map(rs));

        return lista;
    }

    /** Ritorna gli artisti popolari */
    public List<Artista> doRetrieveArtistPopular() throws SQLException {
        PreparedStatement st = connection.prepareStatement(ArtistaQuery.getQueryArtistPopular());
        ResultSet rs = st.executeQuery();
        ArrayList<Artista> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new ArtistaMapper().map(rs));

        return lista;
    }

    /** Ritorna la lista di artisti che hanno quel nome */
    public List<Artista> doRetrieveByName(String name) throws SQLException {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM artista ART WHERE ART.nomeDArte LIKE ? GROUP BY nomeDArte;");
        String nameLike = "%"+name+"%";
        st.setString(1,nameLike);
        ResultSet rs = st.executeQuery();
        ArrayList<Artista> lista = new ArrayList<>();
        while (rs.next())
            lista.add(new ArtistaMapper().map(rs));

        return lista;
    }

    /** Salva nel DB l'artista*/
    public void doSave(Artista artista) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT INTO artista VALUES (?,?,?,?,?)");
        st.setString(1,artista.getCodFiscale());
        st.setString(2,artista.getNome());
        st.setString(3,artista.getCognome());
        st.setString(4,artista.getNomeDArte());
        st.setString(5,artista.getPathImg());

        if(st.executeUpdate()!=1)
            throw new RuntimeException("insert error");
    }

    /** Elimina l'artista dal DB*/
    public void doDelete(String codFiscale) throws SQLException {
        PreparedStatement st = connection.prepareStatement("DELETE FROM artista WHERE codFiscale=?");
        st.setString(1,codFiscale);
        if(st.executeUpdate()!=1)
            throw new RuntimeException("delete error");
    }

    /** Modifica l'artista */
    public boolean doUpdate(Artista artista) throws SQLException {
        PreparedStatement st = connection.prepareStatement(ArtistaQuery.getQueryArtistaUpdate());
        st.setString(1,artista.getNome());
        st.setString(2,artista.getCognome());
        st.setString(3,artista.getNomeDArte());
        st.setString(4,artista.getCodFiscale());
        return st.executeUpdate()==1;
    }

    /** Ritorna tutti gli artisti*/
    public List<Artista> doRetrieveAllArtist() throws SQLException {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM artista ART;");
        ResultSet rs = st.executeQuery();
        ArrayList<Artista> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new ArtistaMapper().map(rs));

        return lista;
    }

    public List<Artista> doRetrieveAllArtist(int offset, int numElements) throws SQLException {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM artista ART ORDER BY ART.nomeDArte LIMIT ?,?;");
        st.setInt(1,offset);
        st.setInt(2,numElements);
        ResultSet rs = st.executeQuery();
        ArrayList<Artista> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new ArtistaMapper().map(rs));

        return lista;
    }

    /**Ritorna la lista degli artisti che hanno composto l'album*/
    public List<Artista> doRetriveArtistaByAlbum(String cod) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(ArtistaQuery.getQueryArtistaByAlbum());
        ps.setString(1,cod);
        ResultSet rs = ps.executeQuery();
        ArrayList<Artista> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new ArtistaMapper().map(rs));

        return lista;
    }

    /** Ritorna gli artisti del genere passato*/
    public List<Artista> doRetrieveArtistaByGenere(String genere) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(ArtistaQuery.getQueryArtistaByGenere());
        ps.setString(1,genere);
        List<Artista> artisti = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next())
            artisti.add(new ArtistaMapper().map(rs));

        return artisti;
    }

    /**Ritorna la lista di artisti che hanno scitto la canzone, con la lista generi*/
    public List<Artista> doRetrieveArtistiWithGenereBySong(String codice) throws SQLException {

            PreparedStatement preparedStatement = connection.prepareStatement(ArtistaQuery.getQueryArtistiWithGenereBySong());
            preparedStatement.setString(1,codice);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArtistaMapper mapperArtista = new ArtistaMapper();
            GenereMapper mapperGenere = new GenereMapper();
            LinkedHashMap<String,Artista> mapArtista = new LinkedHashMap<>();

            while (resultSet.next()){
                Artista artista = mapperArtista.map(resultSet);
                if(!mapArtista.containsKey(artista.getCodFiscale())){
                    mapArtista.put(artista.getCodFiscale(),artista);
                    artista.setGeneri(new ArrayList<>());
                    artista.getGeneri().add(mapperGenere.map(resultSet));
                }else{
                    mapArtista.get(artista.getCodFiscale()).getGeneri().add(mapperGenere.map(resultSet));
                }
            }
            return new ArrayList<>(mapArtista.values());
    }

    /**Ritorna la lista di artisti che hanno scitto la canzone, con la lista generi*/
    public List<Artista> doRetrieveArtistiWithGenereBySongGroupNomeArte(String codice) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ArtistaQuery.getQueryArtistiWithGenereBySongGroupNomeDArte());
        preparedStatement.setString(1,codice);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArtistaMapper mapperArtista = new ArtistaMapper();
        GenereMapper mapperGenere = new GenereMapper();
        LinkedHashMap<String,Artista> mapArtista = new LinkedHashMap<>();

        while (resultSet.next()){
            Artista artista = mapperArtista.map(resultSet);
            if(!mapArtista.containsKey(artista.getCodFiscale())){
                mapArtista.put(artista.getCodFiscale(),artista);
                artista.setGeneri(new ArrayList<>());
                artista.getGeneri().add(mapperGenere.map(resultSet));
            }else{
                mapArtista.get(artista.getCodFiscale()).getGeneri().add(mapperGenere.map(resultSet));
            }
        }
        return new ArrayList<>(mapArtista.values());

    }


    /**Ritorna il numero di artisti nel DB*/
    public int doRetrieveNumArtisti() throws SQLException {
       PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) NUM FROM artista;");
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next())
            return resultSet.getInt("NUM");
        else return 0;
    }

    /**Ritorna la lista di artisti che hanno scitto la canzone*/
    public List<Artista> doRetrieveArtistiBySong(String codice) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ArtistaQuery.getQueryDoRetrieveArtistiBySong());
        preparedStatement.setString(1,codice);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Artista> list = new ArrayList<>();
        ArtistaMapper mapper = new ArtistaMapper();
        while (resultSet.next())
            list.add(mapper.map(resultSet));
        return list;
    }

    /**Ritorna la lista di artisti che hanno scitto la canzone*/
    public List<Artista> doRetrieveArtistiBySongGroupNomeDArte(String codice) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ArtistaQuery.getQueryDoRetrieveArtistiBySongGroupNomeDArte());
        preparedStatement.setString(1,codice);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Artista> list = new ArrayList<>();
        ArtistaMapper mapper = new ArtistaMapper();
        while (resultSet.next())
            list.add(mapper.map(resultSet));
        return list;
    }

    public boolean doInsertPreferenza(String username, List<String> codFiscali) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ArtistaQuery.getQueryDoInsertPreferenza(codFiscali.size()));

        int j=0;
        for(int i=0; i<codFiscali.size(); i++){
            System.out.println((j+1)+"   "+(j+2)+"\n");
            preparedStatement.setString(j+1,codFiscali.get(i));
            preparedStatement.setString(j+2,username);
            j+=2;
        }
        return preparedStatement.executeUpdate()==codFiscali.size();
    }

    public boolean doRemovePreferenza(String username, List<String> codFiscali) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ArtistaQuery.getQueryDoRemovePreferenza(codFiscali.size()));
        preparedStatement.setString(1,username);
        for(int i=2; i<codFiscali.size()+2; i++)
            preparedStatement.setString(i,codFiscali.get(i-2));
        return preparedStatement.executeUpdate()==codFiscali.size();
    }

    public List<Artista> doRetrieveArtistiPreferiti(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ArtistaQuery.getQueryDoRetrieveArtistiPreferiti());
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Artista> list = new ArrayList<>();
        ArtistaMapper mapper = new ArtistaMapper();
        while (resultSet.next())
            list.add(mapper.map(resultSet));
        return list;
    }

    public List<String> doRetrieveNomiArteArtistiPreferiti(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ArtistaQuery.getQueryDoRetrieveNomiArteArtistiPreferiti());
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> list = new ArrayList<>();
        while (resultSet.next())
            list.add(resultSet.getString("ART.nomeDArte"));
        return list;
    }
}
