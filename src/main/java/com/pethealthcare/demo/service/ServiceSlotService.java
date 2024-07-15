package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.GetSlotAvailableRequest;
import com.pethealthcare.demo.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.demo.mapper.ServiceSlotMapper;
import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.ServiceSlotRepository;
import com.pethealthcare.demo.responsitory.SlotRepository;
import com.pethealthcare.demo.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class ServiceSlotService {
    @Autowired
    private ServiceSlotRepository serviceSlotRepository;

    @Autowired
    private ServiceSlotMapper serviceSlotMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlotRepository slotRepository;

    public List<ServiceSlot> getSlotAvailable(GetSlotAvailableRequest request) {
        User user = userRepository.findUserByUserId(request.getUserId());

        return serviceSlotRepository.findByUserAndDateAndStatus(user,
                request.getDate(), false);
    }

    public String addServiceSlots(List<ServiceSlotCreateRequest> requests) {
        for (ServiceSlotCreateRequest request : requests) {
            User user = userRepository.findUserByUserId(request.getUserId());
            Slot slot = slotRepository.findSlotBySlotId(request.getSlotId());

            if (slot == null) {
                return "Slot with ID " + request.getSlotId() + " is not existed";
            }

            boolean existed = serviceSlotRepository.existsByUserAndSlotAndDate(user, slot, request.getDate());
            if (!existed) {
                ServiceSlot serviceSlot = serviceSlotMapper.toServiceSlot(request);
                serviceSlot.setStatus(false);
                serviceSlot.setUser(user);
                serviceSlot.setSlot(slot);
                serviceSlotRepository.save(serviceSlot);
            } else {
                return "ServiceSlot with user " + request.getUserId() + " and slot " + request.getSlotId() + " is already existed";
            }
        }

        return "Insert Service Slots Successfully";
    }


    public void bookedSlot(int userId, LocalDate date, int slotId) {
        User user = userRepository.findUserByUserId(userId);
        Slot slot = slotRepository.findSlotBySlotId(slotId);
        ServiceSlot serviceSlot = serviceSlotRepository.findServiceSlotByUserAndDateAndSlot(user, date, slot);
        if (serviceSlot == null) {
            throw new RuntimeException("Service Slot is not existed");
        }
        serviceSlot.setStatus(true);

        serviceSlotRepository.save(serviceSlot);
    }
}
