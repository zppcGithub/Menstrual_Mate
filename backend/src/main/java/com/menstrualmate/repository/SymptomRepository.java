package com.menstrualmate.repository;

import com.menstrualmate.entity.Symptom;
import com.menstrualmate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    
    List<Symptom> findByUserAndIsCustomFalse(User user);
    
    List<Symptom> findByUserAndIsCustomTrue(User user);
    
    List<Symptom> findByUserAndTypeOrderByName(User user, Symptom.SymptomType type);
    
    List<Symptom> findByUserOrderByTypeAscNameAsc(User user);
}