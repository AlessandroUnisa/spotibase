package data.Exceptions;

public class OggettoNonCancellatoException extends RuntimeException{
    public OggettoNonCancellatoException(){ //
        super();
    }
    public OggettoNonCancellatoException(String msg){
        super(msg);

    }


}
