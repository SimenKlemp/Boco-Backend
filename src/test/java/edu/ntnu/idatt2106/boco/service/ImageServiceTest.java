package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)

public class ImageServiceTest
{
    @InjectMocks
    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @BeforeEach
    public void beforeEach()
    {
        RepositoryMock.mockImageRepository(imageRepository);
    }

    @Test
    public void uploadCorrect()
    {
        MultipartFile file = RequestFactory.getMultipartFile();

        Long imageId = imageService.upload(file);

        assertThat(imageId).isEqualTo(1L);
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
}
