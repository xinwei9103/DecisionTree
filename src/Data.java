import java.util.HashMap;

/**
 * Created by xinweiwang on 2/7/16.
 */
public class Data {

    private int c;

    private HashMap<String, Boolean> attributes = new HashMap<>();

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public HashMap<String, Boolean> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Boolean> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name, boolean value) {
        this.attributes.put(name, value);
    }

    public void addAttribute(String name, String value) {
        if ("1".equals(value)) {
            this.addAttribute(name, true);
        } else if ("0".equals(value)) {
            this.addAttribute(name, false);
        }
    }

    public boolean getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public String toString() {
        return "Data{" +
                "c=" + c +
                ", attributes=" + attributes +
                '}';
    }
}
