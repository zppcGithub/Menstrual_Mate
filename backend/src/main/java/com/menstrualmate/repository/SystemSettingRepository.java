package com.menstrualmate.repository;

import com.menstrualmate.entity.SystemSetting;
import com.menstrualmate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    
    Optional<SystemSetting> findByUserAndSettingKey(User user, String settingKey);
    
    List<SystemSetting> findByUser(User user);
    
    List<SystemSetting> findByUserAndSettingKeyIn(User user, List<String> settingKeys);
}