package demo

import groovy.sql.Sql

import java.sql.Connection

@Singleton
class DbManager {

    Sql sql

    public void createConnection() {
        sql = Sql.newInstance("jdbc:h2:mem:db", "sa", "")
    }

    public void setup() {
        println "Setting up DB..."
        createConnection()
        setupTables()
    }

    public void setupTables() {
        setupTableDeveloper()
        setupTableLanguages()
    }

    public void setupTableDeveloper() {
        println "Creating table DEVELOPER"
        sql.execute('''
  CREATE TABLE DEVELOPER (
    id BIGINT,
    name VARCHAR(50) NOT NULL ,
    age INTEGER NOT NULL,
    PRIMARY KEY (id)
  )
''')
        println "Table DEVELOPER created"
        def records = [
                [1, 'Raj', 30],
                [2, 'Mark', 35],
                [3, 'Reema', 28],
                [4, 'Kumar', 32]
        ]
        records.each {
            sql.execute 'INSERT INTO DEVELOPER(id, name, age) VALUES (?, ?, ?)', it
        }

        sql.eachRow ' select * from developer', { row ->
            println row
        }
    }

    public void setupTableLanguages() {
        println "Creating table DEV_LANGUAGES"
        sql.execute '''
  CREATE TABLE DEV_LANGUAGE(
    devid BIGINT,
    language VARCHAR(50),
    FOREIGN KEY(devid) REFERENCES developer(id)
  )
'''
        println "Table DEV_LANGUAGE created"

        def records = [
                [1, "Java"],
                [1, "Groovy"],
                [1, "JavaScript"],
                [2, "Java"],
                [2, "JavaScript"],
                [3, "JavaScript"],
                [3, "Java"],
                [4, "JavaScript"],
                [4, "Java"]
        ]
        records.each {
            sql.execute 'INSERT INTO DEV_LANGUAGE(devid, language) VALUES (?, ?)', it
        }

        sql.eachRow ' select * from dev_language', { row ->
            println row
        }
    }


    public void shutdown() {
        println "Shutting down DB"
        sql.close()
    }

    public Connection getConnection() {
        sql.connection
    }


}
