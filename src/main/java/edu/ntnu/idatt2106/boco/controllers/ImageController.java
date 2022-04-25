package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.service.ImageService;
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

    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource getImage(@PathVariable long imageId)
    {
        return imageService.getImage(imageId);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    long uploadImage(@RequestParam("image") MultipartFile image)
    {
        return imageService.upload(image);
    }
}