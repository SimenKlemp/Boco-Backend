package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BocoApplication.class)
public class ImageServiceTest
{
    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Before
    public void before()
    {
        for (Image image : imageRepository.findAll())
        {
            imageService.delete(image.getImageId());
        }
    }

    @Test
    public void uploadCorrect()
    {
        MultipartFile file = RequestFactory.getMultipartFile();

        Long imageId = imageService.upload(file);

        assertThat(imageId).isNotNull();
    }

    @Test
    public void uploadWrong()
    {
        Long imageId = imageService.upload(null);

        assertThat(imageId).isNull();
    }

    @Test
    public void getCorrect()
    {
        Image image = ModelFactory.getImage();
        image = imageRepository.save(image);

        Resource resource = imageService.getImage(image.getImageId());

        assertThat(resource).isNotNull();
    }

    @Test
    public void getWrongImageId()
    {
        Resource resource = imageService.getImage(1L);

        assertThat(resource).isNull();
    }

    @Test
    public void deleteCorrect()
    {
        Image image = ModelFactory.getImage();
        image = imageRepository.save(image);

        boolean success = imageService.delete(image.getImageId());
        Optional<Image> optionalImage = imageRepository.findById(image.getImageId());

        assertThat(success).isTrue();
        assertThat(optionalImage.isEmpty()).isTrue();
    }

    @Test
    public void deleteWrongImageId()
    {
        boolean success = imageService.delete(0L);
        assertThat(success).isFalse();
    }
}
