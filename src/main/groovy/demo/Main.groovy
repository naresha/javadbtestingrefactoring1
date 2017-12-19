package demo;


public class Main {
    public static void main(String[] args) {
        DbManager dbManager = DbManager.getInstance();
        dbManager.setup();
        Application application = new Application(dbManager.connection)
        application.run();
        //new Scanner(System.in).nextLine();
        dbManager.shutdown();
    }

}
