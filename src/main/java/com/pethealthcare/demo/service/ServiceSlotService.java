package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.GetSlotAvailableRequest;
import com.pethealthcare.demo.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.demo.mapper.ServiceSlotMapper;
import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.ServiceSlotRepository;
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

    public List<ServiceSlot> getSlotAvailable(GetSlotAvailableRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        return serviceSlotRepository.findByUserAndDateAndStatus(user,
                request.getDate(), false);
    }

    public String addServiceSlot(ServiceSlotCreateRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());

        Slot slot = new Slot();
        slot.setSlotId(request.getSlotId());
        boolean existed = serviceSlotRepository.existsByUserAndSlotAndDate(user,
                slot, request.getDate());
        if (!existed) {
            ServiceSlot serviceSlot = serviceSlotMapper.toServiceSlot(request);

            serviceSlot.setStatus(false);

            serviceSlot.setUser(user);

            serviceSlot.setSlot(slot);

            serviceSlotRepository.save(serviceSlot);

            return "Insert Service Slot Successfully";
        }

        return "ServiceSlot is existed";
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
