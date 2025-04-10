package org.csu.softwaremetric.controller;

import org.csu.softwaremetric.Utils.Method;
import org.csu.softwaremetric.common.CommonResponse;
import org.csu.softwaremetric.entity.BasicInfo;
import org.csu.softwaremetric.entity.CK;
import org.csu.softwaremetric.entity.Classes;
import org.csu.softwaremetric.entity.LK;
import org.csu.softwaremetric.service.InfoService;
import org.csu.softwaremetric.service.XMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/xml")
public class XMLController {

    File dest;
    File codeDest;
    @Autowired
    XMLService xmlService;
    @Autowired
    InfoService infoService;
    List<CK> ckList = null;
    List<LK> lkList = null;

    @PostMapping("/uploadxml")
    public CommonResponse uploadXML(@RequestParam("file") String filePath, @RequestParam(value = "codeFile", required = false) String codeFilePath) {
        System.out.println(filePath);
        System.out.println(codeFilePath);
        if (filePath==null) {
            String msg = "上传失败，请选择文件";
            return CommonResponse.createForError(msg);
        }
        dest = new File(filePath);

        if(codeFilePath!=null){
            codeDest = new File(codeFilePath);
        }


        return CommonResponse.createForSuccess("上传XML文件成功");
    }

//    @PostMapping("/uploadxml")
//    public CommonResponse uploadXML(@RequestParam("file") MultipartFile file, @RequestParam(value = "code", required = false) MultipartFile codeFile) {
//
//        if (file.isEmpty()) {
//            String msg = "上传失败，请选择文件";
//            return CommonResponse.createForError(msg);
//        }
//
//        String filePath = "D:\\file\\";
//
//        String fileName = file.getOriginalFilename();
//        String fileStr = filePath + fileName;
//        System.out.println(fileStr);
//        dest = new File(fileStr);
//        try {
//            file.transferTo(dest);      //保存指定文件到指定路径
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if(codeFile!=null){
//            String codeFileName = codeFile.getOriginalFilename();
//            String codeFileStr = filePath + codeFileName;
//            System.out.println(codeFileStr);
//            codeDest = new File(codeFileStr);
//            try {
//                codeFile.transferTo(codeDest);      //保存指定文件到指定路径
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return CommonResponse.createForSuccess("上传XML文件成功");
//    }


    @RequestMapping("/getBasicInfo")
    public CommonResponse<List<BasicInfo>> GetBasicInfo() {
        String filename = String.valueOf(dest);
        List<Classes> list = XMLService.getMethod(filename);
        List<BasicInfo> basicList = infoService.getInfo(list);

        return CommonResponse.createForSuccess(basicList);
    }

    @RequestMapping("/getCKvalue")
    public CommonResponse GetCKvalue() {
        String filename = String.valueOf(dest);
        List<Classes> list;
        if(codeDest!=null){
            String codeFilename = String.valueOf(codeDest);
            list = XMLService.getMethod(filename,codeFilename);
        }else {
            list = XMLService.getMethod(filename);
        }

        ckList = new ArrayList<>();
        lkList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Classes s = list.get(i);

            Method ck = new Method(s, list);
            CK ckres = ck.getCk();
            ckres.setName(s.getName());
            ckList.add(ckres);

            Method Lk = new Method(s, list);
            LK lkres = Lk.getLk();
            lkres.setName(s.getName());
            lkList.add(lkres);

            System.out.println(ckres.getName()+" "+ckres.getWMC()+" "+ckres.getRFC()+" "+ckres.getDIT()+" "+ckres.getNOC()+" "+ckres.getCBO()+" "+ckres.getLCOM());
        }
        List<CK> ckList1 = ckList;
        ckList=null;

        return CommonResponse.createForSuccess(ckList1);
    }

    @RequestMapping("/getLKvalue")
    public CommonResponse GetLKvalue() {
//        String filename = String.valueOf(dest);
//        lkList = new ArrayList<>();
//        List<Classes> list;
//
//        if(codeDest!=null){
//            String codeFilename = String.valueOf(codeDest);
//            list = XMLService.getMethod(filename,codeFilename);
//        }else {
//            list = XMLService.getMethod(filename);
//        }
//
//        for (int i = 0; i < list.size(); i++) {
//            Classes s = list.get(i);
//            Method Lk = new Method(s, list);
//            LK lkres = Lk.getLk();
//            lkres.setName(s.getName());
//            lkList.add(lkres);
//        }

        List<LK> lkList1 = lkList;
        lkList=null;

        return CommonResponse.createForSuccess(lkList1);
    }


}
