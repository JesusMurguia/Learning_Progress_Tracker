package org.jesusm.controller.menu;

import org.jesusm.controller.StudentController;

import java.util.List;

public class ListStudentsStrategy implements MenuStrategy {
    StudentController studentController = new StudentController();
    @Override
    public void run() {
        List<String> studentIds = studentController.getStudentIds();
        if(studentIds.isEmpty()){
            System.out.println("No students found.");
        }else{
            System.out.println("Students:");
        }
        studentController.getStudentIds().forEach(System.out::println);
    }
}
