package data.Abbonamento;

public abstract class AbbonamentoQuery {
    public static String getQueryTopBuy(){
        return "SELECT ABB.*, COUNT(*)  FROM attivazione ATT JOIN abbonamento ABB ON ATT.nomeAbbonamento=ABB.nome" +
                " GROUP BY ABB.nome ORDER BY COUNT(*) DESC LIMIT 0,10;";
    }

    public static String getQueryTotaleGuadagni(){
        return "SELECT SUM(prezzoMensile) totale FROM attivazione ATT, abbonamento ABB WHERE ATT.nomeAbbonamento=ABB.nome;";
    }
}
