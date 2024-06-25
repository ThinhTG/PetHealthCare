package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.Cage;
import com.pethealthcare.demo.responsitory.CageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CageService {

    @Autowired
    private CageRepository cageRepository;

    public Cage createCage() {
        Cage newCage = new Cage();
        newCage.setStatus(true);

        return cageRepository.save(newCage);

    }

    public Cage updateCage(int Cageid, boolean status) {
        Cage cage = cageRepository.findById(Cageid);
        cage.setStatus(status);

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
}
