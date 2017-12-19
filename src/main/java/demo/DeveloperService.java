package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DeveloperService {
    private Connection connection;

    public DeveloperService(Connection connection) {
        this.connection = connection;
    }

    public List<Developer> developersOlderThan(int age) {
        List<Developer> allDevelopers = getAllDevelopers();
        List<Developer> developersOlderThanAge = new ArrayList<>();
        for (Developer developer : allDevelopers) {
            if (developer.getAge() > age) {
                developersOlderThanAge.add(developer);
            }
        }
        return developersOlderThanAge;
    }

    public List<Developer> developersYoungerThan(int age) {
        List<Developer> allDevelopers = getAllDevelopers();
        List<Developer> developersYoungerThanAge = new ArrayList<>();
        for (Developer developer : allDevelopers) {
            if (developer.getAge() < age) {
                developersYoungerThanAge.add(developer);
            }
        }
        return developersYoungerThanAge;
    }

    public List<Developer> getAllDevelopers() {
        List<Developer> developers = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * from DEVELOPER");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Developer developer = new Developer();
                developer.setDevId(resultSet.getLong("id"));
                developer.setName(resultSet.getString("name"));
                developer.setAge(resultSet.getInt("age"));
                developers.add(developer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while querying", e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return developers;
    }

    public Set<String> languagesKnownBy(Developer developer) {
        PreparedStatement preparedStatement = null;
        Set<String> languages = new HashSet<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM DEV_LANGUAGE WHERE devid = ?");
            preparedStatement.setLong(1, developer.getDevId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                languages.add(resultSet.getString("language"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return languages;
    }

    public Developer learn(Developer student, String language) {
        Developer developer = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * from DEVELOPER WHERE id = ?");
            preparedStatement.setLong(1, student.getDevId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                developer = new Developer();
                developer.setDevId(resultSet.getLong("id"));
                developer.setName(resultSet.getString("name"));
                developer.setAge(resultSet.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (developer == null) {
            throw new RuntimeException("Developer NOT Found");
        }
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO DEV_LANGUAGE(devid, language) values(?, ?)");
            preparedStatement.setLong(1, developer.getDevId());
            preparedStatement.setString(2, language);
            int updatedRecords = preparedStatement.executeUpdate();
            System.out.println(updatedRecords + " records updated");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM  DEV_LANGUAGE dl WHERE dl.devid = ?");
            preparedStatement.setLong(1, developer.getDevId());
            ResultSet resultSet = preparedStatement.executeQuery();
            developer.setLanguagesKnown(new HashSet<>());
            while (resultSet.next()) {
                developer.getLanguagesKnown().add(resultSet.getString("language"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return developer;
    }

    public List<Developer> getAllDevelopersWithLanguages() {
        List<DeveloperLanguageVO> developerLanguageVOS = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT d.id, d.name, d.age, dl.language FROM DEVELOPER d, DEV_LANGUAGE dl WHERE d.id = dl.devid");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DeveloperLanguageVO developer = new DeveloperLanguageVO();
                developer.setId(resultSet.getLong("id"));
                developer.setName(resultSet.getString("name"));
                developer.setAge(resultSet.getInt("age"));
                developer.setLanguage(resultSet.getString("language"));
                developerLanguageVOS.add(developer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while querying", e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        List<Developer> developers = new ArrayList<>();
        for (DeveloperLanguageVO developerVO : developerLanguageVOS) {
            Developer developer = null;
            for (Developer dev : developers) {
                if (dev.getDevId().equals(developerVO.getId())) {
                    developer = dev;
                }
            }
            if (developer == null) {
                developer = new Developer();
                developer.setDevId(developerVO.getId());
                developer.setName(developerVO.getName());
                developer.setAge(developerVO.getAge());
                developers.add(developer);
                developer.setLanguagesKnown(new HashSet<>());
            }
            developer.getLanguagesKnown().add(developerVO.getLanguage());
        }
        return developers;
    }

    public String getNameOf(Long devId) {
        Developer developer = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * from DEVELOPER WHERE id = ?");
            preparedStatement.setLong(1, devId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                developer = new Developer();
                developer.setDevId(resultSet.getLong("id"));
                developer.setName(resultSet.getString("name"));
                developer.setAge(resultSet.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return developer.getName() != null ? developer.getName() : null;
    }

    public Developer getDeveloperById(Long id) {
        Developer developer = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * from DEVELOPER WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                developer = new Developer();
                developer.setDevId(resultSet.getLong("id"));
                developer.setName(resultSet.getString("name"));
                developer.setAge(resultSet.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return developer;
    }
}
