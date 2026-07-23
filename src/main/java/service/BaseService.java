package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigProperties;
import dto.response.ResponseWrapper;
import enums.EPropertyKey;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseService {

    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper;

    protected BaseService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    protected BaseService() {
        this(HttpClient.newHttpClient(), new ObjectMapper());
    }

    protected <T> ResponseWrapper<T> send(HttpRequest request, Class<T> responseType) {
        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            int statusCode = response.statusCode();

            System.out.println(response.body());

            if (statusCode < 200 || statusCode >= 300) {
                logError(
                        request.method(),
                        request.uri().toString(),
                        statusCode,
                        response.body()
                );
                return ResponseWrapper.failure();
            }

            if (responseType == Void.class || response.body().isBlank()) {
                return ResponseWrapper.success(null);
            }

            T data = objectMapper.readValue(response.body(), responseType);
            return ResponseWrapper.success(data);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logException(request.method(), request.uri().toString(), e);
        } catch (IOException | IllegalArgumentException e) {
            logException(request.method(), request.uri().toString(), e);
        }

        return ResponseWrapper.failure();
    }

    protected <T> ResponseWrapper<T> get(
            String url,
            Class<T> responseType,
            Map<String, String> params,
            Map<String, String> headers
    ) {
        if (url == null || url.isBlank()) {
            return ResponseWrapper.failure();
        }

        params = params == null ? Map.of() : params;
        headers = headers == null ? Map.of() : headers;

        String finalUrl = buildUrl(url, params);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(finalUrl))
                .GET();

        headers.forEach(builder::header);

        return send(builder.build(), responseType);
    }

    protected <T> ResponseWrapper<T> get(String url, Class<T> responseType) {
        return get(url, responseType, Map.of(), Map.of());
    }

    protected <T> ResponseWrapper<T> getWithToken(String url, Class<T> responseType, Map<String, String> params) {
        String token = ConfigProperties.getInstance().getProperties(EPropertyKey.API_TOKEN);

        Map<String, String> headers = (token == null || token.isBlank())
                ? Map.of()
                : Map.of("Authorization", "Bearer " + token);

        return get(url, responseType, params, headers);
    }

    private String buildUrl(String url, Map<String, String> params) {
        if (params.isEmpty()) {
            return url;
        }

        String query = params.entrySet()
                .stream()
                .map(entry ->
                        URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)
                                + "="
                                + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        return url + (url.contains("?") ? "&" : "?") + query;
    }

    private void logError(String method, String url, int status, String body) {
        System.err.printf("""
                %s request failed
                URL: %s
                Status: %d
                Body: %s
                """, method, url, status, body);
    }

    private void logException(String method, String url, Exception e) {
        System.err.printf("""
                %s request failed
                URL: %s
                Reason: %s
                """, method, url, e.getMessage());
    }
}