package com.spring.Docdoc.controller;

import com.spring.Docdoc.entity.Image;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.exception.ResponseMessage;
import com.spring.Docdoc.service.AuthService;
import com.spring.Docdoc.service.ImageService;
import com.spring.Docdoc.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class imageController {

    final private AuthService authService;
    final private ImageService imageService;
    final private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseMessage> saveImage(@RequestParam MultipartFile multipartFile)  {

        try {
            User user = authService.getCurrentUser() ;

            if(multipartFile.isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseMessage
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Please upload a file")
                        .build());
            }


            String imagePath = imageService.saveUserImage(multipartFile,user) ;

            user.setImage(imagePath);
            userService.update(user);


            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage
                            .builder()
                            .status(HttpStatus.OK.value())
                            .message("Uploaded image successfully")
                            .build());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseMessage
                        .builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Failed to save image: " + e.getMessage())
                        .build());
        }

    }

    @GetMapping(value = "{name}" )
    public ResponseEntity<byte[]> getImage(@PathVariable String name) {
        Image image = imageService.findByName(name) ;
        if(image == null) {
            throw new NotFoundException("Image not found with name: " + name);
        }

        byte[] imageData = imageService.decompressImage(image.getImageData());


        HttpHeaders headers = new HttpHeaders() ;
        headers.setContentType(imageService.getMediaType(name));

        return new ResponseEntity<>(imageData,headers,HttpStatus.OK);
    }

}
