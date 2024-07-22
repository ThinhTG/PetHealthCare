package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Cage;
import com.pethealthcare.demo.model.CageType;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.repository.BookingDetailRepository;
import com.pethealthcare.demo.repository.CageRepository;
import com.pethealthcare.demo.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CageService {

    @Autowired
    private CageRepository cageRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    public Cage createCage(CageType type) {
        Cage newCage = new Cage();
        newCage.setPet(null);
        newCage.setCageType(type);
        newCage.setStatus(true);

        return cageRepository.save(newCage);

    }

    public Cage checkinCage(int Cageid, int BookingDetailId) {
        Cage cage = cageRepository.findById(Cageid);
        Optional<BookingDetail> bookingDetailOptional = bookingDetailRepository.findById(BookingDetailId);

        if (!bookingDetailOptional.isPresent()) {
            // Handle the case where the BookingDetail is not found
            throw new EntityNotFoundException("BookingDetail not found with id: " + BookingDetailId);
        }

        BookingDetail bookingDetail = bookingDetailOptional.get();
        Pet pet = petRepository.findPetByPetId(bookingDetail.getPet().getPetId());
        pet.setStayCage(true);
        cage.setPet(pet);
        bookingDetail.setNeedCage(false);
        cage.setStatus(false);

        return cageRepository.save(cage);
    }


    public List<Cage> getALl(){
        List<Cage> cages = cageRepository.findAll();

        return cages;
    }


    public List<Cage> getALlEmptyCage(){
        List<Cage> cages = cageRepository.findAll();


        return cages;
    }

    public Cage checkoutCage(int Cageid) {
        Cage cage = cageRepository.findById(Cageid);
        Pet pet = petRepository.findPetByPetId(cage.getPet().getPetId());
        pet.setStayCage(false);
        cage.setPet(null);
        cage.setStatus(true);

        return cageRepository.save(cage);
    }

    public List<Cage> getCageAvailableByType(CageType type) {
        List<Cage> cages = cageRepository.findCageByCageType(type);
        List<Cage> availableCages = new ArrayList<>();
        for (Cage cage : cages) {
            if (cage.getPet() == null) {
                availableCages.add(cage);
            }
        }
        return availableCages;
    }

    public List<Cage> getCageHasPetByType() {
        List<Cage> cages = cageRepository.findAll();
        List<Cage> availableCages = new ArrayList<>();
        for (Cage cage : cages) {
            if (cage.getPet() != null) {
                availableCages.add(cage);
            }
        }
        return availableCages;
    }


}
