package org.falabella.pocbigquery.repository;

import org.falabella.pocbigquery.entity.Vtakaowp;
import org.falabella.pocbigquery.entity.VtakaowpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VtakaowpRepository extends JpaRepository<Vtakaowp, VtakaowpId> {
}
