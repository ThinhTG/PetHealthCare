package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.Pet;

import java.util.List;

public interface IPetService {
    List<Pet> getPetsByUserID(int userID);
}
