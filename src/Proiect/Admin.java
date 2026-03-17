package Proiect;

class Admin {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";

    public static boolean authenticate(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }
}
