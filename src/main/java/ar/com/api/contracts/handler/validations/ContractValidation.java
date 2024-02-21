package ar.com.api.contracts.handler.validations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Slf4j
public class ContractValidation {

    public static Mono<Boolean> validationForCallingGetContractByIdAndAddress(ServerRequest sRequest) {

        String idAsset = sRequest.pathVariable("id");
        String contractAddress = sRequest.pathVariable("contractAddress");

        return validateIdAssetAndContractAddressParameters(idAsset, contractAddress) ?
                Mono.error(new IllegalArgumentException("Id Asset or Contract Address are null or blanc.")) :
                Mono.just(true);
    }

    public static Mono<Boolean> validationGetContractAddressMarketChartById
            (ServerRequest sRequest) {

        String idAsset = sRequest.pathVariable("id");
        String contractAddress = sRequest.pathVariable("contractAddress");
        String vsCurrency = sRequest.queryParam("vsCurrency").orElse("");
        String days = sRequest.queryParam("days").orElse("");

        return validateIdAssetAndContractAddressParameters(idAsset, contractAddress) &&
                validateParameterCurrency(vsCurrency) &&
                validateParameterDays(days) ?
                    Mono.error(new IllegalArgumentException("Some parameters are null or blanc.")) :
                    Mono.just(true);
    }

    public static Mono<Boolean> validateGetContractAddressMarketChartByIdAndRange(ServerRequest sRequest) {

        String idAsset = sRequest.pathVariable("id");
        String contractAddress = sRequest.pathVariable("contractAddress");
        String vsCurrency = sRequest.queryParam("vsCurrency").orElse("");
        String dateFrom = sRequest.queryParam("fromDate").orElse("");
        String dateTo  = sRequest.queryParam("toDate").orElse("");

        return validateIdAssetAndContractAddressParameters(idAsset, contractAddress) &&
                validateParameterCurrency(vsCurrency) &&
                validateParameterDayRange(dateFrom, dateTo) ?
                    Mono.error(new IllegalArgumentException("Some parameters are null or blanc.")) :
                    Mono.just(true);
    }

    private static Boolean validateIdAssetAndContractAddressParameters(String idAsset, String contractAddress) {
        if (idAsset.isEmpty() || contractAddress.isEmpty()) {
            log.error("Id Asset or Contract Address are null or blanc");
            return true;
        }
        return false;
    }

    private static Boolean validateParameterCurrency(String vsCurrency) {
        if (vsCurrency.isEmpty()) {
            log.error("Currency are null or blanc");
            return true;
        }

        return false;
    }

    private static Boolean validateParameterDays(String days) {
        if(days.isEmpty()) {
            log.error("Days are null or blanc");
            return  true;
        }
        return false;
    }

    private static Boolean validateParameterDayRange(String from, String to) {
        if(from.isEmpty() || to.isEmpty()){
            log.error("Date From or To are null or blanc");
            return true;
        }
        return false;
    }


}
