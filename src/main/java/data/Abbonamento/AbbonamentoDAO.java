package data.Abbonamento;

import data.utils.SingletonJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbbonamentoDAO {
    //metodi NON documentati per IS----------------------------------------------------------------------------------------------

    /**Ritorna tutti gli abbonamenti*/
    public List<Abbonamento> doRetrieveAll() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM abbonamento ABB;");
        ResultSet rs = st.executeQuery();
        List<Abbonamento> abbonamenti = new ArrayList<>();
        while (rs.next())
            abbonamenti.add(new AbbonamentoMapper().map(rs));
        return abbonamenti;
    }

    /**Ritorna gli abbonamenti piu acquistati*/
    public List<Abbonamento> doRetrieveTopBuy() throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(AbbonamentoQuery.getQueryTopBuy());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Abbonamento> lista = new ArrayList<>();
        while (resultSet.next())
            lista.add(new AbbonamentoMapper().map(resultSet));
        return lista;
    }

    /**Aggiorna il prezzo dell abbonamento*/
    public boolean updatePrice(String codAbbonamento, double prezzo) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("UPDATE abbonamento ABB SET prezzo=? WHERE nome=?;");
        st.setDouble(1,prezzo);
        st.setString(2,codAbbonamento);
        return st.executeUpdate()==1;
    }


    /**Ritorna la somma dei prezzi degli abbonamenti acquistati*/
    public double doRetrieveTotaleGuadagno() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(AbbonamentoQuery.getQueryTotaleGuadagni());
        ResultSet rs = st.executeQuery();
        if(!rs.next())
            return 0;
        else return rs.getDouble("totale");
    }

}
