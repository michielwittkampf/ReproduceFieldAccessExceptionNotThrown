package com.example.reproduce;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.github.tomakehurst.wiremock.client.WireMock;

import lombok.extern.slf4j.Slf4j;

@RestClientTest({ Client.class })
@AutoConfigureWireMock
@Slf4j
class ClientTest {

    @Autowired
    Client sut;

    /**
     * I expect this test to succeed. But it doesn't. No FieldAccessException is thrown.
     */
    @Test
    void whenRetrievingRootAndBranchPathContainsErrorThenFieldAccessException() {
        WireMock.stubFor(WireMock.any(WireMock.anyUrl())
                .willReturn(WireMock.ok()
                        .withBody(resourceToString("/responseWithBranchNullAndError.json"))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        assertThatExceptionOfType(FieldAccessException.class).isThrownBy(sut::getRoot);
    }

    /**
     * Informational test. Log what is returned in the above test.
     * I expect this test to fail, with a FieldAccessException. But it succeeds.
     */
    @Test
    void logRootWhenRetrievingRootAndBranchPathContainsError() {
        WireMock.stubFor(WireMock.any(WireMock.anyUrl())
                .willReturn(WireMock.ok()
                        .withBody(resourceToString("/responseWithBranchNullAndError.json"))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final var root = sut.getRoot();
        log.info("Root: {}", root);
    }

    /**
     * This test succeeds, as expected.
     */
    @Test
    void whenRetrievingRootAndRootPathContainsErrorThenFieldAccessException() {
        WireMock.stubFor(WireMock.any(WireMock.anyUrl())
                .willReturn(WireMock.ok()
                        .withBody(resourceToString("/responseWithRootNullAndError.json"))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        assertThatExceptionOfType(FieldAccessException.class).isThrownBy(sut::getRoot);
    }

    /**
     * Informational test. Checking if the basics work.
     */
    @Test
    void logRootWhenRetrievingRootAndAllRequestedDataIsReturned() {
        WireMock.stubFor(WireMock.any(WireMock.anyUrl())
                .willReturn(WireMock.ok()
                        .withBody(resourceToString("/responseWithData.json"))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final Root root = sut.getRoot();
        log.info("Roots: {}", root);
    }

    private static String resourceToString(String resourceName) {
        try {
            return new String(ClientTest.class.getResourceAsStream(resourceName).readAllBytes(), UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
