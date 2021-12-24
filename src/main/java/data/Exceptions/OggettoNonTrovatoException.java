package data.Exceptions;

public class OggettoNonTrovatoException extends RuntimeException{
    public OggettoNonTrovatoException(){//
        super();
    }
    public OggettoNonTrovatoException(String msg){
        super(msg);
    }

}
