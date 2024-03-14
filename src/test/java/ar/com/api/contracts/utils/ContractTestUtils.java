package ar.com.api.contracts.utils;

import ar.com.api.contracts.enums.ErrorTypeEnum;
import ar.com.api.contracts.exception.ApiClientErrorException;
import ar.com.api.contracts.exception.ApiServerErrorException;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ContractTestUtils {

    public static void assertService4xxClientError(Publisher<?> publisher,
                                                   String expectedMessage,
                                                   ErrorTypeEnum expectedErrorType) {
        StepVerifier
                .create(publisher)
                .expectErrorMatches(throwable ->
                        throwable instanceof ApiServerErrorException &&
                                throwable.getMessage().equals(expectedMessage) &&
                                ((ApiServerErrorException) throwable).getHttpStatus().is4xxClientError() &&
                                ((ApiServerErrorException) throwable).getErrorTypeEnum().equals(expectedErrorType)
                )
                .verify();
    }

    public static void assertService5xxServerError(Publisher<?> publisher,
                                                   String expectedMessage,
                                                   ErrorTypeEnum expectedErrorType) {
        StepVerifier
                .create(publisher)
                .expectErrorMatches(throwable ->
                        throwable instanceof ApiServerErrorException &&
                                throwable.getMessage().equals(expectedMessage) &&
                                ((ApiServerErrorException) throwable).getHttpStatus().is5xxServerError() &&
                                ((ApiServerErrorException) throwable).getErrorTypeEnum().equals(expectedErrorType)
                )
                .verify();
    }

    public static void assertClient5xxServerError(Publisher<?> publisher,
                                                  String expectedMessage,
                                                  HttpStatus status) {
        StepVerifier
                .create(publisher)
                .expectErrorMatches(throwable ->
                        throwable instanceof ApiClientErrorException &&
                                throwable.getMessage().equals(expectedMessage) &&
                                ((ApiClientErrorException) throwable).getHttpStatus() == status &&
                                ((ApiClientErrorException) throwable).getErrorTypeEnum()
                                        .equals(ErrorTypeEnum.API_SERVER_ERROR)
                )
                .verify();
    }

    public static <T> void assertMonoSuccess(Mono<T> monoObject, Consumer<T> assertions) {
        StepVerifier
                .create(monoObject)
                .assertNext(assertions)
                .verifyComplete();
    }

    public static <T> void assertFluxSuccess(Flux<T> fluxObject, Consumer<Collection<T>> assertions) {
        StepVerifier
                .create(fluxObject)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(item -> true)
                .consumeRecordedWith(assertions)
                .verifyComplete();
    }

}
