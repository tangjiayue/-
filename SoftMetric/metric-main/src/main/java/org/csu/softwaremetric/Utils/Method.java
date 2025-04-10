package org.csu.softwaremetric.Utils;

import org.csu.softwaremetric.entity.*;

import java.util.List;
import java.util.Stack;

public class Method {
    private CK ck;
    private LK lk;
    private Classes targetClass;
    private List<Classes> lists;
    private String methodString;

    public Method(Classes targetClass, List<Classes> lists) {
        super();
        ck = new CK();
        lk = new LK();
        this.targetClass = targetClass;
        this.lists = lists;
    }

    public Method(Classes targetClass, List<Classes> lists, String methodString) {
        super();
        ck = new CK();
        this.targetClass = targetClass;
        this.lists = lists;
        this.methodString = methodString;
    }
    /** Weighted Method per class   类加权方法数*/
    private double getWMC(){
        return targetClass.getOperations().size();
    }

    /** Response For a Class    类的响应数量*/
    private double getRFC(){
//        return targetClass.getAssociations().size() + targetClass.getDependencies().size()
//                + targetClass.getOperations().size();
        System.out.println("RFC: 调用方法数"+targetClass.getCallMethodNumber());
        return targetClass.getOperations().size()+targetClass.getCallMethodNumber();
    }

    /** Depth of Inheritance Tree   继承树的深度*/
    private double getDIT(){
        int DIT = 0;
        String generalization = targetClass.getGeneralization();
        while (generalization != null) {
            for (Classes element : lists) {
                if (generalization.equals(element.getId())) {
                    generalization = element.getGeneralization();
                    DIT++;
                    break;
                }
            }
        }
        return DIT;
    }

    /** Number Of Children      子类数量*/
    private double getNOC(){
        if (targetClass.getChildren() != null) {
            return targetClass.getChildren().size();
        } else {
            return 0;
        }
    }

    /** Coupling Between Objects       对象间的耦合度*/
    private double getCBO(){
        int CBO = 0;
        if (targetClass.getAssociations() != null) {
            CBO += targetClass.getAssociations().size();
        }
        if (targetClass.getDependencies() != null) {
            CBO += targetClass.getDependencies().size();
        }
        if(targetClass.getImplementations() != null) {
            CBO += targetClass.getImplementations().size();
        }
        return CBO;
    }

    /** Lack of Cohesion        类缺乏内聚性*/
    private double getLCOM(){
        int LCOM = 0;   //初始化
        LCOM = targetClass.getP()-targetClass.getQ();
        System.out.println("LCOM: P"+targetClass.getP()+" Q"+targetClass.getQ());
        return Math.max(LCOM, 0);


//        int LCOM = 1;   //初始化
//        if(methodString != null){
//            methodString = methodString.replace("    "," ");
//            String[] elements = methodString.split(" ");
//            String[] tempElems = elements;
//
//            List<Operation> operations = targetClass.getOperations();
//            for(String string : elements){
//                System.out.println(string);
//            }
//            System.out.println("method size:" + operations.size());
//
//            String[] methodCode = new String[operations.size()];
//            for (int i=0;i<operations.size();i++){
//                String tempCode = "";
//                System.out.println("first method: " + operations.get(i).getName());
//                for (int j=0;j<elements.length;j++) {
//                    // 判断是方法调用还是方法声明
//                    if (elements[j].contains(operations.get(i).getName()+"(")){
//                        int currentIndex = j;
//                        boolean flag = true;
//                        boolean isMethod = false;
//                        while (flag){
//                            if (elements[currentIndex].contains(")")){
//                                // 如果后接“{”，则为方法体
//                                if (elements[currentIndex].contains("{") || elements[currentIndex + 1].contains("{")){
//                                    if (elements[currentIndex + 1].contains("{")){
//                                        currentIndex++;
//                                    }
//
//                                    StringBuffer stringBuffer = new StringBuffer();
//                                    Stack<String> stack = new Stack<String>();
//
//                                    while (true){
//                                        if (elements[currentIndex].contains("{")){
//                                            stack.push("{");
//                                        }
//
//                                        stringBuffer.append(elements[currentIndex]);
//                                        currentIndex++;
//
//                                        if (elements[currentIndex].contains("}")){
//                                            stringBuffer.append(elements[currentIndex]);
//                                            tempCode = stringBuffer.toString();
//                                            stack.pop();
//
//                                            if (stack.isEmpty()){
//                                                flag = false;
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }else {
//                                    break;
//                                }
//                                currentIndex++;
//                            }
//                        }
//                        methodCode[i] = tempCode;
//                    }
//                }
//                // 计算LCOM
//                int p = 0, q = 0;
//                List<Attribute> attributes = targetClass.getAttributes();
//                for (int s=0;s<methodCode.length;s++){
//                    for (int t=s+1;t<methodCode.length;t++){
//                        for (int u=0;u<attributes.size();u++){
//                            if(methodCode[s].contains(attributes.get(u).getName())
//                                    && methodCode[t].contains(attributes.get(u).getName())){
//                                q++;
//                                break;
//                            }
//                        }
//                    }
//                }
//                int total = (methodCode.length)*(methodCode.length-1)/2;
//                p = total - q;
//                LCOM =  (p - q > 0 ? p - q : 0);
//
//                for (int v=0;v<methodCode.length;v++){
//                    System.out.println(methodCode[v]);
//                }
//            }
//        }else {
//            LCOM = 0;
//        }
//        return LCOM;
    }

