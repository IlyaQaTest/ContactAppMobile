package mobile.model.factory;


import mobile.model.User;

/**
 * Manual builder for creating User objects.
 * Provides a fluent interface for constructing User instances.
 */
public class UserBuilder {
    private String username;
    private String password;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public User build() {
        return new User(username, password);
    }
}