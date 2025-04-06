package iscteiul.ista;

import java.util.HashMap;
import java.util.Map;

public class Property {
    private Map<String, String> attributes;

    public Property() {
        attributes = new HashMap<>();
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return attributes.toString();
    }
}
