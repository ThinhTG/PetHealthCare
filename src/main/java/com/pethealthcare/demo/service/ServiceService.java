package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.ServiceCreateRequest;
import com.pethealthcare.demo.enums.ServiceStatus;
import com.pethealthcare.demo.mapper.ServiceMapper;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Services;
import com.pethealthcare.demo.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    public Services createService(ServiceCreateRequest request, MultipartFile file) throws IOException {
        boolean exists = serviceRepository.existsByName(request.getName());
        if (!exists) {
            Services service = serviceMapper.toService(request);
            if (file != null && !file.isEmpty()) {
                String fileName = firebaseStorageService.uploadFile(file);
                String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/pethealthcaresystem-64c52.appspot.com/o/%s?alt=media", fileName);
                service.setImageUrl(fileUrl);
            }
            return serviceRepository.save(service);
        }
        return null;
    }

    public Services updateService(int serviceId, ServiceCreateRequest request, MultipartFile file) throws IOException {
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
            if (file != null && !file.isEmpty()) {
                String fileName = firebaseStorageService.uploadFile(file);
                String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/pethealthcaresystem-64c52.appspot.com/o/%s?alt=media", fileName);
                service.setImageUrl(fileUrl);
            }
            return serviceRepository.save(service);
        }

        return null;
    }
}

