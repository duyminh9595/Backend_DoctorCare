package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.HistoryUpdatePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "historyupdateprice",collectionResourceRel = "historyupdateprice")
@CrossOrigin
public interface HistoryUpdatePriceRepository extends JpaRepository<HistoryUpdatePrice,Long> {
}
