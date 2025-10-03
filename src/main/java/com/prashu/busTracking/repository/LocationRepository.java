package com.prashu.busTracking.repository;

import com.prashu.busTracking.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByBus_BusIdOrderByTimestampDesc(Long busId);
    
    @Query("SELECT l FROM Location l WHERE l.bus.busId = :busId ORDER BY l.timestamp DESC LIMIT 1")
    Optional<Location> findLatestByBusId(@Param("busId") Long busId);
    
    List<Location> findTop10ByOrderByTimestampDesc();
}