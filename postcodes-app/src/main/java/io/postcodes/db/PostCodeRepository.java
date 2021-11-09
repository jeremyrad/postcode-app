package io.postcodes.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCodeRepository extends CrudRepository<PostCode, Integer> {

}
