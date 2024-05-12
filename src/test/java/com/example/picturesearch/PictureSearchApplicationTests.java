package com.example.picturesearch;

import com.example.picturesearch.entity.Category;
import com.example.picturesearch.entity.Picture;
import com.example.picturesearch.entity.PictureCategory;
import com.example.picturesearch.mapper.CategoryMapper;
import com.example.picturesearch.mapper.PictureCategoryMapper;
import com.example.picturesearch.mapper.PictureMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PictureSearchApplicationTests {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    PictureMapper pictureMapper;
    @Autowired
    PictureCategoryMapper pictureCategoryMapper;
    @Test
    void contextLoads() {
    }

    @Test
    public void load() {
        String filePath = "D:\\data\\数据集\\annotations_trainval2017\\annotations\\instances_val2017.json";
        try (Reader reader = new FileReader(filePath)) {
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(reader).getAsJsonObject();
            System.out.println("info:");
            System.out.println(root.get("info"));
            System.out.println("categories:");
            System.out.println(root.get("categories"));
            JsonArray images = root.getAsJsonArray("images");
            System.out.println("Length of images: " + images.size());
            System.out.println(images.get(0));
            JsonArray annotations = root.getAsJsonArray("annotations");
            System.out.println("Length of annotations: " + annotations.size());
            System.out.println(annotations.get(0));

            savePictures(images);
            saveCategories(root.get("categories"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePictures(JsonArray images) {
        List<Picture> pictureCategories = new ArrayList<>();
        for (JsonElement image : images) {
            Picture picture = new Picture(
                    null,
                    image.getAsJsonObject().get("file_name").getAsString(),
                    image.getAsJsonObject().get("coco_url").getAsString(),
                    image.getAsJsonObject().get("id").getAsLong(), null, null);
            pictureCategories.add(picture);
        }
        pictureMapper.insertBatch(pictureCategories);
    }

    private void saveCategories(JsonElement categories) {
        List<Category> pictureCategories = new ArrayList<>();
        categories.getAsJsonArray().forEach(cate -> {
            Category category = new Category(
                    null,
                    cate.getAsJsonObject().get("supercategory").getAsString(),
                    cate.getAsJsonObject().get("name").getAsString(),
                    cate.getAsJsonObject().get("id").getAsInt(), null
            );
            pictureCategories.add(category);
        });
        categoryMapper.insertBatch(pictureCategories);
    }

    @Test
    public void savePictureCategory() {
        String filePath = "D:\\data\\数据集\\annotations_trainval2017\\annotations\\instances_val2017.json";
        try (Reader reader = new FileReader(filePath)) {
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(reader).getAsJsonObject();
            System.out.println("info:");
            System.out.println(root.get("info"));
            System.out.println("categories:");
            System.out.println(root.get("categories"));
            JsonArray images = root.getAsJsonArray("images");
            System.out.println("Length of images: " + images.size());
            System.out.println(images.get(0));
            JsonArray annotations = root.getAsJsonArray("annotations");
            System.out.println("Length of annotations: " + annotations.size());
            System.out.println(annotations.get(0));

            List<PictureCategory> pictureCategories = new ArrayList<>();

            for (JsonElement annotation : annotations) {
                PictureCategory pictureCategory = new PictureCategory(
                        null,
                        annotation.getAsJsonObject().get("category_id").getAsInt(),
                        annotation.getAsJsonObject().get("image_id").getAsInt());
                // 打印对象，或者可以选择不打印，以提高性能
                // System.out.println(pictureCategory);
                pictureCategories.add(pictureCategory);
            }

            // 假设 pictureCategoryMapper 有一个批量插入的方法
            pictureCategoryMapper.insertBatch(pictureCategories);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test() {
        String filePath = "D:\\data\\数据集\\annotations_trainval2017\\annotations\\instances_train2017.json";
        try (Reader reader = new FileReader(filePath)) {
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(reader).getAsJsonObject();
            System.out.println("info:");
            System.out.println(root.get("info"));
            System.out.println("categories:");
            System.out.println(root.get("categories"));
            JsonArray images = root.getAsJsonArray("images");
            System.out.println("Length of images: " + images.size());
            System.out.println(images.get(0));
            JsonArray annotations = root.getAsJsonArray("annotations");
            System.out.println("Length of annotations: " + annotations.size());
            System.out.println(annotations.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
