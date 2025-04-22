package com.ags.logitrack.service;

import com.ags.logitrack.model.RoboLogistico;
import com.ags.logitrack.repository.RoboLogisticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoboLogisticoService {

    @Autowired
    private RoboLogisticoRepository roboLogisticoRepository;

    public List<RoboLogistico> findAll() {
        return roboLogisticoRepository.findAll();
    }

    public Optional<RoboLogistico> findById(Long id) {
        return roboLogisticoRepository.findById(id);
    }

    public RoboLogistico save(RoboLogistico roboLogistico) {
        return roboLogisticoRepository.save(roboLogistico);
    }

    public RoboLogistico update(Long id, RoboLogistico roboLogistico) {
        if (roboLogisticoRepository.existsById(id)) {
            roboLogistico.setId(id);
            return roboLogisticoRepository.save(roboLogistico);
        }
        return null;
    }

    public void delete(Long id) {
        roboLogisticoRepository.deleteById(id);
    }

    public RoboLogistico atualizarNivelBateria(Long id, Double nivelBateria) {
        Optional<RoboLogistico> roboOptional = roboLogisticoRepository.findById(id);
        if (roboOptional.isPresent()) {
            RoboLogistico robo = roboOptional.get();
            robo.setNivelBateria(nivelBateria);
            return roboLogisticoRepository.save(robo);
        }
        return null;
    }

    public RoboLogistico atualizarStatus(Long id, String status) {
        Optional<RoboLogistico> roboOptional = roboLogisticoRepository.findById(id);
        if (roboOptional.isPresent()) {
            RoboLogistico robo = roboOptional.get();
            robo.setStatus(status);
            return roboLogisticoRepository.save(robo);
        }
        return null;
    }
}
