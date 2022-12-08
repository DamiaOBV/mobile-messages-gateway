package com.mobilemessagesgateway.domain.repository;

import com.mobilemessagesgateway.domain.entity.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long> {

}
