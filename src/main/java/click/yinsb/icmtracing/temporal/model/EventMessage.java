package click.yinsb.icmtracing.temporal.model;


public class EventMessage {
    private String id;
    private String type;
    private boolean timeout;

    public EventMessage() {
    }

    public EventMessage(String id, String type, boolean timeout) {
        this.id = id;
        this.type = type;
        this.timeout = timeout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
}
