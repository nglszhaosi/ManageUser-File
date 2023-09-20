package com.liu.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.entity.Files;
import com.liu.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Value("${server.ip}")
    private String serverIp;

    @Resource
    private FileMapper fileMapper;


    /**
     * 文件上传接口
     *
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        //先存储到磁盘
        File uploadParantFile = new File(fileUploadPath);
        if (!uploadParantFile.exists()) {
            uploadParantFile.mkdirs();
        }
        //生成一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUUID);

        System.out.println("uploadFile==>" + uploadFile);
        System.out.println("fileUploadPath + fileUUID===>" + fileUploadPath + fileUUID);
        System.out.println("fileUploadPath===>" + fileUploadPath);

        //获取文件的url
        String url = "";
        //获取当前文件的md5,
        String md5 = SecureUtil.md5(file.getInputStream());
        List<Files> dbFile = getFileByMd5(md5);//通过md5查询文件

        if (dbFile.size() != 0) {//如果存在的话，就通过md5查询已存文件的url，并返回
            QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
            QueryWrapper<Files> existingFile = queryWrapper.eq("md5", md5);
            System.out.println("existingFile==>"+existingFile);

            System.out.println("fileMapper.selectList(existingFile)==>"+fileMapper.selectList(existingFile));
            url = fileMapper.selectList(existingFile).get(0).getUrl();


        } else {//如果文件不存在，就把文件的相关信息存到数据库
            file.transferTo(uploadFile);//上传文件到磁盘
            url = "http://"+serverIp+":9090/file/" + fileUUID; //生成文件的url
        }
        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * 文件下载接口  http://localhost:9090/file/{fileUUID}
     *
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过md5查询文件
     *
     * @param md5
     * @return
     */
    private List<Files> getFileByMd5(String md5) {
        //查询md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        return fileMapper.selectList(queryWrapper);
    }

    //查询所有文件信息
    @GetMapping
    public List<Files> findAll() {
        return fileMapper.selectList(null);
    }

    //分页
    @GetMapping("/page")
    public Page<Files> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Files> fileQueryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            fileQueryWrapper.like("name", name).select("is_delete", "0");
        }

        fileQueryWrapper.eq("is_delete", 0).orderByDesc("id");
        return fileMapper.selectPage(new Page<>(pageNum, pageSize), fileQueryWrapper);
    }


    //启用禁用接口
    @PostMapping("/update")
    public int save(@RequestBody Files files) {
        return fileMapper.updateById(files);
    }


    //删除
    @DeleteMapping("/{id}")
    public int delete(@PathVariable Integer id) {
        Files file = fileMapper.selectById(id);
        file.setIs_delete(true);
        int is_del = fileMapper.updateById(file);
        return is_del;
    }


    //批量删除数据
    @PostMapping("/del/batch")
    public int deleteBath(@RequestBody List<Integer> ids) {
        int total_del = 0;//共删除几个文件
        for (Integer id : ids) {
            Files file = fileMapper.selectById(id);
            file.setIs_delete(true);
            int is_del = fileMapper.updateById(file);
            total_del = total_del + is_del;
        }
        return total_del;
    }


}
