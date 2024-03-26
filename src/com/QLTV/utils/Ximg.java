/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.utils;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

/**
 *
 * @author tuonght
 */
public class Ximg {

    // lấy hình ảnh theo đường dẫn
    public static Image getImage() {
        URL url = Ximg.class.getResource("/com/poly/pictures/coffeedesign.png");
        return new ImageIcon(url).getImage();
    }

    public static void save_img(File src) {
        File dsf = new File("image", src.getName());
        if (!dsf.getParentFile().exists()) { // sau khi tìm file theo đường dẫn k thấy thư mục mẹ
            dsf.getParentFile().mkdirs(); // sẽ tạo ra thư mục y như thư mục mẹ
        }
        try {
            Path from = Paths.get(src.getAbsolutePath()); // lấy dường dẫn lưu
            Path to = Paths.get(dsf.getAbsolutePath()); // lấy đường dẫn thư mục tạo

            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING); 
            // StandardCopyOption.REPLACE_EXISTING dùng để di chuyển một tập tin ngay cả khi tập tin đó đã tồn tại
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // đọc file đường dẫn chứa trong thư mục mẹ
    public static ImageIcon read_img(String file_name) {
        File f = new File("image", file_name);
        return new ImageIcon(f.getAbsolutePath());
    }
}
