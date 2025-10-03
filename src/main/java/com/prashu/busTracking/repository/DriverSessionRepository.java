
package com.prashu.busTracking.repository;

import com.prashu.busTracking.model.DriverSession;
import com.prashu.busTracking.model.User;
import com.prashu.busTracking.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface DriverSessionRepository extends JpaRepository<DriverSession, Long> {
    
    // FIXED: Use explicit query to avoid Spring Data naming confusion
    @Query("SELECT ds FROM DriverSession ds WHERE ds.driver = :driver AND ds.isActive = true AND ds.sessionEnd IS NULL")
    Optional<DriverSession> findActiveSessionByDriver(@Param("driver") User driver);
    
    // FIXED: Use explicit query
    @Query("SELECT ds FROM DriverSession ds WHERE ds.assignedBus = :bus AND ds.isActive = true AND ds.sessionEnd IS NULL")
    Optional<DriverSession> findActiveSessionByBus(@Param("bus") Bus bus);
}