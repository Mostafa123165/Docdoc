package com.spring.Docdoc.service;


import com.spring.Docdoc.entity.Image;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.repository.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {


    @Autowired
    private ImageRepository imageRepository ;


    public byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public  byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    @Transactional
    public String saveUserImage(MultipartFile multipartFile , User user) throws IOException {

        String imagePath = getImagePathForUser(multipartFile,user) ;

        byte[] imageData = compressImage(multipartFile.getBytes());
        Image image = Image.builder()
                .name(imagePath)
                .imageData(imageData)
                .build() ;

        Image oldImage = findByStartWith(user.getId()+"-"+"User") ;

        if(oldImage != null) {
            image.setId(oldImage.getId());
        }

        imageRepository.save(image) ;

        return imagePath ;
    }


    private String getImagePathForUser(MultipartFile multipartFile , User user) {
        String fileName = multipartFile.getOriginalFilename() ;
        String extension = getExtensionImage(fileName) ;
        return  user.getId()+"-"+"User" + extension;
    }


    public Image findByName(String name) {

        return imageRepository.findByName(name).orElse(null);
    }

    private Image findByStartWith(String name) {

        return imageRepository.findByNameStartingWith(name).orElse(null);
    }

    public String getExtensionImage(String name) {

        return name.substring(name.lastIndexOf('.'));
    }


    public MediaType getMediaType(String name) {

        String extension = getExtensionImage(name);

        if (extension.equals(".svg")) {
            return MediaType.valueOf("image/svg+xml");
        } else {
            return MediaType.IMAGE_PNG;
        }

    }

    public void saveImage(MultipartFile multipartFile , String imagePath) throws IOException {

        byte[] imageData = compressImage(multipartFile.getBytes());
        Image image = Image.builder()
                .name(imagePath)
                .imageData(imageData)
                .build() ;


        Image oldImage = findByStartWith(imagePath) ;

        if(oldImage != null) {
            image.setId(oldImage.getId());
        }

        imageRepository.save(image) ;
    }


    @Transactional
    public void deleteImageByName(String imagePath) {

        imageRepository.deleteByName(imagePath);
    }
}