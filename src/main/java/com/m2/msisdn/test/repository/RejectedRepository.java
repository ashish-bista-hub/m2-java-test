package com.m2.msisdn.test.repository;

import com.m2.msisdn.test.model.Rejected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RejectedRepository extends JpaRepository<Rejected, Long> {

    @Modifying(flushAutomatically = true)
    @Query("delete from Rejected")
    void deleteAllEntries();
}
