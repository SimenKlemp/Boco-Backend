package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/image")
@EnableAutoConfiguration
@CrossOrigin
class ImageController
{
    @Autowired
    ImageService imageService;

    Logger logger = LoggerFactory.getLogger(ImageController.class);


    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource getImage(@PathVariable Long imageId)
    {
        return imageService.getImage(imageId);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Long uploadImage(@RequestParam("image") MultipartFile image)
    {
        logger.info("Uploading image");
        return imageService.upload(image);
    }
}