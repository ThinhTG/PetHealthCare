package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.enums.ServiceSlotStatus;
import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceSlotRepository extends JpaRepository<ServiceSlot, Integer> {
    List<ServiceSlot> findByUserAndDateAndStatus(User user, LocalDate date, ServiceSlotStatus status);

    boolean existsByUserAndSlotAndDate(User user, Slot slot, LocalDate date);

    List<ServiceSlot> getServiceSlotByUser(User user);

    ServiceSlot findServiceSlotByUserAndDateAndSlot (User user, LocalDate date, Slot slot);

    List<ServiceSlot> findByUserAndDate(User user, LocalDate date);

    List<ServiceSlot> findServiceSlotByDateAndSlot_SlotIdAndAndStatus(LocalDate date, int slotId, ServiceSlotStatus status);
}
