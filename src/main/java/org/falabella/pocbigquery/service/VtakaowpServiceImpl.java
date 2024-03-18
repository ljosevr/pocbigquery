package org.falabella.pocbigquery.service;

import lombok.extern.slf4j.Slf4j;
import org.falabella.pocbigquery.entity.Vtakaowp;
import org.falabella.pocbigquery.repository.VtakaowpRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VtakaowpServiceImpl implements VtakaowpService{

    private final VtakaowpRepository vtakaowpRepository;

    public VtakaowpServiceImpl(VtakaowpRepository vtakaowpRepository) {
        this.vtakaowpRepository = vtakaowpRepository;
    }

    @Override
    public void insertar(Vtakaowp vtakaowp) {

        vtakaowp = vtakaowpRepository.save(vtakaowp);

        log.info("Registro Insertado en Vtakaowp: {}", vtakaowp);
    }
}
