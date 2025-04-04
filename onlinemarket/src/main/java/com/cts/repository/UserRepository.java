package com.cts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("SELECT u.email FROM User u WHERE u.email = :email")
    String searchByEmail(@Param("email") String email);
    
    

}
