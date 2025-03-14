package i2f.jdbc.procedure.context;

import java.util.*;

public class ProcedureMeta {
    public static enum Type {
        XML,
        JAVA
    }

    protected Type type;
    protected String name;
    protected Object target;
    protected List<String> arguments = new ArrayList<>();
    protected Map<String, List<String>> argumentFeatures = new LinkedHashMap<>();

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public Map<String, List<String>> getArgumentFeatures() {
        return argumentFeatures;
    }

    public void setArgumentFeatures(Map<String, List<String>> argumentFeatures) {
        this.argumentFeatures = argumentFeatures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcedureMeta that = (ProcedureMeta) o;
        return type == that.type
                && Objects.equals(name, that.name)
                && Objects.equals(target, that.target)
                && Objects.equals(arguments, that.arguments)
                && Objects.equals(argumentFeatures, that.argumentFeatures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, target, arguments, argumentFeatures);
    }

    @Override
    public String toString() {
        return "ProcedureMeta{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", target=" + target +
                ", arguments=" + arguments +
                ", argumentFeatures=" + argumentFeatures +
                '}';
    }
}
