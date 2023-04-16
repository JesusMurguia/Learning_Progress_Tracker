package org.jesusm.controller.menu;

import org.jesusm.controller.StudentController;
import org.jesusm.view.Command;

public class FindStudentsMenu implements MenuStrategy {
    StudentController studentController = new StudentController();
    @Override
    public void run() {
        System.out.println("Enter an id or 'back' to return");
        String command = Command.getNextLine();
        while(!"back".equals(command)){
            var student = studentController.findById(command);
            if(student.isPresent()){
                System.out.println(student.get());
            }else{
                System.out.printf("No student is found for id=%s\n", command);
            }
            command = Command.getNextLine();
        }
    }
}
