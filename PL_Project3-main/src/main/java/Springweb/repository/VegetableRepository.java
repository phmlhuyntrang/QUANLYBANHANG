package Springweb.repository;

import Springweb.*;
import Springweb.entity.Vegetable;
import Springweb.entity.Category;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author caothanh
 */
@Repository
public interface VegetableRepository extends CrudRepository<Vegetable, Integer> {

    public Iterable<Vegetable> findByCatagoryID(int CatagoryID);

    List<Vegetable> findByVegetableNameContaining(String VegetableName);

    List<Vegetable> findByVegetableNameContainingAndCatagoryID(String VegetableName, int categoryID);

}
