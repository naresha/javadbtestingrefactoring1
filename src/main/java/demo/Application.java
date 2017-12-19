package demo;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

public class Application {
    private Connection connection;
    private DeveloperService developerService;

    public Application(Connection connection) {
        this.connection = connection;
        developerService = new DeveloperService(connection);
    }

    public void run() {
        System.out.println("");
        System.out.println("All Developers:");
        System.out.println("----------------");
        List<Developer> allDevelopers = developerService.getAllDevelopers();
        allDevelopers.forEach(System.out::println);

        System.out.println("");
        System.out.println("All Developers with Languages");
        System.out.println("-----------------------------");
        List<Developer> allDevelopersWithLanguages = developerService.getAllDevelopersWithLanguages();
        //System.out.println(allDevelopersWithLanguages);
        allDevelopersWithLanguages.forEach(System.out::println);


        System.out.println("");
        System.out.println("Developer with id 2");
        System.out.println("-------------------");
        Developer developerWithId2 = developerService.getDeveloperById(2L);
        System.out.println(developerWithId2);
        String name = developerService.getNameOf(2L);
        System.out.println("Name: " + name);
        Developer developer = developerService.getDeveloperById(2L);
        int age = developer.getName() != null ? developer.getAge() : null;
        System.out.println("Age: " + age);
        System.out.println("Before learning Groovy");
        Set<String> languages = developerService.languagesKnownBy(developer);
        System.out.println("Languages known: " + languages);
        System.out.println("Learning Groovy");
        Developer afterLearningGroovy = developerService.learn(developer, "Groovy");
        System.out.println(afterLearningGroovy);
        System.out.println("After learning Groovy");
        Set<String> languagesKnownLatest = developerService.languagesKnownBy(developer);
        System.out.println("Languages known: " + languagesKnownLatest);

        System.out.println("");
        System.out.println("Developers older than 30");
        System.out.println("------------------------");
        List<Developer> developersOlderThan30 = developerService.developersOlderThan(30);
        developersOlderThan30.forEach(System.out::println);

        System.out.println("");
        System.out.println("Developers younger than 30");
        System.out.println("--------------------------");
        List<Developer> developersYoungerThan30 = developerService.developersYoungerThan(30);
        developersYoungerThan30.forEach(System.out::println);

    }
}
