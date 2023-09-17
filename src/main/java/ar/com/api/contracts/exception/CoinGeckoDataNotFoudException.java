package ar.com.api.contracts.exception;

public class CoinGeckoDataNotFoudException extends RuntimeException {

    public CoinGeckoDataNotFoudException() {
        super("Data nor Found");
    }
    public CoinGeckoDataNotFoudException(String parameterNotFoundFilter) {
        super("Data not Found with " + parameterNotFoundFilter);
    }

}