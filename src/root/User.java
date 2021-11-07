package root;

public class User {
    private String username;
    private String password;
    private int coins = 20;
    private Deck deck;
    private int elo;
    private CardCollection cardCollection;

    public User(String username) {
        this.username = username;
    }
}
