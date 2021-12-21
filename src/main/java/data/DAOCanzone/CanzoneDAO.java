package data.DAOCanzone;


import data.utils.Dao;
import data.utils.SingletonJDBC;
import data.Artista.Artista;
import data.Artista.ArtistaMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CanzoneDAO implements CanzoneAPI {
    //metodi documentati per IS----------------------------------------------------------------------------------------------

    @Override
    /**Questo metodo preleva una canzone salvata nel db
     * @param chiavi la primary key della canzone
     * @return oggetto canzone
     * */
    public Canzone doGet(String chiave) throws SQLException {
        String[] chiavi = chiave.split(";");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM canzone CAN WHERE CAN.codice = ?");
        preparedStatement.setString(1,chiavi[0]);
        CanzoneMapper mapper = new CanzoneMapper();
        return mapper.map(preparedStatement.executeQuery());
    }

    /** Salva nel DB la canzone*/
    public boolean doSave(Canzone canzone) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("INSERT INTO canzone VALUES (?,?,?,?,?,?)");
        preparedStatement.setString(1,canzone.getCodice());
        preparedStatement.setInt(2,canzone.getAnno());
        preparedStatement.setDouble(3,canzone.getDurata());
        preparedStatement.setString(4,canzone.getTitolo());
        preparedStatement.setDouble(5,canzone.getPrezzo());
        return preparedStatement.executeUpdate()==1;
    }


    /** Elimina la canzone dal DB*/
    public boolean doDelete(String codeCanzone) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("DELETE FROM canzone WHERE codice=?");
        preparedStatement.setString(1,codeCanzone);
        return preparedStatement.executeUpdate()==1;
    }

