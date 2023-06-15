
package Springweb.entity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author MaiThy
 */

@Data
@Entity
@Table
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CustomerID;
    @Column
    private String email;
    @Column
    private String Password;
    @Column(name="Fullname")
    private String fullname;
    @Column
    private String Address;
    @Column
    private String City;

//    @OneToMany(mappedBy = "customer")
//    private List<Orders> listOrder;
}
