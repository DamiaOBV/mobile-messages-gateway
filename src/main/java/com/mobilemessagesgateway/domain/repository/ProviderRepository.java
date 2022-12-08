package com.mobilemessagesgateway.domain.repository;

import com.mobilemessagesgateway.domain.entity.Provider;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    String FIND_AVAILABLE_PREFIXES = "select distinct prefix from provider";
    String FIND_BY_PREFIX_WITH_MIN_COST = "select * from provider where prefix=:prefix and cost = " +
                                          "(select min(cost) from provider where prefix=:prefix)";

    @Query(value = FIND_AVAILABLE_PREFIXES, nativeQuery = true)
    int[] getAvailablePrefixes();
    @Query(value = FIND_BY_PREFIX_WITH_MIN_COST, nativeQuery = true)
    Provider[] findByPrefixWithMinCost(@Param("prefix") int prefix);
}
