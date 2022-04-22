package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService
{
    @Autowired
    ImageRepository imageRepository;

    public Image createImage(MultipartFile file)
    {
        try
        {
            Image image = new Image(file.getName(), file.getBytes());
            image = imageRepository.save(image);
            return image;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
