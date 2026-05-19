package com.seathub.auth.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seathub.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private final String secret;
    private final long accessTokenExpirationSeconds;
    private final ObjectMapper objectMapper;

    public JwtTokenProvider(
            @Value("${seathub.jwt.secret}") String secret,
            @Value("${seathub.jwt.access-token-expiration-seconds}") long accessTokenExpirationSeconds,
            ObjectMapper objectMapper
    ) {
        this.secret = secret;
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
        this.objectMapper = objectMapper;
    }

    public String createAccessToken(User user) {
        long issuedAt = Instant.now().getEpochSecond();
        long expiresAt = issuedAt + accessTokenExpirationSeconds;

        String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64Url("""
                {"sub":"%s","email":"%s","iat":%d,"exp":%d}
                """.formatted(user.getId(), user.getEmail(), issuedAt, expiresAt).trim());
        String unsignedToken = header + "." + payload;

        return unsignedToken + "." + sign(unsignedToken);
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = splitToken(token);
            String unsignedToken = parts[0] + "." + parts[1];
            String expectedSignature = sign(unsignedToken);

            if (!expectedSignature.equals(parts[2])) {
                return false;
            }

            return extractExpiration(parts[1]) > Instant.now().getEpochSecond();
        } catch (Exception exception) {
            return false;
        }
    }

    public Optional<Long> getUserId(String token) {
        try {
            String[] parts = splitToken(token);
            JsonNode payload = decodePayload(parts[1]);
            return Optional.of(payload.get("sub").asLong());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create JWT signature.", exception);
        }
    }

    private String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String[] splitToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format.");
        }
        return parts;
    }

    private long extractExpiration(String encodedPayload) throws Exception {
        return decodePayload(encodedPayload).get("exp").asLong();
    }

    private JsonNode decodePayload(String encodedPayload) throws Exception {
        byte[] decoded = Base64.getUrlDecoder().decode(encodedPayload);
        return objectMapper.readTree(new String(decoded, StandardCharsets.UTF_8));
    }
}
