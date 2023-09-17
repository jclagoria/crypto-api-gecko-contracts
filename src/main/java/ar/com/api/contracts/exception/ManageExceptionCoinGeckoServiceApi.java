package ar.com.api.contracts.exception;

public class ManageExceptionCoinGeckoServiceApi {

    public static void throwServiceException(Throwable throwable) {
        throw new
                ServiceException(
                    throwable.getMessage(),
                    throwable.fillInStackTrace()
        );
    }



}
