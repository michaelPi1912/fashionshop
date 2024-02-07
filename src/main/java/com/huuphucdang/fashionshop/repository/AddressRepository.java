package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("""
            select a
            from Address a  inner join User u  on a.user.id = u.id 
            where u.id = :userId
            """)
    List<Address> getAllByUser(UUID userId);

}
