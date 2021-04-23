package com.m2.msisdn.test.repository;

import com.m2.msisdn.test.model.Msisdn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MsisdnRepository extends JpaRepository<Msisdn, String> {
    Msisdn findByMsisdn(String msisdn);

    @Modifying(flushAutomatically = true)
    @Query("delete from Msisdn")
    void deleteAllEntries();
}
