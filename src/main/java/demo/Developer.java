package demo;

import lombok.Data;

import java.util.Set;

@Data
public class Developer {
    private Long devId;
    private String name;
    private int age;
    private Set<String> languagesKnown;

}
