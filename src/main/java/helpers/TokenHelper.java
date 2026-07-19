package helpers;

import java.security.SecureRandom;
import java.util.Base64;

public final class TokenHelper {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32;

    private TokenHelper() {
    }

    public static String generateAccessToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public static boolean isValidToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        try {
            byte[] decoded = Base64.getUrlDecoder().decode(token);
            return decoded.length == TOKEN_BYTES;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
