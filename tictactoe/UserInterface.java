package tictactoe;

import java.util.Scanner;

public abstract class UserInterface {

    private final static Scanner scanner = new Scanner(System.in);

    public static void printLine(String text) {
        System.out.println(text);
    }
    public static void print(String text) { System.out.print(text); }

    public static Integer[] promptForCoordinates() {
        System.out.print("Enter the coordinates: ");
        String input = scanner.nextLine();
        if (!input.matches("^\\d \\d$")) {
            System.out.println("Invalid format!");
            return null;
        }
        try {
            input = input.replaceAll(" ", "");
            int x = Integer.parseInt(String.valueOf(input.charAt(0))) - 1;
            int y = Integer.parseInt(String.valueOf(input.charAt(1))) - 1;
            return new Integer[] {x, y};
        } catch (NumberFormatException e) {
            System.out.println("Numbers only!");
            return null;
        }
    }

    public static String[] promptForMenuCommands() {
        System.out.print("Input command: ");
        String command = scanner.nextLine();
        /*
        *   Available commands:
        *       exit
        *       start user user
		*   	start user easy
		*   	start easy user
		*   	start easy easy
        * */
        if (command.matches("exit|start (easy|medium|hard|user) (easy|medium|hard|user)")) {
            String[] parameters = command.split(" ");

            // if command equals "exit", return String array with one value - "exit"
            if (parameters[0].equals("exit")) return new String[] { "exit" };
            // else return both parameters after "start" command
            return new String[] { parameters[1], parameters[2] };
        }
        // If input does not match pattern, return null
        return null;
    }
}
