package com.pethealthcare.service;

import com.pethealthcare.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.mapper.ServiceSlotMapper;
import com.pethealthcare.dto.request.GetSlotAvailableRequest;
import com.pethealthcare.model.ServiceSlot;
import com.pethealthcare.model.Slot;
import com.pethealthcare.model.User;
import com.pethealthcare.responsitory.ServiceSlotRepository;
import com.pethealthcare.responsitory.SlotRepository;
import com.pethealthcare.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public void bookedSlot(int userId, Date date, int slotId) {
        User user = new User();
        user.setUserId(userId);
        Slot slot = new Slot();
        slot.setSlotId(slotId);
        ServiceSlot serviceSlot = serviceSlotRepository.findByUserAndDateAndSlot(user, date, slot);
        serviceSlot.setStatus(true);

        serviceSlotRepository.save(serviceSlot);
    }
}
