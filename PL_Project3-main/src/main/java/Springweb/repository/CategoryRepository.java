
package Springweb.repository;

import Springweb.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author caothanh
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>{

    public Category findOneByName(String Name);
    
//    @Query("SELECT * FROM category c WHERE LOWER(c.Name) = LOWER(:Name)")
//    public Category findOneByNameIgnoreCase(String Name);
    
}
