package helpers;

import enums.ELanguage;

import java.io.BufferedReader;
import java.io.File;
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

    public static ELanguage checkFileLanguage(String fileName) {
        if (fileName.endsWith(".js")) {
            return ELanguage.JAVASCRIPT;
        }
        if (fileName.endsWith(".java")) {
            return ELanguage.JAVA;
        }
        if (fileName.endsWith(".py")) {
            return ELanguage.PYTHON;
        }

        return null;
    }

    public static boolean checkCanRunFile(String fileName){
        ELanguage language = checkFileLanguage(fileName);

        if (language == null) return false;
        else {
            switch (language){
                case JAVA -> {
                    return isJavaInstalled();
                }
                case JAVASCRIPT -> {
                    return isNodeInstalled();
                }
                case PYTHON -> {
                    return isPythonInstalled();
                }
                default -> {
                    return false;
                }
            }
        }
    }

    public static boolean runFile(File file) {
        ELanguage language = checkFileLanguage(file.getName());

        if (language == null || !checkCanRunFile(file.getName())) {
            return false;
        }

        ProcessBuilder processBuilder;

        String filePath = file.getAbsolutePath();

        switch (language) {
            case JAVA -> {
                String className = file.getName().replace(".java", "");

                try {
                    Process compile = new ProcessBuilder("javac", filePath)
                            .redirectErrorStream(true)
                            .start();

                    BufferedReader compileReader = new BufferedReader(
                            new InputStreamReader(compile.getInputStream())
                    );

                    StringBuilder compileOutput = new StringBuilder();
                    String compileLine;
                    while ((compileLine = compileReader.readLine()) != null) {
                        compileOutput.append(compileLine).append("\n");
                    }

                    compile.waitFor();

                    if (compile.exitValue() != 0) {
                        System.out.println("⚠ Compile warning: " + compileOutput.toString().trim());
                        return false;
                    }

                    processBuilder = new ProcessBuilder(
                            "java",
                            "-cp",
                            file.getParent(),
                            className
                    );

                } catch (IOException | InterruptedException e) {
                    System.out.println("⚠ Compile warning: " + e.getMessage());
                    return false;
                }
            }

            case JAVASCRIPT -> processBuilder = new ProcessBuilder(
                    "node",
                    filePath
            );

            case PYTHON -> processBuilder = new ProcessBuilder(
                    "python",
                    filePath
            );

            default -> {
                return false;
            }
        }

        try {
            Process process = processBuilder
                    .redirectErrorStream(true)
                    .start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
            }

            process.waitFor();

            return process.exitValue() == 0;

        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}