package io.postcodes.db;

import io.postcodes.db.model.PostCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCodeRepository extends CrudRepository<PostCode, Integer> {

}
