package org.csu.softwaremetric.Utils;

import org.csu.softwaremetric.entity.*;
import org.dom4j.Attribute;
import org.dom4j.Document;

import org.dom4j.Element;

import org.dom4j.io.SAXReader;


import java.io.File;
import java.util.*;

public class XMLUtil {

    private static Classes targetClass;
    private static Association association;    //存储正在解析的关联
    private static Dependency dep;    //存储正在解析的依赖
    private static org.csu.softwaremetric.entity.Attribute att;    // 存储正在解析的属性
    private static Operation ope;    // 存储正在解析的方法


    public Document load(String filename) {
        Document document = null;
        System.out.println("Loading XML file: " + filename);
        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(new File(filename));
            System.out.println("读取成功");
        } catch (Exception e) {
            System.out.println("读取失败");
            e.printStackTrace();
        }
        return document;
    }

//    public String UmlToXml(String filename) {
//        //存储XML文档中所有元素ID
//        List<String> List_id = new ArrayList<String>();
//        //存储XML文档中对应ID元素的name
//        List<String> List_name = new ArrayList<String>();
//        //存储XML文档中所有的继承关系
//
//        //1.读取XML文件
//        System.out.println("读取.xml文件");
//        Document document = load(filename);
//
//        //2.获取文档根结点
//        Element rootElem = document.getRootElement();
//        // String value = rootElem.attributeValue("id");
//        //System.out.println("根结点ID："  + value);
//
//        //3.获取文档中所有packageElement即文档元素的ID和Name
//        List nodes = rootElem.elements("packagedElement");
//        for (Iterator it = nodes.iterator(); it.hasNext();) {
//            Element elm = (Element) it.next();
//            String id = elm.attributeValue("id");
//            String name = elm.attributeValue("name");
//            List_id.add(id);
//            List_name.add(name);
//        }
//
//        int count = 0;
//
//        try{
//            // 1、创建document对象
//            Document newXML = DocumentHelper.createDocument();
//            // 2、创建根节点temp
//            Element temp = newXML.addElement("rss");
//            // 3、向rss节点添加version属性
//            temp.addAttribute("version", "2.0");
//
//            for (Iterator it = nodes.iterator(); it.hasNext();) {
//                Element elm = (Element) it.next();
//                String elm_type = elm.attributeValue("type");      //type = uml:...
//                elm_type = elm_type.substring(4, elm_type.length());  //type : class/interface/realization/dependency
//                //如果是数据类型，则跳过本次循环
//                //System.out.println(elm_type);
//                if (elm_type.equals("PrimitiveType")) {
//                    continue;
//                }
//                String elm_id = elm.attributeValue("id");          //elm在xml文档中的编号
//                String elm_name = elm.attributeValue("name");      //elm在xml文档中的名字
//                String obj_count = Integer.toString(count);           //elm在存储xml文档中的编号
//                count++;
//                Element c = temp.addElement(elm_type);
//                c.addAttribute("Id", obj_count);
//                Element obj_name = c.addElement("objectName");
//                obj_name.setText(elm_name);
//                Element obj_id = c.addElement("objectId");
//                obj_id.setText(elm_id);
//                //如果是class or interface，则存在属性、方法
//                if (elm_type.equals("Class") || elm_type.equals("Interface")) {
//                    //System.out.println("类或接口：" + elm_type);
//                    //System.out.println(elm_name);
//
//                    //1. 属性
//                    List nodes_attr = elm.elements("ownedAttribute");
//                    //如果该类存在属性，则写入<attributes>...<attribute>
//                    if (nodes_attr.size() > 0) {
//                        Element attrs = c.addElement("attributes");
//                        for (Iterator it_attr = nodes_attr.iterator(); it_attr.hasNext();){
//                            Element attr = (Element) it_attr.next();
//                            Element attribute = attrs.addElement("attribute");
//
//                            //属性名
//                            Element attr_name = attribute.addElement("name");
//                            attr_name.setText(attr.attributeValue("name"));
//
//                            //属性类型
//                            String attr_type_id = attr.attributeValue("type");      //获取属性类型在xml中的ID
//                            //将ID转化为类型名称
//                            int tmp_id = List_id.indexOf(attr_type_id);
//
//                            String attr_type_name = List_name.get(tmp_id);
//                            Element attr_type = attribute.addElement("type");
//                            attr_type.setText(attr_type_name);
//
//                            //属性可见性
//                            Element attr_visibility = attribute.addElement("visibility");
//                            attr_visibility.setText(attr.attributeValue("visibility"));
//                        }
//                    }
//
//                    //2. 方法
//                    List nodes_method = elm.elements("ownedOperation");
//                    //如果该类存在方法，则写入<methods>...</methods>
//                    if (nodes_method.size() > 0) {
//                        Element methods = c.addElement("methods");
//                        for (Iterator it_method = nodes_method.iterator(); it_method.hasNext(); ) {
//                            Element mtd = (Element)it_method.next();
//                            Element method = methods.addElement("method");
//
//                            //方法名
//                            Element method_name = method.addElement("name");
//                            method_name.setText(mtd.attributeValue("name"));
//
//                            //方法可见性
//                            Element method_visibility = method.addElement("visibility");
//                            method_visibility.setText(mtd.attributeValue("visibility"));
//
//                            //方法参数和方法返回值类型
//                            List nodes_method_attr = mtd.elements("ownedParameter");
//                            if (nodes_method_attr.size() > 0) {
//                                Element method_attrs = method.addElement("attributes");
//                                for (Iterator it_method_attr = nodes_method_attr.iterator(); it_method_attr.hasNext(); ) {
//                                    Element mtd_attr = (Element) it_method_attr.next();
//                                    //System.out.println("返回值：" +mtd_attr.attributeValue("direction"));
//                                    //不是返回值属性
//                                    if (mtd_attr.attributeValue("direction") == null) {
//                                        //增加<attribute>...</attribute>标签
//                                        //System.out.println(mtd_attr.attributeValue("name"));
//                                        Element mtd_attribute = method_attrs.addElement("attribute");
//
//                                        //参数名<name>...</name>
//                                        Element mtd_attr_name = mtd_attribute.addElement("name");
//                                        mtd_attr_name.setText(mtd_attr.attributeValue("name"));
//
//                                        //类型<type>...</type>
//                                        String mtd_attr_type_id = mtd_attr.attributeValue("type");
//                                        //将ID转换为类型名称
//                                        int tmp_id = List_id.indexOf(mtd_attr_type_id);
//                                        String mtd_attr_type_name = List_name.get(tmp_id);
//                                        Element mtd_attr_type = mtd_attribute.addElement("type");
//                                        mtd_attr_type.setText(mtd_attr_type_name);
//                                    }
//                                    else {
//                                        //返回值类型<returnType>...</returnType>
//                                        Element mtd_returnType = method.addElement("returnType");
//                                        int tmp_id = List_id.indexOf(mtd_attr.attributeValue("type"));
//                                        String mtd_return_type_name = List_name.get(tmp_id);
//                                        mtd_returnType.setText(mtd_return_type_name);
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    //generation
//                    Element generalization = elm.element("generalization");
//                    if (generalization != null) {
//                        Element general = c.addElement("general");
//
//                        //System.out.println(elm_name + " " +generalization.attributeValue("general"));
//                        int tmp_id = List_id.indexOf(generalization.attributeValue("general"));
//                        String tmp_general = List_name.get(tmp_id);
//                        general.setText(tmp_general);
//                    }
//                }
//                //如果是Association
//                else if (elm_type.equals("Association")) {
//                    //System.out.println("关联关系：" + elm_type);
//                    //System.out.println(elm_name);
//
//                    //association: <association>...</association>
//                    Element association = c.addElement("association");
//                    List nodes_property = elm.elements("ownedEnd");
//                    int tmp_flag = 0;
//                    for (Iterator it_association = nodes_property.iterator(); it_association.hasNext(); ) {
//                        Element asso_property = (Element) it_association.next();
//                        int tmp_id = List_id.indexOf(asso_property.attributeValue("type"));
//                        String tmp_property = List_name.get(tmp_id);
//                        Element property = association.addElement("property");
//                        property.setText(tmp_property);
//                        if (tmp_flag == 1) {
//                            Element aggregation = association.addElement("aggregation");
//                            if (asso_property.attributeValue("aggregation") != null) {
//                                aggregation.setText(asso_property.attributeValue("aggregation"));
//                            }
//                            else{
//                                aggregation.setText("association");
//                            }
//                        }
//                        tmp_flag ++;
//                    }
//                }
//                //如果是Dependency or Realization
//                else {
//                    //System.out.println("依赖/实现/继承:" + elm_type);
//                    //System.out.println(elm_name);
//
//                    //supplier: <supplier>...</supplier>
//                    Element supplier = c.addElement("supplier");
//                    //System.out.println(elm.attributeValue("supplier"));
//                    int tmp_id = List_id.indexOf(elm.attributeValue("supplier"));
//                    String supplier_name = List_name.get(tmp_id);
//                    supplier.setText(supplier_name);
//
//                    //client: <client>...</client>
//                    Element client = c.addElement("client");
//                    //System.out.println(elm.attributeValue("client"));
//                    tmp_id = List_id.indexOf(elm.attributeValue("client"));
//                    String client_name = List_name.get(tmp_id);
//                    client.setText(client_name);
//                }
//            }
//
//            // 5、设置生成xml的格式
//            OutputFormat format = OutputFormat.createPrettyPrint();
//            // 设置编码格式
//            format.setEncoding("UTF-8");
//
//
//            // 6、生成xml文件
//            //获取当前时间进行命名
//            //获取当前时间
//            SimpleDateFormat df = new SimpleDateFormat("yyyyMMDDmmss");
//            String tmp_fileName = df.format(new Date());
//            String fileName = tmp_fileName + ".xml";
//
//            File file = new File(fileName);
//
//            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
//            // 设置是否转义，默认使用转义字符
//            //writer.setEscapeText(false);
//            writer.write(newXML);
//            writer.close();
//            System.out.println("生成"+ fileName + "成功");
//            return fileName;
//        } catch (Exception ex){
//            ex.printStackTrace();
//            System.out.println("生成XML文件失败");
//        }
//        return "false";
//    }

    public static String getType(Element element){
        Attribute typeAttribute = element.attribute("type");
        String typeValue = typeAttribute.getValue();
        return typeValue;
    }
    public static String getName(Element element){
        Attribute nameAttribute = element.attribute("name");
        String nameValue = nameAttribute.getValue();
        return nameValue;
    }
    public static String getID(Element element){
        Attribute idAttribute = element.attribute("id");
        String idValue = idAttribute.getValue();
        return idValue;
    }

    public static String getVisibility(Element element){
        Attribute visibilityAttribute = element.attribute("visibility");
        String visibilityValue = visibilityAttribute.getValue();
        return visibilityValue;
    }

    public static String getGeneral(Element element){
        Attribute generalAttribute = element.attribute("general");
        String generalValue = generalAttribute.getValue();
        return generalValue;
    }
    public static void analysisXML(Element element, List<Classes> classList, List<Association> associationList, List<Dependency> dependencyList){

        if(element.getName().equals("packagedElement")){    // 判断节点是否为类
            if(getType(element).equals("uml:Class")){
                targetClass = new Classes();
                targetClass.setName(getName(element));
                targetClass.setId(getID(element));
                classList.add(targetClass);
            }else if(getType(element).equals("uml:Association")){   // 判断节点是否存在关联关系
                association = new Association();
                associationList.add(association);
            }else if(getType(element).equals("uml:Dependency")){    // 判断节点是否存在依赖关系
                dep = new Dependency();
                dependencyList.add(dep);
            }
        }else if (element.getName().equals("ownedAttribute")) {     // 判断节点是否为属性
            att = new org.csu.softwaremetric.entity.Attribute();
            att.setName(getName(element));
            att.setVisibility(getVisibility(element));
            targetClass.addAttribute(att);

        } else if (element.getName().equals("ownedOperation")) {    // 判断节点是否为方法
            ope = new Operation();
            ope.setName(getName(element));
            ope.setVisibility(getVisibility(element));
            targetClass.addOperation(ope);

        } else if(element.getName().equals("generalization")){
            targetClass.setGeneralization(getGeneral(element));

        }
    }

    public void clean(List<Classes> list1){
        for (int i = 0; i < list1.size(); i++) {
            Classes s = list1.get(i);
            for(int j = 0; j < list1.size(); j++){
                Classes tempClass = list1.get(j);
                if(tempClass.getId().equals(s.getGeneralization())){
                    list1.get(j).addChildren(s);
                    list1.get(i).setFather(tempClass);
                }
            }
        }
    }

    /**
     * 解析 Classes 节点
     */
    public static Map<String,Classes> parseClasses(List<Element> classElements) {
        Map<String,Classes> classMap = new HashMap<>();

        for (Element classElement : classElements) {
            Classes clazz = new Classes();
            clazz.setId(classElement.attributeValue("Id"));
            clazz.setName(classElement.elementText("Name"));

            //解析 Attributes
            List<Element> attributesElements = classElement.element("Attributes")==null?new ArrayList<>():classElement.element("Attributes").elements("Attribute");
            if (!attributesElements.isEmpty()) {
                for (Element attributesElement : attributesElements) {
                    org.csu.softwaremetric.entity.Attribute attribute = new org.csu.softwaremetric.entity.Attribute();
                    attribute.setName(attributesElement.elementText("Name"));
                    attribute.setVisibility(attributesElement.elementText("Attribute.Visibility"));

                    clazz.addAttribute(attribute);
                }
            }

            // 解析 Operations
            List<Element> operationsElements = classElement.element("Operations")==null?new ArrayList<>():classElement.element("Operations").elements("Operation");
            if (!operationsElements.isEmpty()) {
                for (Element operationElement : operationsElements) {
                    Operation operation = new Operation();
                    operation.setName(operationElement.elementText("Name"));
                    operation.setReturnType(operationElement.elementText("ReturnType"));

                    // 解析 Parameters
                    List<Element> parametersElements = operationElement.element("Parameters")==null?new ArrayList<>(): operationElement.element("Parameters").elements("Parameter");
                    List<Parameter> Parameters = new ArrayList<>();
                    for (Element parameterElement : parametersElements) {
                        Parameter p = new Parameter();
                        p.setName(parameterElement.elementText("Name"));
                        p.setClassName(parameterElement.elementText("Parameter.DataType"));
                        Parameters.add(p);
                    }
                    operation.setParameters(Parameters);

                    clazz.addOperation(operation);
                }
            }

            classMap.put(clazz.getId(), clazz);
        }

        return classMap;
    }

    /**
     * 解析 Interfaces 节点
     */
    public static Map<String,Interface> parseInterface(List<Element> interfaceElements) {
        Map<String,Interface> interfaceMap = new HashMap<>();

        for (Element interfaceElement : interfaceElements) {
            Interface interFace = new Interface();
            interFace.setId(interfaceElement.attributeValue("Id"));
            interFace.setName(interfaceElement.elementText("Name"));

            // 解析 Operations
            List<Element> operationsElements = interfaceElement.element("Operations")==null?new ArrayList<>(): interfaceElement.element("Operations").elements("Operation");
            if (!operationsElements.isEmpty()) {
                for (Element operationElement : operationsElements) {
                    Operation operation = new Operation();
                    operation.setName(operationElement.elementText("Name"));

                    // 解析 Parameters
                    List<Element> parametersElements = operationElement.element("Parameters")==null?new ArrayList<>(): operationElement.element("Parameters").elements("Parameter");
                    List<Parameter> Parameters = new ArrayList<>();
                    for (Element parameterElement : parametersElements) {
                        Parameter p = new Parameter();
                        p.setName(parameterElement.elementText("Name"));
                        p.setClassName(parameterElement.elementText("Parameter.DataType"));
                        Parameters.add(p);
                    }
                    operation.setParameters(Parameters);

                    interFace.addOperation(operation);
                }
            }

            interfaceMap.put(interFace.getId(), interFace);
        }

        return interfaceMap;
    }

    /**
     * 解析 Realizations 节点
     */
    public static void parseRealizations(List<Element> realizationElements, Map<String, Classes> classMap, Map<String, Interface> interfaceMap) {
        for (Element realizationElement : realizationElements) {
            String parentId = realizationElement.element("Object1").element("Interface").attributeValue("Ref");
            String childId = realizationElement.element("Object2").element("Class").attributeValue("Ref");

            Interface parentInterface = interfaceMap.get(parentId);
            Classes childclass = classMap.get(childId);

            if (parentInterface != null && childclass != null) {
                parentInterface.addImplementation(childclass);
                childclass.addImplementation(parentInterface);
            }
        }
    }

    /**
     * 解析 Generalizations 节点
     */
    public static void parseGeneralizations(List<Element> generalizationElements, Map<String, Classes> classMap,Map<String, Interface> interfaceMap) {
        for (Element generalizationElement : generalizationElements) {         //类的继承
            if(generalizationElement.element("Object1").element("Class")!=null){
                String parentId = generalizationElement.element("Object1").element("Class").attributeValue("Ref");
                String childId = generalizationElement.element("Object2").element("Class").attributeValue("Ref");

                Classes parentClass = classMap.get(parentId);
                Classes childClass = classMap.get(childId);

                if (parentClass != null && childClass != null) {
                    parentClass.addChildren(childClass);
                    childClass.setFather(parentClass);
                    childClass.setGeneralization(parentId);
                }
            }else {   //接口的继承
                String parentId = generalizationElement.element("Object1").element("Interface").attributeValue("Ref");
                String childId = generalizationElement.element("Object2").element("Interface").attributeValue("Ref");

                Interface parentInterface = interfaceMap.get(parentId);
                Interface childInterface = interfaceMap.get(childId);

                if (parentInterface != null && childInterface != null) {
                    parentInterface.addChildren(childInterface);
                    childInterface.addFather(parentInterface);
                }
            }

        }
    }

    /**
     * 解析 Dependencies 节点
     */
    public static void parseDependencies(List<Element> dependencieElements, Map<String, Classes> classMap,Map<String, Interface> interfaceMap) {
        for (Element dependencieElement : dependencieElements) {
            if(dependencieElement.element("Object1").element("Class")==null){
                String parentId = dependencieElement.element("Object1").element("Interface").attributeValue("Ref");
                String childId = dependencieElement.element("Object2").element("Class").attributeValue("Ref");

                Interface parentInterface = interfaceMap.get(parentId);
                Classes childclass = classMap.get(childId);

                if (parentInterface != null && childclass != null) {
                    parentInterface.addDependencies(childId);
                    childclass.addDependencies(parentId);
                }
            }else {
                String parentId = dependencieElement.element("Object1").element("Class").attributeValue("Ref");
                String childId = dependencieElement.element("Object2").element("Class").attributeValue("Ref");

                Classes parentclass = classMap.get(parentId);
                Classes childclass = classMap.get(childId);

                if (parentclass != null && childclass != null) {
                    parentclass.addDependencies(childId);
                    childclass.addDependencies(parentId);
                }
            }


        }
    }

    /**
     * 解析 Associations 节点
     */
    public static void parseAssociations(List<Element> associationElements, Map<String, Classes> classMap,Map<String, Interface> interfaceMap) {
        for (Element dependencieElement : associationElements) {
            if(dependencieElement.element("Object1").element("Class")==null){
                String parentId = dependencieElement.element("Object1").element("Interface").attributeValue("Ref");
                if(dependencieElement.element("Object2").element("Class")==null){
                    String childId = dependencieElement.element("Object2").element("Interface").attributeValue("Ref");

                    Interface parentInterface = interfaceMap.get(parentId);
                    Interface childInterface = interfaceMap.get(childId);

                    if (parentInterface != null && childInterface != null) {
                        parentInterface.addAssociation(childId);
                        childInterface.addAssociation(parentId);
                    }
                }else {
                    String childId = dependencieElement.element("Object2").element("Class").attributeValue("Ref");

                    Interface parentInterface = interfaceMap.get(parentId);
                    Classes childclass = classMap.get(childId);

                    if (parentInterface != null && childclass != null) {
                        parentInterface.addAssociation(childId);
                        childclass.addAssociation(parentId);
                    }
                }

            }else {
                String parentId = dependencieElement.element("Object1").element("Class").attributeValue("Ref");
                if(dependencieElement.element("Object2").element("Class")==null){
                    String childId = dependencieElement.element("Object2").element("Interface").attributeValue("Ref");

                    Classes parentclass = classMap.get(parentId);
                    Interface childInterface = interfaceMap.get(childId);

                    if (parentclass != null && childInterface != null) {
                        parentclass.addAssociation(childId);
                        childInterface.addAssociation(parentId);
                    }
                }else {
                    String childId = dependencieElement.element("Object2").element("Class").attributeValue("Ref");

                    Classes parentclass = classMap.get(parentId);
                    Classes childclass = classMap.get(childId);

                    if (parentclass != null && childclass != null) {
                        parentclass.addAssociation(childId);
                        childclass.addAssociation(parentId);
                    }
                }


            }

        }
    }

    /**
     * 解析源代码AST抽象语法树
     */
    public static void parseCodeClassInfo(List<Element> codeClassInfo, Map<String, Classes> classMap) {
        for (Element classInfo : codeClassInfo) {
            String className = classInfo.attributeValue("name");
            Classes classes = null;
            for(Map.Entry<String, Classes> entry : classMap.entrySet()) {
                if(entry.getValue().getName().equals(className)) {
                    classes = entry.getValue();
                    break;
                }
            }

            if(classes!=null){
                List<Element> methodElements = classInfo.elements("Method")==null? new ArrayList<>() :classInfo.elements("Method");
                //计算调用方法总数
                for(Element methodElement : methodElements) {
                    List<Element> callMethods = methodElement.elements("MethodCall")==null? new ArrayList<>() :methodElement.elements("MethodCall");
                    classes.addCallMethodNumber(callMethods.size());
                }

                //计算P和Q
                int P=0,Q=0;
                for (int i = 0; i < methodElements.size(); i++) {
                    for (int j = i + 1; j < methodElements.size(); j++) {
                        List<Element> FieldUse1 = methodElements.get(i).elements("FieldUse")==null? new ArrayList<>() :methodElements.get(i).elements("FieldUse");
                        List<Element> FieldUse2 = methodElements.get(j).elements("FieldUse")==null? new ArrayList<>() :methodElements.get(j).elements("FieldUse");

                        // 获取两个方法的字段集合
                        Set<String> fields1= new HashSet<>();
                        Set<String> fields2= new HashSet<>();
                        for(Element fieldElement : FieldUse1) {
                            fields1.add(fieldElement.getText());
                        }
                        for(Element fieldElement : FieldUse2) {
                            fields2.add(fieldElement.getText());
                        }

                        // 检查是否有共享字段
                        boolean hasSharedFields = !Collections.disjoint(fields1, fields2); // 判断两个集合是否有交集

                        if (hasSharedFields) {
                            Q++; // 共享至少一个字段
                        } else {
                            P++; // 不共享任何字段
                        }
                    }
                }

                classes.setP(P);
                classes.setQ(Q);

            }

        }
    }

}
