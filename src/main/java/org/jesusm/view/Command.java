package org.jesusm.view;

import java.util.Scanner;

public class Command {
    private static final Scanner scanner = new Scanner(System.in);
    public static String getNextLine(){
        return scanner.nextLine();
    }
}
