// FILE: src/main/java/com/prashu/busTracking/repository/BusStopRepository.java
package com.prashu.busTracking.repository;

import com.prashu.busTracking.model.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BusStopRepository extends JpaRepository<BusStop, Long> {
    
    // Use the actual field names from your Bus and Stop entities
    List<BusStop> findByBusBusId(Long busId);
    List<BusStop> findByStopStopId(Long stopId);
    Optional<BusStop> findByBusBusIdAndStopStopId(Long busId, Long stopId);
    List<BusStop> findByBusBusIdOrderByStopOrderAsc(Long busId);
    
    // If you want to find by bus number instead of ID
    List<BusStop> findByBusBusNumber(String busNumber);
}