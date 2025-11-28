package com.project.back_end.dto;

public class Login {

    private String identifier; // existing field
    private String email;      // new field for email
    private String password;

    public Login() {
        // Default constructor needed for JSON deserialization
    }

    public Login(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
        this.email = identifier; // keep email in sync with identifier
    }

    public Login(String email, String password, boolean isEmail) {
        // Constructor specifically for email
        this.email = email;
        this.identifier = email;
        this.password = password;
    }

    // Getter and setter for identifier
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
        this.email = identifier; // keep email in sync
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.identifier = email; // keep identifier in sync
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
