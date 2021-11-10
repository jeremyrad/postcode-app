package io.postcodes.db;


import io.postcodes.PostcodesServerApplication;
import io.postcodes.db.model.PostCode;
import org.junit.Test;
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

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
@ContextConfiguration(classes = {PostcodesServerApplication.class, PostCodeRepository.class})
@EnableJpaRepositories(basePackages = {"io.postcodes.*"})
@EntityScan("io.postcodes.db")
public class PostCodeRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PostCodeRepository postCodeRepository;

    @Test
    public void testInjectedComponentsAreNotNull() {
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
        assertNotNull(postCodeRepository);
    }

    @Test
    public void testSavePostCode() {
        PostCode postCode = new PostCode(1224);
        postCodeRepository.save(postCode);
    }


}