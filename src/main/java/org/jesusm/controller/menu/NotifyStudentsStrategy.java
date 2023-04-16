package org.jesusm.controller.menu;

import org.jesusm.controller.CourseController;
import org.jesusm.data.Email;

import java.util.List;
import java.util.stream.Collectors;

public class NotifyStudentsStrategy implements MenuStrategy {
    CourseController courseController = new CourseController();
    @Override
    public void run() {
        List<Email> emailList = courseController.getPendingNotifications();
        emailList.forEach(System.out::println);
        System.out.println("Total "+emailList.stream().
                collect(Collectors.groupingBy(
                        Email::to,
                        Collectors.counting()
                )).size()+" students have been notified.");
    }
}
