package org.csu.softwaremetric.service;

import org.csu.softwaremetric.entity.Interface;
import org.springframework.stereotype.Service;

import org.csu.softwaremetric.entity.Association;

import org.csu.softwaremetric.entity.Classes;
import org.csu.softwaremetric.entity.Dependency;
import org.dom4j.Element;

import org.csu.softwaremetric.Utils.XMLUtil;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class XMLService {

    public static List<Classes> getMethod(String filename) {
        List<Association> associationList = new ArrayList<>();
        List<Dependency> dependencyList = new ArrayList<>();
        List<Classes> classList = new ArrayList<>();

        XMLUtil xmlUtil = new XMLUtil();
        Document document = xmlUtil.load(filename);
        Element rootNode = document.getRootElement();
        if(rootNode.elements("packagedElement").isEmpty()){
            Element model =rootNode.element("RootObject")
                    .element("Children").element("Model");
            List<Element> Classes = model.element("Classes")==null? new ArrayList<>() :model.element("Classes").elements("Class");
            List<Element> Generalizations = model.element("Generalizations")==null? new ArrayList<>() : model.element("Generalizations").elements("Generalization");
            List<Element> Interfaces = model.element("Interfaces")==null? new ArrayList<>() : model.element("Interfaces").elements("Interface");
            List<Element> Realizations = model.element("Realizations")==null? new ArrayList<>() : model.element("Realizations").elements("Realization");
            List<Element> Dependencies = model.element("Dependencies")==null? new ArrayList<>() : model.element("Dependencies").elements("Dependency");
            List<Element> Associations = model.element("Associations")==null? new ArrayList<>() : model.element("Associations").elements("Association");

            //解析类
            Map<String,Classes> classMap = XMLUtil.parseClasses(Classes);
            Map<String, Interface> interfaceMap = XMLUtil.parseInterface(Interfaces);
            XMLUtil.parseGeneralizations(Generalizations, classMap,interfaceMap);
            XMLUtil.parseRealizations(Realizations, classMap,interfaceMap);
            XMLUtil.parseDependencies(Dependencies, classMap,interfaceMap);
            XMLUtil.parseAssociations(Associations, classMap,interfaceMap);
            for(Map.Entry<String, Classes> clazz : classMap.entrySet()){
                classList.add(clazz.getValue());
            }

        }else {
            List<Element> list = rootNode.elements("packagedElement");

            for (Element element : list) { //循环输出全部packagedElement的相关信息
                XMLUtil.analysisXML(element, classList, associationList, dependencyList);
                List<Element> list2 = element.elements(); //得到emp元素下的子元素
                for (Element e : list2) {  //遍历emp元素下的子元素
                    XMLUtil.analysisXML(e, classList, associationList, dependencyList);
                }
            }
            xmlUtil.clean(classList);
        }

        return classList;
    }

    public static List<Classes> getMethod(String filename,String codeFileName) {
        List<Association> associationList = new ArrayList<>();
        List<Dependency> dependencyList = new ArrayList<>();
        List<Classes> classList = new ArrayList<>();

        XMLUtil xmlUtil = new XMLUtil();
        Document document = xmlUtil.load(filename);
        Element rootNode = document.getRootElement();

        XMLUtil codeXmlUtil = new XMLUtil();
        Document codeDocument = codeXmlUtil.load(codeFileName);
        Element codeRootNode = codeDocument.getRootElement();

        if(rootNode.elements("packagedElement").isEmpty()){
            Element model =rootNode.element("RootObject").element("Children").element("Model");
            List<Element> Classes = model.element("Classes")==null? new ArrayList<>() :model.element("Classes").elements("Class");
            List<Element> Generalizations = model.element("Generalizations")==null? new ArrayList<>() : model.element("Generalizations").elements("Generalization");
            List<Element> Interfaces = model.element("Interfaces")==null? new ArrayList<>() : model.element("Interfaces").elements("Interface");
            List<Element> Realizations = model.element("Realizations")==null? new ArrayList<>() : model.element("Realizations").elements("Realization");
            List<Element> Dependencies = model.element("Dependencies")==null? new ArrayList<>() : model.element("Dependencies").elements("Dependency");
            List<Element> Associations = model.element("Associations")==null? new ArrayList<>() : model.element("Associations").elements("Association");

            //解析类
            Map<String,Classes> classMap = XMLUtil.parseClasses(Classes);
            Map<String, Interface> interfaceMap = XMLUtil.parseInterface(Interfaces);
            XMLUtil.parseGeneralizations(Generalizations, classMap,interfaceMap);
            XMLUtil.parseRealizations(Realizations, classMap,interfaceMap);
            XMLUtil.parseDependencies(Dependencies, classMap,interfaceMap);
            XMLUtil.parseAssociations(Associations, classMap,interfaceMap);

            List<Element> codeClassInfo = codeRootNode.elements("Class")==null ? new ArrayList<>() : codeRootNode.elements("Class");
            XMLUtil.parseCodeClassInfo(codeClassInfo,classMap);

            for(Map.Entry<String, Classes> clazz : classMap.entrySet()){
                classList.add(clazz.getValue());
            }

        }else {
            List<Element> list = rootNode.elements("packagedElement");

            for (Element element : list) { //循环输出全部packagedElement的相关信息
                XMLUtil.analysisXML(element, classList, associationList, dependencyList);
                List<Element> list2 = element.elements(); //得到emp元素下的子元素
                for (Element e : list2) {  //遍历emp元素下的子元素
                    XMLUtil.analysisXML(e, classList, associationList, dependencyList);
                }
            }
            xmlUtil.clean(classList);
        }



        return classList;
    }


    public static List<Association> getAssociation(String filename) {
        // String filename = String.valueOf(new File("/Users/yenan/IdeaProjects/SoftwareMetrics/src/test/java/org/csu/Automation/target.xml"));
        XMLUtil xmlUtil = new XMLUtil();
        Document document = xmlUtil.load(filename);
        Element rootNode = document.getRootElement();
        List<Element> list = rootNode.elements("packagedElement");


        List<Classes> classList = new ArrayList<>();
        List<Association> associationList = new ArrayList<>();
        List<Dependency> dependencyList = new ArrayList<>();

        for (Element element : list) { //循环输出全部packagedElement的相关信息
            XMLUtil.analysisXML(element, classList, associationList, dependencyList);
            List<Element> list2 = element.elements(); //得到emp元素下的子元素
            for (Element e : list2) {  //遍历emp元素下的子元素
                XMLUtil.analysisXML(e, classList, associationList, dependencyList);
            }
        }
        return associationList;
    }


    public static List<Dependency> getDependency(String filename) {
        // String filename = String.valueOf(new File("/Users/yenan/IdeaProjects/SoftwareMetrics/src/test/java/org/csu/Automation/target.xml"));
        XMLUtil xmlUtil = new XMLUtil();
        Document document = xmlUtil.load(filename);
        Element rootNode = document.getRootElement();
        List<Element> list = rootNode.elements("packagedElement");


        List<Classes> classList = new ArrayList<>();
        List<Association> associationList = new ArrayList<>();
        List<Dependency> dependencyList = new ArrayList<>();

        for (Element element : list) { //循环输出全部packagedElement的相关信息
            XMLUtil.analysisXML(element, classList, associationList, dependencyList);
            List<Element> list2 = element.elements(); //得到emp元素下的子元素
            for (Element e : list2) {  //遍历emp元素下的子元素
                XMLUtil.analysisXML(e, classList, associationList, dependencyList);
            }
        }
        return dependencyList;
    }
}
