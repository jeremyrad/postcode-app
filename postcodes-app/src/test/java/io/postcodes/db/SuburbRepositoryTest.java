package io.postcodes.db;

import io.postcodes.PostcodesServerApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
@ContextConfiguration(classes = {PostcodesServerApplication.class, PostCodeRepository.class})
@EnableJpaRepositories(basePackages = {"io.postcodes.*"})
@EntityScan("io.postcodes.db")
class SuburbRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SuburbRepository suburbRepository;
    @Autowired
    private PostCodeRepository postCodeRepository;

    @Test
    public void testSaveSuburb() {

        final PostCode savedPostCode = postCodeRepository.save(new PostCode(1224));
        Suburb suburb = new Suburb();
        suburb.setPostCode(savedPostCode);
        suburb.setName("test");
        final Suburb savedSuburb = suburbRepository.save(suburb);

        assertNotNull(savedSuburb);
        assertTrue(savedSuburb.getId() > 0);
        assertEquals("test", savedSuburb.getName());
        assertEquals(1224, savedSuburb.getPostCode().getCode());

    }

    @Test
    void findByPostCode_CodeIsBetween() {

        final PostCode savedPostCode1 = postCodeRepository.save(new PostCode(1224));
        Suburb suburb1 = new Suburb();
        suburb1.setPostCode(savedPostCode1);
        suburb1.setName("A");
        final Suburb savedSuburb1 = suburbRepository.save(suburb1);

        final PostCode savedPostCode2 = postCodeRepository.save(new PostCode(1225));
        Suburb suburb2 = new Suburb();
        suburb2.setPostCode(savedPostCode2);
        suburb2.setName("B");
        final Suburb savedSuburb2 = suburbRepository.save(suburb2);

        final PostCode savedPostCode3 = postCodeRepository.save(new PostCode(1228));
        Suburb suburb3 = new Suburb();
        suburb3.setPostCode(savedPostCode3);
        suburb3.setName("C");
        suburbRepository.save(suburb3);

        final List<Suburb> byPostCode_codeIsBetween = suburbRepository
                .findByPostCode_CodeIsBetween(savedSuburb1.getPostCode().getCode(), savedSuburb2.getPostCode().getCode());

        assertNotNull(byPostCode_codeIsBetween);
        assertFalse(byPostCode_codeIsBetween.isEmpty());
        assertEquals(byPostCode_codeIsBetween.size(), 2);
    }

    @Test
    void findSuburbByPostCodeCode() {
        final PostCode savedPostCode1 = postCodeRepository.save(new PostCode(1224));
        Suburb suburb = new Suburb();
        suburb.setPostCode(savedPostCode1);
        suburb.setName("test");
        final Suburb savedSuburb = suburbRepository.save(suburb);

        final List<Suburb> byPostCode_codeIsBetween = suburbRepository
                .findSuburbByPostCodeCode(savedSuburb.getPostCode().getCode());

        assertNotNull(byPostCode_codeIsBetween);
        assertFalse(byPostCode_codeIsBetween.isEmpty());
        assertEquals(byPostCode_codeIsBetween.size(), 1);
    }
}