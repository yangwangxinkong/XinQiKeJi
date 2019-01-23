package top.zwwl.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import top.zwwl.management.dao.AdvertisingImgMapper;
import top.zwwl.management.pojo.AdvertisingImg;

@Controller
public class FileController {

    @Value("${MultipartFile.image.ref}")
    private String imageRef;

    @Autowired
    private AdvertisingImgMapper advertisingImgMapper;

    /*图片上传*/
    @PostMapping(value = "/fileUpload")
    public String fileUpload(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request) {
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        //将文件名转换成全小写
        fileName = fileName.toLowerCase();

        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String nameSuffix = fileName.substring(fileName.indexOf(".")+1);
        List<String> bcakgroundList = new ArrayList<>();
        bcakgroundList.add("jpg");
        bcakgroundList.add("png");
        bcakgroundList.add("jpeg");
        bcakgroundList.add("wmf");
        bcakgroundList.add("raw");
        //当图片类型不在集合中时
        if (!bcakgroundList.contains(nameSuffix)){
            model.addAttribute("imgAddress","上传文件类型不正确");
            return "index3";
        }

        String filePath = "H://testIMG/"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);

        HttpSession session = request.getSession();
        try {
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read == null) {
                // 证明上传的文件不是图片，获取图片流失败，不进行下面的操作
                model.addAttribute("imgAddress","上传的不是图片");
                return "index3";
            }
            session.setAttribute("width",read.getWidth());
            session.setAttribute("height",read.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String imgAddress = imageRef + "/" + fileName;
        model.addAttribute("imgAddress", imgAddress);
        return "index3";
    }

    @RequestMapping("/saveImg")
    public String saveImgAddress(HttpServletRequest request,String imgAddress,Integer imgType){
        try {
            //从session中获取图片的宽高
            HttpSession session = request.getSession();
            Integer width = (Integer)session.getAttribute("width");
            Integer height = (Integer)session.getAttribute("height");

            AdvertisingImg advertisingImg = new AdvertisingImg();
            advertisingImg.setWidth(width);
            advertisingImg.setHeight(height);
            advertisingImg.setImgType(imgType);
            advertisingImg.setImgAddress(imgAddress);
            int test = advertisingImgMapper.insertAdvertising(advertisingImg);

            request.setAttribute("message","图片保存成功了！");
            return "index3";
        }catch (Exception e){
            e.printStackTrace();
        }
        request.setAttribute("message","图片保存失败了！");
        return "失败了！";
    }
}

