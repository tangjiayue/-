package org.csu.softwaremetric.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Interface {
    private String id;
    private String name;
    private List<Operation> operations;
    private List<Interface> fathers;
    private List<Interface> children;
    private List<Classes> implementations;   //被哪些类实现
    private List<String> dependencies; //依赖
    private List<String> associations;

    public Interface() {
        super();
        fathers = new ArrayList<>();
        operations = new ArrayList<>();
        children = new ArrayList<>();
        implementations = new ArrayList<>();
        dependencies = new ArrayList<>();
        associations = new ArrayList<>();
    }

    public void addFather(Interface father){fathers.add(father);}

    public void addOperation(Operation operation){
        operations.add(operation);
    }
    public void addDependencies(String dependency){dependencies.add(dependency);}
    public void addAssociation(String association){associations.add(association);}

    public void addChildren(Interface targetInterface){
        children.add(targetInterface);
    }
    public void addImplementation(Classes implementation){implementations.add(implementation);}
}

