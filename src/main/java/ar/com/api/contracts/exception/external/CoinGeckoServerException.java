package ar.com.api.contracts.exception.external;

public class CoinGeckoServerException extends RuntimeException{

    public CoinGeckoServerException(String message) {
        super(message);
    }

}
