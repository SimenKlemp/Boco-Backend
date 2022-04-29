package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService
{
    @Autowired
    ImageRepository imageRepository;

    Logger logger = LoggerFactory.getLogger(ImageService.class);

    public long upload(MultipartFile file)
    {
        try
        {
            logger.info("Image file size: " + file.getBytes().length);

            Image image = new Image(file.getName(), file.getBytes());
            image = imageRepository.save(image);
            return image.getImageId();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public Resource getImage(long imageId)
    {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        if (optionalImage.isEmpty()) return null;
        Image image = optionalImage.get();

        return new ByteArrayResource(image.getContent());
    }
}
