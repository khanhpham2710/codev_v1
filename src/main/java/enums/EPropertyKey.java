package enums;

public enum EPropertyKey {
    API_TOKEN("api.token");

    private final String key;

    EPropertyKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
