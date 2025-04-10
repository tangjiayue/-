package softwaremetric.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private RestTemplate restTemplate;

    public CommonResponse uploadFiles(MultipartFile file, String outputFilePath) {
    	if (file.isEmpty()) {
            String msg = "上传失败，请选择文件";
            return CommonResponse.createForError(msg);
        }

        String filePath = "D:\\file\\";

        String fileName = file.getOriginalFilename();
        String fileStr = filePath + fileName;
        System.out.println(fileStr);
        File dest = new File(fileStr);
        try {
            file.transferTo(dest);      //保存指定文件到指定路径

        } catch (IOException e) {
            e.printStackTrace();
        }

        
        try {
        	// 构建请求体
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("file", fileStr); // 文件路径字符串
            body.add("codeFile", outputFilePath); // 输出文件路径字符串

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

            // 调用接口
            String url = "http://localhost:8080/xml/uploadxml";
            String result = restTemplate.postForObject(url, requestEntity, String.class);
            System.out.println("上传结果：" + result);

        }catch(Exception e) {
        	e.printStackTrace();
        	String msg = "上传失败，请选择文件";
            return CommonResponse.createForError(msg);
        }

        return CommonResponse.createForSuccess("上传XML文件成功");
    }
}