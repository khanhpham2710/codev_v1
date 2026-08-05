package enums;

public enum EPropertyKey {
    API_TOKEN("api.token"), CONNECTION_STRING("connection_string"), DB_PASSWORD("db_password"), DB_USERNAME("db_username");

    private final String key;

    EPropertyKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