    public CK getCk(){
        ck.setWMC(getWMC());
        ck.setRFC(getRFC());
        ck.setCBO(getCBO());
        ck.setDIT(getDIT());
        ck.setNOC(getNOC());
        ck.setLCOM(getLCOM());
        return ck;
    }

    /** number of operation     属性总数量*/
    private double getAttribute() {
        return targetClass.getAttributes().size();
    }

    /** number of attribute     方法总数量*/
    private double getOperation() {
        return targetClass.getOperations().size();
    }

    /** number of overridden    方法重写数*/
    private double getNOO() {
        Classes father = targetClass.getFather();
        int NOO = 0;
        if (father == null) {
            return NOO;
        } else {
            List<Operation> fatherOperations = father.getOperations();
            List<Operation> childOperations = targetClass.getOperations();
            Operation fatherOperation ;
            Operation childOperation;
            for (int i = 0; i < childOperations.size(); i++) {
                childOperation = childOperations.get(i);
                for (int j = 0; j < fatherOperations.size(); j++) {
                    fatherOperation = fatherOperations.get(j);
                    if (childOperation.getName().equals(fatherOperation.getName())&&
                            childOperation.getReturnType().equals(fatherOperation.getReturnType())&&
                            childOperation.getParameters().size()==fatherOperation.getParameters().size()) {
                        for(int k=0;k<childOperation.getParameters().size();k++){
                            Parameter parameter1 = childOperation.getParameters().get(k);
                            Parameter parameter2 = fatherOperation.getParameters().get(k);
                            if(parameter1.getName().equals(parameter2.getName())&&
                            parameter1.getClassName().equals(parameter2.getClassName())){
                                if(k==childOperation.getParameters().size()-1){
                                    NOO++;
                                }
                            }else {
                                break;
                            }

                        }
                    }
                }
            }
        }

        return NOO;
    }

    /** number of add           增加方法数量*/
    private double getNOA() {
        double NOA = 0;
        Classes father = targetClass.getFather();

        if (father == null) {
            return NOA;
        } else {
            List<Operation> fatherOperation = father.getOperations();
            List<Operation> childOperation = targetClass.getOperations();
            NOA = childOperation.size() - getNOO();
        }
        return NOA;
    }

    /** specialization index    特征化指数*/
    private double getSI() {
        Method ckMethod = new Method(targetClass, lists);
        CK ck = ckMethod.getCk();
        double DIT = ck.getDIT();
        double NOO = getNOO();
        double totalMethod = getOperation();
        double SI = (NOO * DIT) / totalMethod;

        return SI;


    }

    public LK getLk() {
        lk.setCS(getOperation()+getAttribute());
        lk.setNOA(getNOA());
        lk.setNOO(getNOO());
        lk.setSI(getSI());

        System.out.println("LK:"+lk.getCS()+" "+lk.getNOA()+" "+lk.getNOO()+" "+lk.getSI());
        return lk;
    }

}
