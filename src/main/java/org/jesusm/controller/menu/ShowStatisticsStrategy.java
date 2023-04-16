package org.jesusm.controller.menu;

import org.jesusm.IndividualStats;
import org.jesusm.controller.CourseController;
import org.jesusm.data.Stats;
import org.jesusm.view.Command;

import java.util.List;
import java.util.Optional;


public class ShowStatisticsStrategy implements MenuStrategy {
    CourseController courseController = new CourseController();
    @Override
    public void run() {
        System.out.println("Type the name of a course to see details or 'back' to quit");
        Stats stats = courseController.getStatistics();

        System.out.println("Most popular: " + stats.mostPopular());
        System.out.println("Least popular: " + stats.leastPopular());
        System.out.println("Highest activity: " + stats.mostPopular());
        System.out.println("Lowest activity: " + stats.leastPopular());
        System.out.println("Easiest course: " + stats.easiestCourse());
        System.out.println("Hardest course: " + stats.hardestCourse());

        String command = Command.getNextLine();
        while(!"back".equals(command)){
            Optional<List<IndividualStats>> statsOptional = courseController.getIndividualStats(command);
            if (statsOptional.isPresent()) {
                System.out.println(command);
                System.out.printf("%-4s %-7s %s\n", "id", "points", "completed");
                statsOptional.get().forEach(
                        s -> System.out.printf("%-4s %-7s %s\n", s.id(), s.points(), s.completed())
                );
            }else{
                System.out.println("Unknown course");
            }
            command = Command.getNextLine();
        }
    }
}
