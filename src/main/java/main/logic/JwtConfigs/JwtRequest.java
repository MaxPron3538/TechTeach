package main.logic.JwtConfigs;


import java.io.Serializable;

public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String email;
    private String passwordHash;

    public JwtRequest() {}

    public JwtRequest(String email, String passwordHash) {
        this.setEmail(email);
        this.setPasswordHash(passwordHash);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}