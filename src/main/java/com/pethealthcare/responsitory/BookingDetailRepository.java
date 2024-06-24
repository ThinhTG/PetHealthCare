package com.pethealthcare.responsitory;

import com.pethealthcare.model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
}
