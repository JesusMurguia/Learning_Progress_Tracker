package org.jesusm.view;

import org.jesusm.controller.menu.*;

public class Menu {
    public void mainMenu(){
        System.out.println("Learning Progress Tracker");

        String command = Command.getNextLine();

        while(true){
            if(command.isBlank()){
                System.out.println("No input");
            } else {
                switch (command) {
                    case "add students" -> run(new AddStudentsStrategy());
                    case "list" -> run(new ListStudentsStrategy());
                    case "add points" -> run(new AddPointsStrategy());
                    case "statistics" -> run(new ShowStatisticsStrategy());
                    case "find" -> run(new FindStudentsMenu());
                    case "notify" -> run(new NotifyStudentsStrategy());
                    case "exit" -> exit();
                    case "back" -> System.out.println("Enter 'exit' to exit the program");
                    default -> System.out.println("Unknown command!");
                }
            }
            command = Command.getNextLine();
        }

    }

    private void run(MenuStrategy menu){
        menu.run();
    }

    private void exit(){
        System.out.println("Bye!");
        System.exit(0);
    }
}
