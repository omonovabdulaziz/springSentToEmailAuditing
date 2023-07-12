package uz.pdp.springsecurityemailsenderauditing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.springsecurityemailsenderauditing.entity.Product;

import java.util.UUID;


@RepositoryRestResource(path = "product")
public interface ProductRepository extends JpaRepository<Product , UUID> {
}
