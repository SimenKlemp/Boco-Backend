package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * A class that represents a ImageService
 */
@Service
public class ImageService
{
    @Autowired
    ImageRepository imageRepository;

    Logger logger = LoggerFactory.getLogger(ImageService.class);

    /**
     * A method for storing an image to database
     *
     * @param file the MultipartFile that is being stored
     * @return returns a long status
     */
    public Long upload(MultipartFile file)
    {
        try
        {
            Image image = new Image(file.getName(), file.getBytes());
            image = imageRepository.save(image);
            return image.getImageId();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A method for retrieving an image based on the id
     *
     * @param imageId the imageId thar is being fetched
     * @return returns a Resource with the image info
     */

    public Resource getImage(Long imageId)
    {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        if (optionalImage.isEmpty()) return null;
        Image image = optionalImage.get();

        return new ByteArrayResource(image.getContent());
    }

    public boolean delete(Long imageId)
    {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        if (optionalImage.isEmpty()) return false;
        Image image = optionalImage.get();

        imageRepository.delete(image);
        return true;
    }
}
