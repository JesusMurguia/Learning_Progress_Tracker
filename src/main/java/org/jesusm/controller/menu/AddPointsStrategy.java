package org.jesusm.controller.menu;

import org.jesusm.controller.StudentController;
import org.jesusm.view.Command;

public class AddPointsStrategy implements MenuStrategy {
    StudentController studentController = new StudentController();
    @Override
    public void run() {
        System.out.println("Enter an id and points or 'back' to return");
        String command = Command.getNextLine();
        while(!"back".equals(command)){
            if(studentController.update(command)){
                System.out.println("Points updated");
            }
            command = Command.getNextLine();
        }
    }
}
