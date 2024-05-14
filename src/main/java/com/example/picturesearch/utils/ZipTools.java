package com.example.picturesearch.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.example.picturesearch.dto.DatasetUploadDTO;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author by yuanlai
 * @Date 2024/5/10 15:30
 * @Description: TODO
 * @Version 1.0
 */


public class ZipTools {


    @SneakyThrows
    public static String uploadToMinIO(String imagePath){
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://10.4.177.198:39000/")
                .credentials("eEE7Bu5pGfkGFBeyLxcQ", "2UkCz7Qds640I7nOkPhRXsxsN1Dx3weWM8CK6WVG")
                .build();

        // 创建 FileInputStream 实例
        try (FileInputStream stream = new FileInputStream(imagePath)) {
            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("sanhui")
                            .object(imagePath)
                            .stream(stream, new File(imagePath).length(), -1)
                            .build()
            );
        }

        return "http://10.4.177.198:39000/sanhui/" + imagePath; // 假设的返回URL
    }

    @SneakyThrows
    public static String uploadToMinIO(MultipartFile picture){
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://10.4.177.198:39000/")
                .credentials("eEE7Bu5pGfkGFBeyLxcQ", "2UkCz7Qds640I7nOkPhRXsxsN1Dx3weWM8CK6WVG")
                .build();

        String imagePath = picture.getOriginalFilename();
        // 创建 FileInputStream 实例
        try (InputStream stream = picture.getInputStream()) {
            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("sanhui")
                            .object(imagePath)
                            .stream(stream, picture.getSize(), -1)
                            .build()
            );
        }

        return "http://10.4.177.198:39000/sanhui/" + imagePath; // 假设的返回URL
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
        return FileUtil.readBytes(imageFilePath);
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

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = new FileOutputStream(convFile)) {
            int byteRead = 0;
            byte[] buffer = new byte[1024];
            while ((byteRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteRead);
            }
        }
        return convFile;
    }

    @SneakyThrows
    public static DatasetUploadDTO dealZip(MultipartFile datasource) {
        System.out.println("开始处理压缩文件");
        DatasetUploadDTO datasetUploadDTO = new DatasetUploadDTO();
        try {
            // 解压文件到uuid命名的临时文件夹中
            String dataset = Objects.requireNonNull(datasource.getOriginalFilename()).split("\\.")[0];
            datasetUploadDTO.setDatasetName(dataset);

            String tempFolderPath = generateUUID();
            File targetFile = new File(tempFolderPath);
            File zipFile = convert(datasource);
            Path tempFolderPathPath = Paths.get(tempFolderPath);
            // 确保临时文件夹存在
            Files.createDirectories(tempFolderPathPath);

            // 解压文件到临时文件夹
            ZipUtil.unzip(zipFile, targetFile);
            FileUtil.del(zipFile);
            // 检查临时文件夹中是否有description.json，如果没有就报错
            Path descriptionJsonPath = tempFolderPathPath.resolve("description.json");
            if (!Files.exists(descriptionJsonPath)) {
                throw new IOException("description.json 文件不存在");
            }

            // 读取description.json中的数据
            JSONArray jsonArray = readJsonArray(descriptionJsonPath.toString());

            // 循环遍历json数组获取对象
            Set<String> categories = new HashSet<>();
            Map<String, List<String>> pictures = new HashMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                // 获取图片的路径和标签
                String imagePath = tempFolderPath + "/" + jsonArray.getJSONObject(i).getString("image");

                JSONArray tags = jsonArray.getJSONObject(i).getJSONArray("tag");
                List<String> stringList = new ArrayList<>();

                // 遍历JSONArray并将每个元素添加到ArrayList中
                for (int j = 0; j < tags.length(); j++) {
                    stringList.add(tags.getString(j));
                    categories.add(tags.getString(j));
                }
                // 获取图片的字节流
//                byte[] imageBytes = getImageBytes(imagePath);

                // 上传图片到minio服务器，返回图片的url
                String imageUrl = uploadToMinIO(imagePath);
                pictures.put(imageUrl, stringList);
                // 打印图片的url和标签
                System.out.println("图片URL: " + imageUrl);
                System.out.println("标签: " + stringList);

            }
            datasetUploadDTO.setCategories(categories);
            datasetUploadDTO.setPictures(pictures);
            // 清理临时文件夹
            FileUtil.del(tempFolderPathPath);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return datasetUploadDTO;
    }
}
