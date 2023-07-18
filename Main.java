package carsharing;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String databaseFilename = getDatabaseFileNameFromArguments(args);
        UserInterface ui = new UserInterface(scanner, databaseFilename);
        ui.boot();
    }

    private static String getDatabaseFileNameFromArguments(String[] args) {
        String databaseFileName = "carsharing";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName")) {
                if (i + 1 < args.length) {
                    databaseFileName = args[i + 1];
                }
                break;
            }
        }

        return databaseFileName;
    }
}