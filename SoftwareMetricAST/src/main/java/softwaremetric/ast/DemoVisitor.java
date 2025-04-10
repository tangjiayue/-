package softwaremetric.ast;
import java.util.*;
import org.eclipse.jdt.core.dom.*;

public class DemoVisitor extends ASTVisitor {
    private Map<String, ClassInfo> classInfoMap = new HashMap<>();
    private Map<String, Set<String>> classFieldMap = new HashMap<>();
    private String currentClassName = null; // 用于记录当前类名

    @Override
    public boolean visit(TypeDeclaration node) {
        // 当进入类声明时，记录类名
        currentClassName = node.getName().toString();
        if (!classInfoMap.containsKey(currentClassName)) {
            classInfoMap.put(currentClassName, new ClassInfo(currentClassName));
        }
        return true;
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        for (Object obj : node.fragments()) {
            VariableDeclarationFragment fragment = (VariableDeclarationFragment) obj;
            String fieldName = fragment.getName().toString();

            // 添加字段到当前类
            if (currentClassName != null) {
                classInfoMap.get(currentClassName).addField(fieldName);
                // 保存字段绑定信息
                classFieldMap.computeIfAbsent(currentClassName, k -> new HashSet<>()).add(fieldName);
            }
        }
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        String methodName = node.getName().toString();

        // 添加方法到当前类
        if (currentClassName != null) {
            MethodInfo methodInfo = new MethodInfo(methodName);
            methodInfo.setClassName(currentClassName);
            classInfoMap.get(currentClassName).addMethod(methodInfo);

            // 遍历方法体中的内容
            Block body = node.getBody();
            if (body != null) {
                body.accept(new MethodBodyVisitor(methodInfo));
            }
        }
        return true;
    }

    /**
     * 内部类 - 方法体访问器
     */
    private static class MethodBodyVisitor extends ASTVisitor {
        private MethodInfo methodInfo;

        public MethodBodyVisitor(MethodInfo methodInfo) {
            this.methodInfo = methodInfo;
        }

        @Override
        public boolean visit(SimpleName node) {
            IBinding binding = node.resolveBinding();
            
            if (binding instanceof IVariableBinding) {
                IVariableBinding variableBinding = (IVariableBinding) binding;
                System.out.println("1:"+node.getIdentifier());
                
             // 检查 SimpleName 是否是 FieldAccess 的一部分
                if (node.getParent() instanceof FieldAccess) {
                    System.out.println("SimpleName is part of a FieldAccess: " + node.getIdentifier());
                    return true; // 不单独处理，交给 FieldAccess 处理
                }
            
	            // 检查是否是字段，并且属于当前类
	            if (variableBinding.isField() &&!variableBinding.isParameter() && belongsToCurrentClass(variableBinding)) {
	                String fieldName = "FieldUse: " + node.getIdentifier();
	                methodInfo.addCall(fieldName);
	                System.out.println("Field Identified: " + fieldName);
	            } else {
	                System.out.println("Not a field: " + node.getIdentifier());
	            }
            }
            return true;
        }

        @Override
        public boolean visit(FieldAccess node) {
        	System.out.println("2:"+node.getName().toString());
        	IBinding binding = node.getName().resolveBinding();
            if (binding instanceof IVariableBinding) {
                IVariableBinding variableBinding = (IVariableBinding) binding;
                
         

                // 检查是否是字段，并且属于当前类
                if (variableBinding.isField() && !variableBinding.isParameter() &&belongsToCurrentClass(variableBinding)) {
                    String fieldName = "FieldUse: " + node.getName().toString();
                    methodInfo.addCall(fieldName);
                    System.out.println("Field Identified: " + fieldName);
                } else {
                    System.out.println("Not a field: " + node.getName().toString());
                }
            }
            return true;
        }
        
     // 辅助方法：判断变量是否属于当前类
        private boolean belongsToCurrentClass(IVariableBinding variableBinding) {
        	ITypeBinding declaringClass = variableBinding.getDeclaringClass();
            if (declaringClass == null) {
                return false; // 如果没有声明类，直接返回 false
            }
            String declaringClassName = declaringClass.getName(); // 获取声明类的简单名称
            String currentClassName = methodInfo.getClassName(); // 获取当前类的名称
            
            System.out.println(declaringClassName);
            return declaringClassName.equals(currentClassName); // 比较类名
        }
        
        @Override
        public boolean visit(MethodInvocation node) {
            String methodName = "MethodCall: " + node.getName().toString();
            
            // 当前方法名
            String currentMethodName = methodInfo.getMethodName();

            // 检查方法调用的接收者（即字段作为方法调用的目标）
            Expression expression = node.getExpression();
            // 判断是否是当前类中的方法调用
            if (isCurrentClassMethod(expression, currentMethodName)) {
                System.out.println("This is a method call from the current class: " + node.getName().toString());
            } else {
                System.out.println("This is a method call from another object/class: " + node.getName().toString());
                methodInfo.addCall(methodName);
            }

            // 检查方法调用的参数列表
            for (Object arg : node.arguments()) {
                if (arg instanceof SimpleName) {
                    SimpleName argNode = (SimpleName) arg;
                    IBinding binding = argNode.resolveBinding();
                	System.out.println("5:"+argNode.getIdentifier());
                    System.out.println("Argument binding: " + binding);
                    if (binding instanceof IVariableBinding) {
                        IVariableBinding variableBinding = (IVariableBinding) binding;
                        if (variableBinding.isField()) { // 判断是否是字段
                            String fieldName = "FieldUse: " + argNode.getIdentifier();
                            methodInfo.addCall(fieldName);
                        }
                    }
                } else if (arg instanceof FieldAccess) {
                    FieldAccess fieldAccess = (FieldAccess) arg;
                    System.out.println("6:"+fieldAccess.getName().toString());
                    String fieldName = "FieldUse: " + fieldAccess.getName().toString();
                    methodInfo.addCall(fieldName);
                }
            }
            return true;
        }
        
        private boolean isCurrentClassMethod(Expression expression, String currentMethodName) {
            // 如果没有接收者，或者接收者是 this
            if (expression == null || expression instanceof ThisExpression) {
                return true;
            }

            // 如果方法名与当前方法相同，视为递归调用
            if (expression instanceof SimpleName) {
                SimpleName receiver = (SimpleName) expression;
                IBinding binding = receiver.resolveBinding();
                if (binding instanceof IMethodBinding) {
                    IMethodBinding methodBinding = (IMethodBinding) binding;
                    return methodBinding.getName().equals(currentMethodName);
                }
            }

            return false;
        }
        
    }
    
    

    /**
     * 获取解析结果
     */
    public Map<String, ClassInfo> getClassInfoMap() {
        return classInfoMap;
    }
}