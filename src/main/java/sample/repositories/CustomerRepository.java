package sample.repositories;

import org.springframework.data.repository.CrudRepository;
import sample.entities.Customer;

/**
 * Created by deepak on 8/20/18.
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
