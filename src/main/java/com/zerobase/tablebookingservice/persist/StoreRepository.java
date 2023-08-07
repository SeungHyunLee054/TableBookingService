package com.zerobase.tablebookingservice.persist;

import com.zerobase.tablebookingservice.persist.entity.StoreEntity;
import com.zerobase.tablebookingservice.persist.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    List<StoreEntity> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String keyword1, String keyword2, String keyword3);

    long countByUserEntity(UserEntity userEntity);

    boolean existsByNameAndAddress(String name, String address);

    List<StoreEntity> findByUserEntity(UserEntity userEntity);
}
