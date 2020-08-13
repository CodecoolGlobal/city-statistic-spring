package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.Image;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class ImageRepositoryTest {
    @Autowired
    ImageRepository imageRepository;

    @Test
    public void saveOneSimple(){
        Image image = Image.builder()
                .slug("budapest")
                .base64("image/budapest.jpg")
                .build();

        imageRepository.save(image);

        assertThat(imageRepository.findAll()).hasSize(1);
    }

    @Test
    public void slugShouldBeNotNull(){
        Image image = Image.builder()
                .base64("image/random.jpg")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {imageRepository.save(image);});
    }

    @Test
    public void base64ShouldBeNotNull(){
        Image image = Image.builder()
                .slug("budapest")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {imageRepository.save(image);});
    }

    @Test
    public void getAllBase64BySlug(){
        Image image = Image.builder()
                .slug("budapest")
                .base64("image/budapest.jpg")
                .build();

        Image image2 = Image.builder()
                .slug("budapest")
                .base64("image/budapest2.jpg")
                .build();


        Image image3 = Image.builder()
                .slug("london")
                .base64("image/london.jpg")
                .build();

        imageRepository.saveAll(Lists.newArrayList(image, image2, image3));

        assertThat(imageRepository.getAllBase64BySlug("budapest")).hasSize(2)
                .containsExactlyInAnyOrder(image.getBase64(), image2.getBase64());
    }
}