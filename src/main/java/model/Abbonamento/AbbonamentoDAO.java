package model.Abbonamento;

import model.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbbonamentoDAO {

    /**Ritorna tutti gli abbonamenti*/
    public List<Abbonamento> doRetrieveAll(){
        try(Connection conn = ConPool.getConnection()){
            PreparedStatement st = conn.prepareStatement("SELECT * FROM abbonamento ABB;");
            ResultSet rs = st.executeQuery();
            List<Abbonamento> abbonamenti = new ArrayList<>();
            while (rs.next())
                abbonamenti.add(new AbbonamentoMapper().map(rs));
            return abbonamenti;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna gli abbonamenti piu acquistati*/
    public List<Abbonamento> doRetrieveTopBuy(){
        try (Connection conn = ConPool.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(AbbonamentoQuery.getQueryTopBuy());
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Abbonamento> lista = new ArrayList<>();
            while (resultSet.next())
                lista.add(new AbbonamentoMapper().map(resultSet));
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Aggiorna il prezzo dell abbonamento*/
    public boolean updatePrice(String codAbbonamento, double prezzo){
        try(Connection conn = ConPool.getConnection()){
            PreparedStatement st = conn.prepareStatement("UPDATE abbonamento ABB SET prezzo=? WHERE nome=?;");
            st.setDouble(1,prezzo);
            st.setString(2,codAbbonamento);
            return st.executeUpdate()==1;
         }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    /**Ritorna la somma dei prezzi degli abbonamenti acquistati*/
    public double doRetrieveTotaleGuadagno(){
        try (Connection conn = ConPool.getConnection()){
            PreparedStatement st = conn.prepareStatement(AbbonamentoQuery.getQueryTotaleGuadagni());
            ResultSet rs = st.executeQuery();
            if(!rs.next())
                return 0;
            else return rs.getDouble("totale");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
