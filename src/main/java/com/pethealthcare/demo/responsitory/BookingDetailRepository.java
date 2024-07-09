package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
    List<BookingDetail> findBookingDetailByNeedCage(boolean needCage);
    List<BookingDetail> findBookingDetailByUser(User user);
}
