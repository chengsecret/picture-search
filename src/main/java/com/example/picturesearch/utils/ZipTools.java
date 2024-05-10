package com.example.picturesearch.utils;

import lombok.SneakyThrows;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author by yuanlai
 * @Date 2024/5/10 15:30
 * @Description: TODO
 * @Version 1.0
 */

public class ZipTools {

    // 假设的minio上传方法
    public static String uploadToMinIO(String imagePath, byte[] imageBytes) {
        // todo 实际的上传逻辑
        return "http://minio.server/" + imagePath; // 假设的返回URL
    }

    // 获取图片字节流的方法
    public static byte[] getImageBytes(String imagePath) throws IOException {
        // 将字符串路径转换为Path对象
        Path imageFilePath = Path.of(imagePath);

        // 确保文件存在
        if (!Files.exists(imageFilePath)) {
            throw new IOException("图片文件不存在: " + imagePath);
        }

        // 读取文件为字节数组
        return Files.readAllBytes(imageFilePath);
    }

    // 读取JSON数组的方法
    @SneakyThrows
    public static JSONArray readJsonArray(String jsonFilePath) throws IOException {
        // 使用BufferedReader读取文件内容
        StringBuilder jsonContentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContentBuilder.append(line);
            }
        }

        // 将读取到的字符串转换为JSONArray
        String jsonContent = jsonContentBuilder.toString();
        return new JSONArray(jsonContent);
    }

    // 假设的生成UUID方法
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // 解压ZIP文件到指定路径
    public static void unzip(MultipartFile file, Path destPath) throws IOException {
        if (file != null && !file.isEmpty()) {
            try (InputStream is = file.getInputStream();
                 ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is))) {

                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    Path entryPath = destPath.resolve(zipEntry.getName());
                    if (zipEntry.isDirectory()) {
                        // 创建目录
                        Files.createDirectories(entryPath);
                    } else {
                        // 创建父目录，确保文件的路径存在
                        Files.createDirectories(entryPath.getParent());
                        try (OutputStream out = Files.newOutputStream(entryPath)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                out.write(buffer, 0, len);
                            }
                        }
                    }
                    // 切换到下一个条目
                    zipEntry = zis.getNextEntry();
                    zis.closeEntry(); // 关闭当前条目
                }
            } catch (IOException e) {
                // Handle the exception
                e.printStackTrace();
            }
        }
    }


    public static void dealZip(MultipartFile datasource) {
        System.out.println("开始处理压缩文件");

        try {
            // 解压文件到uuid命名的临时文件夹中
            String tempFolderPath = generateUUID();
            Path tempFolderPathPath = Paths.get(tempFolderPath);
            // 确保临时文件夹存在
            Files.createDirectories(tempFolderPathPath);

            // 解压文件到临时文件夹
            unzip(datasource, tempFolderPathPath);
            // 检查临时文件夹中是否有description.json，如果没有就报错
            Path descriptionJsonPath = tempFolderPathPath.resolve("description.json");
            if (!Files.exists(descriptionJsonPath)) {
                throw new IOException("description.json 文件不存在");
            }

            // 读取description.json中的数据
            JSONArray jsonArray = readJsonArray(descriptionJsonPath.toString());

            // 循环遍历json数组获取对象
            for (int i = 0; i < jsonArray.length(); i++) {
                // 获取图片的路径和标签
                String imagePath = jsonArray.getJSONObject(i).getString("image");
                JSONArray tags = jsonArray.getJSONObject(i).getJSONArray("tag");
                List<String> stringList = new ArrayList<>();

                // 遍历JSONArray并将每个元素添加到ArrayList中
                for (int ii = 0; ii < tags.length(); ii++) {
                    stringList.add(tags.getString(ii));
                }
                // 获取图片的字节流
                byte[] imageBytes = getImageBytes(tempFolderPath+"/"+imagePath);

                // 上传图片到minio服务器，返回图片的url
                String imageUrl = uploadToMinIO(imagePath, imageBytes);

                // 打印图片的url和标签
                System.out.println("图片URL: " + imageUrl);
                System.out.println("标签: " + stringList);
            }

            // 清理临时文件夹（可选）
//             Files.walk(tempFolderPathPath)
//                 .forEach(path -> {
//                     try {
//                         Files.delete(path);
//                     } catch (IOException e) {
//                         e.printStackTrace();
//                     }
//                 });
//             Files.delete(tempFolderPathPath);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
