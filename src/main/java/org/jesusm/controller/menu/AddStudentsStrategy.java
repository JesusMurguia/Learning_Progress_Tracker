package org.jesusm.controller.menu;

import org.jesusm.controller.StudentController;
import org.jesusm.view.Command;

public class AddStudentsStrategy implements MenuStrategy {
    StudentController studentController = new StudentController();
    @Override
    public void run() {
        System.out.println("Enter student credentials or 'back' to return:");

        String command = Command.getNextLine();

        int addedStudents = 0;


        while(!"back".equals(command)){
            if(studentController.save(command)){
                System.out.println("The student has been added");
                addedStudents++;
            }
            command = Command.getNextLine();
        }
        System.out.println("Total "+addedStudents+" students have been added");
    }
}
