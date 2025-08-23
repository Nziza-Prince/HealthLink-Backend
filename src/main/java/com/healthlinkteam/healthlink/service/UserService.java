package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.CreateUserDTO;
import com.healthlinkteam.healthlink.dto.UserDTO;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.enums.UserRole;
import com.healthlinkteam.healthlink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<UserDTO> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setEmail(createUserDTO.getEmail());
        user.setPhoneNo(createUserDTO.getPhoneNo());
        user.setRole(createUserDTO.getRole());
        user.setDepartment(createUserDTO.getDepartment());

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO updateUser(Long id, CreateUserDTO updateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updateUserDTO.getFirstName());
        user.setLastName(updateUserDTO.getLastName());
        user.setEmail(updateUserDTO.getEmail());
        user.setPhoneNo(updateUserDTO.getPhoneNo());
        user.setRole(updateUserDTO.getRole());
        user.setDepartment(updateUserDTO.getDepartment());

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        user.setEndDate(LocalDate.now());
        userRepository.save(user);
    }

    public List<UserDTO> getDoctors() {
        return userRepository.findByRoleAndIsActiveTrue(UserRole.DOCTOR).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNo(user.getPhoneNo());
        dto.setRole(user.getRole());
        dto.setDepartment(user.getDepartment());
        dto.setJoinedDate(user.getJoinedDate());
        dto.setEndDate(user.getEndDate());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}