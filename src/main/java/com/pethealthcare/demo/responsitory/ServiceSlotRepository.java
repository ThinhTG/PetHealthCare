package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ServiceSlotRepository extends JpaRepository<ServiceSlot, Integer> {
    List<ServiceSlot> findByUserAndDateAndStatus(User user, Date date, boolean status);

    boolean existsByUserAndSlotAndDate(User user, Slot slot, Date date);

    ServiceSlot findByUserAndDateAndSlot(User user, Date date, Slot slot);
}
