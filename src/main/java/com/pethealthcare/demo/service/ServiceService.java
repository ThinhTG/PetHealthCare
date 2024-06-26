package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.ServiceCreateRequest;
import com.pethealthcare.demo.mapper.ServiceMapper;
import com.pethealthcare.demo.model.Services;
import com.pethealthcare.demo.responsitory.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceMapper serviceMapper;

    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    public Services createService(ServiceCreateRequest request) {
        boolean exists = serviceRepository.existsByName(request.getName());
        if (!exists) {
            Services service = serviceMapper.toService(request);
            service.setImageUrl(request.getImageUrl());
            return serviceRepository.save(service);
        }
        return null;
    }

    public Services updateService(int serviceId, ServiceCreateRequest request) {
        Optional<Services> updateService = serviceRepository.findById(serviceId);
        if (updateService.isPresent()) {
            Services service = updateService.get();
            if(!service.getName().equals(request.getName())) {
                service.setName(request.getName());
            }
            if(!service.getDescription().equals(request.getDescription())) {
                service.setDescription(request.getDescription());
            }
            if(service.getPrice() != request.getPrice()) {
                service.setPrice(request.getPrice());
            }
            return serviceRepository.save(service);
        }

//        Services services = serviceMapper.toService(request);
        return null;
    }

    public void deleteService(int serviceId) {
        boolean exists = serviceRepository.existsById(serviceId);
        if (exists) {
            serviceRepository.deleteById(serviceId);
        }
    }

    public Services getServiceById(int  serviceID) {

        return serviceRepository.findByServiceId(serviceID);
    }
}

