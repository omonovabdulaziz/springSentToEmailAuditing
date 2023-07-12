package uz.pdp.springsecurityemailsenderauditing.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "userlar")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;//userning takrorlanmas qismi
    @Column(nullable = false)
    private String firstName;//ismi
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    @CreationTimestamp // qachon user yaratilsa vaqtni avtamatik belgilab ketadi
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updateAt;  // oxirgi marta qachon tahrirlangani

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean creadentialsNonExpired = true;
    private boolean enabled = false;
    private String emailCode;

    //bu user detailsning methodlari //




    //BU USERNING HUQULAR ROYXATI
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }


    //USERNING USERNAMENI QAYTARUVCHI METHOD
    //ACCOUNTNIGN AMAL QILISH MUDDATINI QAYTARADI
    @Override
    public String getUsername() {
        return email;
    }


    //ACCOUNTNIGN AMAL QILISH MUDDATINI QAYTARADI
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    //ACCOUNT BLOCKLANGAN HOLATINI QAYTARADI
    @Override
    public boolean isAccountNonLocked() {
        return  accountNonLocked;
    }


    //ACCOUNTNING ISHONCHLILIK MUDDATINI QAYTARADI
    @Override
    public boolean isCredentialsNonExpired() {
        return creadentialsNonExpired;
    }


    //ACCOUNTNING ACTIVE YOKI OCHIQ EKANLIGINI QAYTARADI
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
