package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LanguageChecker {

    private static final boolean isNodeInstalled =
            checkInstalled("node", "-v");

    private static final boolean isJavaInstalled =
            checkInstalled("java", "-version");

    private static final boolean isPythonInstalled =
            checkInstalled("python", "--version")
                    || checkInstalled("python3", "--version");

    private static boolean checkInstalled(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {
            Process process = processBuilder.start();

            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line = stdout.readLine();
            if (line == null) {
                line = stderr.readLine();
            }

            process.waitFor();

            if (line != null) {
                System.out.println(line);
            }

            return process.exitValue() == 0;

        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public static boolean isNodeInstalled() {
        return isNodeInstalled;
    }

    public static boolean isJavaInstalled() {
        return isJavaInstalled;
    }

    public static boolean isPythonInstalled() {
        return isPythonInstalled;
    }
}