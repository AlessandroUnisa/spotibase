package model.Canzone;


import model.Artista.Artista;
import model.Artista.ArtistaMapper;
import model.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CanzoneDAO{
    /**Ritorna la canzone con la lista degli artisti*/
    public Canzone doRetrieveCanzoneWithArtisti(String codiceCanzone){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoneWithArtisti());
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
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    /**Ritorna le ultime uscite*/
    public List<Canzone> doRetrieveCanzoniUltimeUscite(){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryDoRetriveCanzoniUltimeUscite());
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna le canzoni con piu preferenze con la lista degli artisti*/
    public List<Canzone> doRetrivePopularSongsWithArtista(){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryDoRetrievePopularSongsWithArtista());
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
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna una lista di canzoni random*/
    public List<Canzone> doRetrieveCanzoneRandom(){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryDoRetriveCanzoneRandom());
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna le canzoni acquistate dall'utente*/
    public List<Canzone> doRetrieveCanzoniAcquistate(String username){
        try (Connection connection = ConPool.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoniAcquistate());
            statement.setString(1,username);
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Canzone> list = new ArrayList<>();
            CanzoneMapper mapper = new CanzoneMapper();
            System.out.println(resultSet);
            while (resultSet.next())
                list.add(mapper.map(resultSet));
            return list;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Inserisce una preferenza*/
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

    /**Rimuove una preferenza*/
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

    /**Ritorna i codici delle canzoni preferite dall utente*/
    public List<String> doRetrieveaCodiciCanzoniPreferite(String username){
        try(Connection conn = ConPool.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(CanzoneQuery.getQueryDoRetrieveCodiciCanzoniPreferite());
            preparedStatement.setString(1,username);
            ArrayList<String> codici = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                codici.add(resultSet.getString("PRE.codiceCanzone"));
            return codici;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

   public List<Canzone> doRetrieveSongsByPlaylist(String username, String titoloPlaylist){
        try(Connection con = ConPool.getConnection()){

            PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryDoRetrieveSongsByPlaylist());
            String usernameLike = "%"+username+"%";
            String titoloLike = "%"+titoloPlaylist+"%";
            st.setString(1,usernameLike);
            st.setString(2,titoloLike);
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

   public List<Canzone> doRetrieveSongsByAlbum(String codAlbum){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryDoRetrieveSongsByAlbum());
            String codAlbumLike = "%"+codAlbum+"%";
            st.setString(1,codAlbumLike);
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

   public List<Canzone> doRetrieveSinglesByArtista(String nomeDArte){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryDoRetrieveSinglesByArtista());
            String nomeDArteLike = "%"+nomeDArte+"%";
            st.setString(1,nomeDArteLike);
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while (rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna le canzoni dell artista*/
   public List<Canzone> doRetrieveSongsByArtista(String nomeDArte){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryDoRetrieveSongsByArtista());
            String nomeDArteLike = "%"+nomeDArte+"%";
            st.setString(1,nomeDArteLike);
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while (rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna le canzoni con piu preferenze*/
   public List<Canzone> doRetrieveCanzonePopular(){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzonePopular());
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
         }
    }

    /** Ritorna la lista di canzoni che hanno quel titolo */
   public List<Canzone> doRetrieveByName(String titolo){
      try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement("SELECT * FROM canzone CAN WHERE CAN.titolo LIKE ?");
            String titleLike = "%"+titolo+"%";
            st.setString(1,titleLike);
            System.out.println(st.toString());
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while (rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }

   /** Salva nel DB la canzone*/
   public void doSave(Canzone canzone){
            try(Connection con = ConPool.getConnection()){
                PreparedStatement st = con.prepareStatement("INSERT INTO canzone VALUES (?,?,?,?,?,?)");
                st.setString(1,canzone.getCodice());
                st.setInt(2,canzone.getAnno());
                st.setDouble(3,canzone.getDurata());
                st.setString(4,canzone.getTitolo());
                st.setDouble(5,canzone.getPrezzo());
                if(st.executeUpdate()!=1)
                    throw new RuntimeException("insert error");

            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }

   /** Elimina la canzone dal DB*/
   public void doDelete(String codeCanzone){
            try (Connection con = ConPool.getConnection()){
                PreparedStatement st = con.prepareStatement("DELETE FROM canzone WHERE codice=?");
                st.setString(1,codeCanzone);
                if(st.executeUpdate()!=1)
                    throw new RuntimeException("delete error");
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }

   /** Modifica la canzone */
   public boolean doUpdate(Canzone canzone){
            try (Connection con = ConPool.getConnection()){
                PreparedStatement st = con.prepareStatement(CanzoneQuery.getQueryCanzoneUpdate());
                st.setString(1,canzone.getTitolo());
                st.setInt(2,canzone.getAnno());
                st.setDouble(3,canzone.getDurata());
                st.setDouble(4,canzone.getPrezzo());
                st.setString(5,canzone.getCodice());
                return st.executeUpdate()==1;
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }

   /** Ritorna tutti le canzoni*/
   public List<Canzone> doRetrieveAllCanzoni(){
            try(Connection con = ConPool.getConnection()){
                PreparedStatement st = con.prepareStatement("SELECT * FROM canzone CAN;");
                ResultSet rs = st.executeQuery();
                ArrayList<Canzone> lista = new ArrayList<>();
                while(rs.next())
                    lista.add(new CanzoneMapper().map(rs));
                con.close();
                return lista;
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }

   public List<Canzone> doRetrieveAllCanzoni(int offset, int numElements){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement("SELECT * FROM canzone CAN ORDER BY CAN.titolo LIMIT ?,?;");
            st.setInt(1,offset);
            st.setInt(2,numElements);
            ResultSet rs = st.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new CanzoneMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

   /** Ritorna le canzoni del genere passato*/
   public List<Canzone> doRetrieveCanzoneByGenere(String genere){
        try(Connection conn = ConPool.getConnection()){
            PreparedStatement ps = conn.prepareStatement(CanzoneQuery.getQueryCanzoneByGenere());
            ps.setString(1,'%'+genere+'%');
            List<Canzone> canzoni = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                canzoni.add(new CanzoneMapper().map(rs));
            conn.close();
            return canzoni;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Canzone> doRetrieveCanzoneByGenere(String genere, String filterSQL, int offset, int numElements){
        try (Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CanzoneQuery.getQueryCanzoneByGenere(filterSQL));
            preparedStatement.setString(1,genere);
            preparedStatement.setInt(2,offset);
            preparedStatement.setInt(3,numElements);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Canzone> list = new ArrayList<>();
            CanzoneMapper canzoneMapper = new CanzoneMapper();
            while(resultSet.next())
                list.add(canzoneMapper.map(resultSet));
            return list;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna il numero di canzoni nel DB*/
   public int doRetrieveNumCanzoni() {
       try (Connection conn = ConPool.getConnection()) {
           PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) NUM FROM canzone;");
           ResultSet resultSet = statement.executeQuery();
           if(resultSet.next())
               return resultSet.getInt("NUM");
           else return 0;
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }
   /*devo sapere quante canzoni ho nel database. Se ne tengo 100 e ad ogni pagina ne mosto 10 allora 100/10=10pagine
   * se pero ne tengo 92 => 92\10=9.2 arrotondo sempre per eccesso e quindi devo fare 10 pagine
   * */

    /**Ritorna la somma dei guadagni dalle canzoni acquistate*/
    public double doRetrieveTotaleGuadagno(){
        try (Connection conn = ConPool.getConnection()){
            PreparedStatement st = conn.prepareStatement(CanzoneQuery.getQueryTotaleGuadagni());
            ResultSet rs = st.executeQuery();
            if(!rs.next())
                return 0;
            else return rs.getDouble("totale");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna le canzoni piu acquistate*/
    public List<Canzone> doRetrieveCanzoniTopBuy(){
        try (Connection conn = ConPool.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoniTopBuy());
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Canzone> lista = new ArrayList<>();
            while (resultSet.next())
                lista.add(new CanzoneMapper().map(resultSet));
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna la canzone con quel codice*/
    public Canzone doRetrieveCanzoneByCodice(String codice){
        try (Connection conn = ConPool.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(CanzoneQuery.getQueryDoRetrieveAlbumByCodice());
            preparedStatement.setString(1,codice);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return new CanzoneMapper().map(resultSet);
            else return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna la lista di canzoni in base ai codici passati, con la lista artisti*/
    public List<Canzone> doRetrieveCanzoniByCodiciWithArtisti(List<String> codici){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoniByCodici(codici.size()));
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
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Inserisce una canzone acquistata*/
    public boolean doInsertCanzoneAcquistata(String username, String codice){
        try (Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CanzoneQuery.getQueryDoInsertCanzoneAcquistata());
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,codice);
            return preparedStatement.executeUpdate()==1;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna la lista delle canzoni acquistate dall utente*/
    public List<String> doRetrieveCodiciCanzoniAcquistate(String username){
        try (Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CanzoneQuery.getQueryDoRetrieveCodiciCanzoniAcquistate());
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> codici = new ArrayList<>();
            while (resultSet.next())
                codici.add(resultSet.getString("codCanzone"));
            return codici;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}