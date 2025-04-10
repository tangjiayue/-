package softwaremetric.ast;
import java.util.*;

public class MethodInfo {
    private String className;
    private String methodName;
    private List<String> calls = new ArrayList<>();
//    private List<String> uses = new ArrayList<>();


    public MethodInfo(String methodName) {
        this.methodName = methodName;
    }
    
    public void setClassName(String className) {
        this.className=className;
    }
    
    public String getClassName() {
    	return this.className;
    }

    public void addCall(String call) {
        calls.add(call);
    }
    
//    public void addUse(String use) {
//        uses.add(use);
//    }
    
    public String getMethodName() {
        return methodName;
    }

    public List<String> getCalls() {
        return calls;
    }
    
//    public List<String> getUses() {
//        return uses;
//    }
}