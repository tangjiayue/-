package org.csu.softwaremetric.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Classes {
    private String id;
    private String name;
    private List<Attribute> attributes;
    private List<Operation> operations;

    private Classes father;
    private String generalization;
    private List<Classes> children;
    private List<Interface> implementations;     //实现哪些接口
    private List<String> dependencies; //依赖
    private List<String> associations;

    private int callMethodNumber;
    private int P;  //不共享字段的方法对
    private int Q;  //共享字段的方法对


    public Classes() {
        super();
        attributes = new ArrayList<>();
        operations = new ArrayList<>();
        dependencies = new ArrayList<>();
        associations = new ArrayList<>();
        children = new ArrayList<>();
        implementations = new ArrayList<>();
        callMethodNumber=0;
        P=0;
        Q=0;
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    public void addOperation(Operation operation){
        operations.add(operation);
    }
    public void addDependencies(String dependency){dependencies.add(dependency);}
    public void addAssociation(String association){associations.add(association);}

    public void addChildren(Classes targetClass){
        children.add(targetClass);
    }

    public void addImplementation(Interface implementation){implementations.add(implementation);}

    public void addCallMethodNumber(int number){callMethodNumber = callMethodNumber+number;}

}
