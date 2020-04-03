package com.emall.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class UploadUtil {
    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 上传图片到Ftp服务器
     *
     * @param imageFile
     * @param detailFile
     * @param path
     * @return
     */
    @Transactional
    public List<String> uploadToServer(MultipartFile imageFile, MultipartFile detailFile, String path) {
        String tmpPath = "E:/ImageTemp" + path;

        File dir = new File(tmpPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FTPClient ftp = new FTPClient();

        List<String> urlList = new ArrayList<>();
        InputStream imageLocal = null;
        InputStream detailLocal = null;
        try {
            ftp.connect("192.168.153.130", 21);
            ftp.login("ftpadmin", "123456");
            ftp.enterLocalPassiveMode();
            String ftpPath = "/home/ftpadmin/emall/images/";

            Date currentDate = new Date();
            String dateStr = new SimpleDateFormat("yyyy/MM/dd").format(currentDate);
            for (String pathStr : dateStr.split("/")) {
                ftpPath += pathStr + "/";
                boolean flag = ftp.changeWorkingDirectory(ftpPath);
                if (!flag) {
                    ftp.makeDirectory(ftpPath);
                }
            }
            //指定上传路径
            ftp.changeWorkingDirectory(ftpPath);
            //指定上传文件的类型  二进制文件
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            if (imageFile != null) {
                imageLocal = upload(imageFile, path, tmpPath, ftpPath, ftp, urlList);
            }

            if (detailFile != null) {
                detailLocal = upload(detailFile, path, tmpPath, ftpPath, ftp, urlList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            File del = new File(tmpPath);
            delAllFile(del);

            try {
                if (imageLocal != null) {
                    imageLocal.close();
                }
                if (detailLocal != null) {
                    detailLocal.close();
                }
                //退出
                ftp.logout();
                //断开连接
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return urlList;
    }

    /**
     * 文件上传操作
     *
     * @param clientFile
     * @param path
     * @param tmpPath
     * @param ftpPath
     * @param ftp
     * @param urlList
     * @return
     */
    @Transactional
    public InputStream upload(MultipartFile clientFile, String path, String tmpPath, String ftpPath, FTPClient ftp, List<String> urlList) {
        InputStream inputLocal = null;

        try {
            //文件名称
            String clientFileName = clientFile.getOriginalFilename();
            //临时图片文件路径
            File client = new File(path, Objects.requireNonNull(clientFileName));

            //浏览器端上传文件到临时文件夹
            clientFile.transferTo(client);

            //临时文件
            File tmp = new File(tmpPath + clientFileName);
            //ftp流
            inputLocal = new FileInputStream(tmp);
            //文件新名称
            String newFileName = getFileName(clientFileName);
            //临时文件再上传到图片服务器
            ftp.storeFile(newFileName, inputLocal);
            //图片在服务器上的url
            String url = ftpPath.replace("/home/ftpadmin/emall", "http://192.168.153.130") + newFileName;
            urlList.add(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputLocal;
    }

    /**
     * 随机生成文件名称
     *
     * @param primitiveFileName
     * @return
     */
    public String getFileName(String primitiveFileName) {
        //使用uuid生成文件名
        String fileName = String.valueOf(snowflakeIdWorker.nextId());
        //获取文件后缀
        String suffix = primitiveFileName.substring(primitiveFileName.lastIndexOf("."));

        return fileName + suffix;
    }

    /**
     * 删除临时文件夹下所有文件
     *
     * @param dir
     */
    public void delAllFile(File dir) {
        if (!dir.exists()) {
            return;
        } else if (!dir.isDirectory()) {
            return;
        }

        String[] tempList = dir.list();
        if (tempList == null) {
            return;
        }

        File temp;
        String path = dir.getPath();
        for (String s : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + s);
            } else {
                temp = new File(path + File.separator + s);
            }
            temp.delete();
        }
    }
}
