package top.zwwl.management.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class FileController {

    @Value("${MultipartFile.image.ref}")
    private String imageRef;

    /*图片上传*/
    @PostMapping(value = "/fileUpload")
    public String fileUpload(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request) {
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名

        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String nameSuffix = fileName.substring(fileName.indexOf("."));
        List<String> bcakgroundList = new ArrayList<>();
        bcakgroundList.add("jpg");
        if (!bcakgroundList.contains(nameSuffix)){
            return "index3";
        }

        String filePath = "H://testIMG/"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = imageRef + "/" + fileName;
        model.addAttribute("filename", filename);
        return "index3";
    }
}
