package uz.pdp.springsecurityemailsenderauditing.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurityemailsenderauditing.entity.Role;
import uz.pdp.springsecurityemailsenderauditing.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role ,Integer> {
    Role findByRoleName(RoleName roleName);
}
