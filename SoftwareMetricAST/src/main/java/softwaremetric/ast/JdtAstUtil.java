package softwaremetric.ast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.springframework.web.multipart.MultipartFile;
 
 
public class JdtAstUtil {
    /**
    * get compilation unit of source code
    * @param javaFilePath 
    * @return CompilationUnit
    */
	//CompilationUnit 代表一个 Java 编译单元，即一个 Java 源文件
	//输入参数为需解析的Java源代码文件路径，返回值为该文件对应的CompilationUnit节点
    public static CompilationUnit getCompilationUnit(MultipartFile javaFile){
    	//begin
    	//读取指定路径的 Java 源文件内容，并将其存储在字节数组 input 中，判断输入文件是否存在和可用
        byte[] input = null;
		try {
			InputStream inputStream = javaFile.getInputStream();
		    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		    //将源码存储在字节数组
		    input = new byte[bufferedInputStream.available()];
	            bufferedInputStream.read(input);
	            bufferedInputStream.close();
		} catch (FileNotFoundException e) {//异常检测
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//end
		
        //创建ASTParser并将Java源文件解析成AST:
		ASTParser astParser = ASTParser.newParser(AST.JLS17);
        astParser.setSource(new String(input).toCharArray());
        
        //设置要解析的类型，包含下面四种，可按需选择
        //K_COMPILATION_UNIT：编译单元，即一个Java文件
        //K_CLASS_BODY_DECLARATIONS：类的声明
        //K_EXPRESSION：单个表达式
        //K_STATEMENTS：语句块
//        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        
        astParser.setResolveBindings(true); // 启用绑定解析
        // 设置编译环境
        String[] classpath = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        String[] sources = {""}; // 源码路径（可以为空）
        astParser.setEnvironment(classpath, sources, null, true);
     // 设置编译单元名称（使用文件名作为编译单元名称）
        String fileName = javaFile.getName();
        astParser.setUnitName(fileName);

 
        //获取代码的AST，实现AST解析
        CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
        
        
        return result;
    }
    
}
 