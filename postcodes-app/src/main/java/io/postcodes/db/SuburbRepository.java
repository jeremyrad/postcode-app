package io.postcodes.db;

import io.postcodes.db.model.Suburb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuburbRepository extends CrudRepository<Suburb, Long> {

    List<Suburb> findByPostCode_CodeIsBetween(Integer from, Integer to);

    List<Suburb> findSuburbByPostCodeCode(final Integer postcode);

}
