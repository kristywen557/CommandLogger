package me.kristywen.commandlogger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Utils {
    public static String readFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String content = scanner.useDelimiter("\\Z").next();
            scanner.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
