package banking;

public class Main {
    public static void main(String[] args) {
        String dbName = null;
        if (args[0].equals("-fileName")) {
            dbName = args[1];
        }
        Utils utils = new Utils();
        CardJDBC cardJDBC = new CardJDBC(dbName);
        new Card(utils, cardJDBC).mainMenu();
    }
}