//questi bho

    /**Ritorna la canzone con la lista degli artisti*/
    public Canzone doRetrieveCanzoneWithArtisti(String codiceCanzone) throws SQLException {
        PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoneWithArtisti());
        statement.setString(1,codiceCanzone);
        ResultSet resultSet = statement.executeQuery();
        Canzone canzone=null;
        if(resultSet.next()){
            canzone = new CanzoneMapper().map(resultSet);
            canzone.setArtisti(new ArrayList<>());
            canzone.getArtisti().add(new ArtistaMapper().map(resultSet));
            while (resultSet.next())
                canzone.getArtisti().add(new ArtistaMapper().map(resultSet));
        }
        return canzone;
    }




    //metodi NON documentati per IS----------------------------------------------------------------------------------------------

    /**Ritorna le ultime uscite*/
    public List<Canzone> doRetrieveCanzoniUltimeUscite() throws SQLException {
            PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetriveCanzoniUltimeUscite());
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new CanzoneMapper().map(rs));

            return lista;
    }

    /**Ritorna le canzoni con piu preferenze con la lista degli artisti*/
    public List<Canzone> doRetrivePopularSongsWithArtista() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrievePopularSongsWithArtista());
        ResultSet resultSet= st.executeQuery();
        LinkedHashMap<String,Canzone> mappaCanzone= new LinkedHashMap<>();
        CanzoneMapper canzoneMapper= new CanzoneMapper();
        Canzone canzone;
        ArtistaMapper artistaMapper= new ArtistaMapper();
        while(resultSet.next()){
            canzone= canzoneMapper.map(resultSet);
            if(!mappaCanzone.containsKey(canzone.getCodice())){
                mappaCanzone.put(canzone.getCodice(),canzone);
                canzone.setArtisti(new ArrayList<Artista>());
                canzone.getArtisti().add(artistaMapper.map(resultSet));
            }else
                mappaCanzone.get(canzone.getCodice()).getArtisti().add(artistaMapper.map(resultSet));


        }
        return new ArrayList<>(mappaCanzone.values());

    }

    /**Ritorna una lista di canzoni random*/
    public List<Canzone> doRetrieveCanzoneRandom() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetriveCanzoneRandom());
        ResultSet rs = st.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new CanzoneMapper().map(rs));

        return lista;
    }



    /**Ritorna i codici delle canzoni preferite dall utente*/
    public List<String> doRetrieveaCodiciCanzoniPreferite(String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveCodiciCanzoniPreferite());
        preparedStatement.setString(1,username);
        ArrayList<String> codici = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            codici.add(resultSet.getString("PRE.codiceCanzone"));
        return codici;
    }

   public List<Canzone> doRetrieveSongsByPlaylist(String username, String titoloPlaylist) throws SQLException {

            PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveSongsByPlaylist());
            String usernameLike = "%"+username+"%";
            String titoloLike = "%"+titoloPlaylist+"%";
            st.setString(1,usernameLike);
            st.setString(2,titoloLike);
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new CanzoneMapper().map(rs));

            return lista;
    }

   public List<Canzone> doRetrieveSongsByAlbum(String codAlbum) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveSongsByAlbum());
        String codAlbumLike = "%"+codAlbum+"%";
        st.setString(1,codAlbumLike);
        ResultSet rs = st.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new CanzoneMapper().map(rs));

        return lista;
    }

   public List<Canzone> doRetrieveSinglesByArtista(String nomeDArte) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveSinglesByArtista());
        String nomeDArteLike = "%"+nomeDArte+"%";
        st.setString(1,nomeDArteLike);
        ResultSet rs = st.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while (rs.next())
            lista.add(new CanzoneMapper().map(rs));

        return lista;
    }

    /**Ritorna le canzoni dell artista*/
   public List<Canzone> doRetrieveSongsByArtista(String nomeDArte) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveSongsByArtista());
        String nomeDArteLike = "%"+nomeDArte+"%";
        st.setString(1,nomeDArteLike);
        ResultSet rs = st.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while (rs.next())
            lista.add(new CanzoneMapper().map(rs));

        return lista;
    }

    /**Ritorna le canzoni con piu preferenze*/
   public List<Canzone> doRetrieveCanzonePopular() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzonePopular());
        ResultSet rs = st.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new CanzoneMapper().map(rs));

        return lista;
    }

    /** Ritorna la lista di canzoni che hanno quel titolo */
   public List<Canzone> doRetrieveByName(String titolo) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM canzone CAN WHERE CAN.titolo LIKE ?");
        String titleLike = "%"+titolo+"%";
        st.setString(1,titleLike);
        System.out.println(st.toString());
        ResultSet rs = st.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while (rs.next())
            lista.add(new CanzoneMapper().map(rs));

        return lista;
   }



   /** Modifica la canzone */
   public boolean doUpdate(Canzone canzone) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryCanzoneUpdate());
        st.setString(1,canzone.getTitolo());
        st.setInt(2,canzone.getAnno());
        st.setDouble(3,canzone.getDurata());
        st.setDouble(4,canzone.getPrezzo());
        st.setString(5,canzone.getCodice());
        return st.executeUpdate()==1;
   }

   /** Ritorna tutti le canzoni*/
   public List<Canzone> doRetrieveAllCanzoni() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM canzone CAN;");
        ResultSet rs = st.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new CanzoneMapper().map(rs));

        return lista;
   }

   public List<Canzone> doRetrieveAllCanzoni(int offset, int numElements) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM canzone CAN ORDER BY CAN.titolo LIMIT ?,?;");
        st.setInt(1,offset);
        st.setInt(2,numElements);
        ResultSet rs = st.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new CanzoneMapper().map(rs));

        return lista;
   }

   /** Ritorna le canzoni del genere passato*/
   public List<Canzone> doRetrieveCanzoneByGenere(String genere) throws SQLException {
        PreparedStatement ps = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryCanzoneByGenere());
        ps.setString(1,'%'+genere+'%');
        List<Canzone> canzoni = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next())
            canzoni.add(new CanzoneMapper().map(rs));

        return canzoni;
    }

    public List<Canzone> doRetrieveCanzoneByGenere(String genere, String filterSQL, int offset, int numElements) throws SQLException {

        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryCanzoneByGenere(filterSQL));
        preparedStatement.setString(1,genere);
        preparedStatement.setInt(2,offset);
        preparedStatement.setInt(3,numElements);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Canzone> list = new ArrayList<>();
        CanzoneMapper canzoneMapper = new CanzoneMapper();
        while(resultSet.next())
            list.add(canzoneMapper.map(resultSet));
        return list;
    }

    /**Ritorna il numero di canzoni nel DB*/
   public int doRetrieveNumCanzoni() throws SQLException {
       PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement("SELECT COUNT(*) NUM FROM canzone;");
       ResultSet resultSet = statement.executeQuery();
       if(resultSet.next())
           return resultSet.getInt("NUM");
       else return 0;
   }
   /*devo sapere quante canzoni ho nel database. Se ne tengo 100 e ad ogni pagina ne mosto 10 allora 100/10=10pagine
   * se pero ne tengo 92 => 92\10=9.2 arrotondo sempre per eccesso e quindi devo fare 10 pagine
   * */

    /**Ritorna la somma dei guadagni dalle canzoni acquistate*/
    public double doRetrieveTotaleGuadagno() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryTotaleGuadagni());
        ResultSet rs = st.executeQuery();
        if(!rs.next())
            return 0;
        else return rs.getDouble("totale");
    }

    /**Ritorna le canzoni piu acquistate*/
    public List<Canzone> doRetrieveCanzoniTopBuy() throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoniTopBuy());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Canzone> lista = new ArrayList<>();
        while (resultSet.next())
            lista.add(new CanzoneMapper().map(resultSet));
        return lista;
    }

    /**Ritorna la canzone con quel codice*/
    public Canzone doRetrieveCanzoneByCodice(String codice) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveAlbumByCodice());
        preparedStatement.setString(1,codice);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return new CanzoneMapper().map(resultSet);
        else return null;
    }

    /**Ritorna la lista di canzoni in base ai codici passati, con la lista artisti*/
    public List<Canzone> doRetrieveCanzoniByCodiciWithArtisti(List<String> codici) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoniByCodici(codici.size()));
        for(int i=0; i<codici.size(); i++)
            preparedStatement.setString(1+i,codici.get(i));

        ResultSet resultSet = preparedStatement.executeQuery();
        LinkedHashMap<String, Canzone> mappa = new LinkedHashMap<>();
        while (resultSet.next()){
            Canzone canzone = new CanzoneMapper().map(resultSet);
            if(!mappa.containsKey(canzone.getCodice())){
                canzone.setArtisti(new ArrayList<>());
                mappa.put(canzone.getCodice(), canzone);
                canzone.getArtisti().add(new ArtistaMapper().map(resultSet));
            }else mappa.get(canzone.getCodice()).getArtisti().add(new ArtistaMapper().map(resultSet));
        }
        return new ArrayList<>(mappa.values());
    }
/*

    public boolean doInsertPreferenza(String codice, String username){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CanzoneQuery.getQueryDoInsertPreferenza());
            preparedStatement.setString(1,codice);
            preparedStatement.setString(2,username);
            return preparedStatement.executeUpdate()!=0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public boolean doRemovePreferenza(String codice, String username){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CanzoneQuery.getQueryDoDeletePreferenza());
            preparedStatement.setString(1,codice);
            preparedStatement.setString(2,username);
            return preparedStatement.executeUpdate()!=0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
*/
}