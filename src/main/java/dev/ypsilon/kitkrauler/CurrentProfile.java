package dev.ypsilon.kitkrauler;

import java.util.HashMap;
import java.util.Optional;

@SuppressWarnings("unused")
public class CurrentProfile {

    private static final CurrentProfile INSTANCE = new CurrentProfile();

    private String gguid;
    private String tgiud;

    private final HashMap<String, String> data;
    private boolean firstInsert;

    @SuppressWarnings("all")
    private Optional<String> optionalUsername;
    @SuppressWarnings("all")
    private Optional<String> optionalPassword;

    public CurrentProfile() {
        gguid = "";
        tgiud = "";

        data = new HashMap<>();
        firstInsert = false;

        optionalUsername = Optional.empty();
        optionalPassword = Optional.empty();
    }

    public void setLoginInformation(String username, String password) {
        optionalUsername = Optional.of(username);
        optionalPassword = Optional.of(password);
    }

    public boolean usingConsoleAuthentication() {
        return optionalUsername.isPresent() && optionalPassword.isPresent();
    }

    public String getUsername() {
        return optionalUsername.orElse("");
    }

    public String getPassword() {
        return optionalPassword.orElse("");
    }

    public String getGguid() {
        return gguid;
    }

    public String getTguid() {
        return tgiud;
    }

    public void setGguid(String gguid) {
        this.gguid = gguid;
    }

    public void setTgiud(String tgiud) {
        this.tgiud = tgiud;
    }

    public boolean isFirstInsert() {
        return firstInsert;
    }

    public void setFirstInsert(boolean firstInsert) {
        this.firstInsert = firstInsert;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public static CurrentProfile get() {
        return INSTANCE;
    }

}
