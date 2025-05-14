package org.example.popitka2.repository;

import org.example.popitka2.moskvich_card.MoskvichCardRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MoskvichCardRepository extends JpaRepository<MoskvichCardRequest, Long> {

    List<MoskvichCardRequest> findByRequestNumber(String requestNumber);
    Optional<MoskvichCardRequest> findByUserUsername(String username);

    @Query("SELECT c.requestNumber FROM MoskvichCardRequest c WHERE c.user.username = :username")
    String findRequestNumberByUsername(@Param("username") String username);
    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM updated_at) - EXTRACT(EPOCH FROM created_at)) FROM moskvich_card_requests WHERE updated_at IS NOT NULL", nativeQuery = true)
    Double getAverageProcessingTime();

}

