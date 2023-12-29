package com.ablic.cinema.helpers;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageHelper {
    public static void loadImage(String filename, String dir, MultipartFile file) throws URISyntaxException, IOException {
        String destination = Paths.get(ImageHelper.class.getClassLoader().getResource("static").toURI()) + dir;
        File f = new File(destination);
        if (!Files.exists(Paths.get(destination))) {
            f.mkdir();
        }
        Path fileNameAndPath = Paths.get(destination, filename);
        Files.write(fileNameAndPath, file.getBytes());
    }

    public static String generateUniqName() {
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return fileName;
    }

    public static boolean deleteImage(String staticPath) throws URISyntaxException {
        String fullPath = Paths.get(ImageHelper.class.getClassLoader().getResource("static").toURI()) + staticPath;
        File file = new File(fullPath);
        if (Files.exists(Paths.get(fullPath))) {
            file.delete();
            return true;
        }
        return false;
    }

    public static byte[] getImage(String path) throws IOException, URISyntaxException {
        String fullPath = Paths.get(ImageHelper.class.getClassLoader().getResource("static").toURI()) + path;
        return Files.readAllBytes(Path.of(fullPath));
    }
}
