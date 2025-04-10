package softwaremetric.controller;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import softwaremetric.ast.ClassInfo;
import softwaremetric.ast.DemoVisitor;
import softwaremetric.ast.JdtAstUtil;
import softwaremetric.ast.MethodInfo;
import softwaremetric.common.CommonResponse;
import softwaremetric.common.FileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/AST")
public class ASTController {
	
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public CommonResponse upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "files", required = false) MultipartFile[] files,@RequestParam("dirName") String dirName) {

    	if (file.isEmpty()) {
            String msg = "上传失败，请选择文件";
            return CommonResponse.createForError(msg);
        }
        
    	List<MultipartFile> javaFiles = new ArrayList();
    	if(files!=null&&dirName!=null) {
    		for (MultipartFile afile : files) {
                javaFiles.add(afile);
            }

            // 存储所有类的解析结果
            DemoVisitor visitor = new DemoVisitor();

            // 遍历每个 .java 文件并解析
            for (MultipartFile javaFile : javaFiles) {
                CompilationUnit comp = JdtAstUtil.getCompilationUnit(javaFile);
                comp.accept(visitor);
            }

            // 获取解析结果
            Map<String, ClassInfo> classInfoMap = visitor.getClassInfoMap();

            // 将所有类的结果保存到一个 XML 文件中
            String outputFilePath = "D:/file/" + dirName + "AST.xml";
            saveToXML(classInfoMap, outputFilePath);
            CommonResponse  commonResponse= fileService.uploadFiles(file, outputFilePath);
            
            return commonResponse;
    	}else {
    		 CommonResponse  commonResponse= fileService.uploadFiles(file, null);       
             return commonResponse;
    	}
        
        
        
    }


    /**
     * 将所有类的 AST 结果保存为单个 XML 文件
     */
    private static void saveToXML(Map<String, ClassInfo> classInfoMap, String filePath) {
        try {
            // 检查并创建目标目录
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // 创建父目录
            }

            // 创建 DOM 文档
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            // 创建根节点
            Element root = document.createElement("ASTResults");
            document.appendChild(root);

            // 遍历所有类信息
            for (ClassInfo classInfo : classInfoMap.values()) {
                // 创建类节点
                Element classElement = document.createElement("Class");
                classElement.setAttribute("name", classInfo.getClassName());
                root.appendChild(classElement);

                // 添加字段
                for (String field : classInfo.getFields()) {
                    Element fieldElement = document.createElement("Field");
                    fieldElement.setTextContent(field);
                    classElement.appendChild(fieldElement);
                }

                // 添加方法
                for (MethodInfo methodInfo : classInfo.getMethods()) {
                    Element methodElement = document.createElement("Method");
                    methodElement.setAttribute("name", methodInfo.getMethodName());
                    classElement.appendChild(methodElement);

                    // 添加方法内部调用
                    for (String call : methodInfo.getCalls()) {
                        System.out.println(call);
                        Element callElement = document.createElement(call.startsWith("MethodCall:") ? "MethodCall" : "FieldUse");
                        callElement.setTextContent(call.substring(call.indexOf(":") + 2)); // 移除前缀
                        methodElement.appendChild(callElement);
                    }
                }
            }

            // 将 DOM 文档写入文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file); // 使用 File 对象
            transformer.transform(source, result);

            System.out.println("AST 结果已保存到: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
