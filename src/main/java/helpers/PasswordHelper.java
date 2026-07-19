package helpers;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHelper {
    private static final int COST = 12;

    public static int checkPasswordStrength(String password) {
        int score = 0;
        if (password.length() >= 8) {
            score++;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        if (hasUppercase) {
            score++;
        }
        boolean hasLowercase = !password.equals(password.toUpperCase());
        if (hasLowercase) {
            score++;
        }
        boolean hasDigit = password.matches(".*\\d.*");
        if (hasDigit) {
            score++;
        }
        boolean hasSpecialChar = !password.matches("[A-Za-z0-9]*");
        if (hasSpecialChar) {
            score++;
        }
        if (score < 3) {
            return 1;
        } else if (score < 5) {
            return 2;
        } else {
            return 3;
        }
    }

    public static String hashPassword(char[] password) {
        return BCrypt.withDefaults()
                .hashToString(COST, password);
    }

    public static boolean matches(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }

        return BCrypt.verifyer()
                .verify(rawPassword.toCharArray(), hashedPassword.toCharArray())
                .verified;
    }
}
