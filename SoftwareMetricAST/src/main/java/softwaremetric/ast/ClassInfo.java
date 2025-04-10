package softwaremetric.ast;
import java.util.*;

public class ClassInfo {
    private String className;
    private List<String> fields = new ArrayList<>();
    private List<MethodInfo> methods = new ArrayList<>();

    public ClassInfo(String className) {
        this.className = className;
    }

    public void addField(String fieldName) {
        fields.add(fieldName);
    }

    public void addMethod(MethodInfo methodInfo) {
        methods.add(methodInfo);
    }

    public String getClassName() {
        return className;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }
}