package com.casestudy5.service.user;

import com.casestudy5.config.UserPrinciple;
import com.casestudy5.model.entity.user.*;
import com.casestudy5.repo.IRoleRepository;
import com.casestudy5.repo.ISellerRequestRepository;
import com.casestudy5.repo.IUserRepository;
import com.casestudy5.service.email.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private ISellerRequestRepository sellerRequestRepository;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Value("${upload.image}")
    private String uploadPathImage;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return UserPrinciple.build(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public String requestSellerRole(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        if (sellerRequestRepository.existsByUser(user)) {
            return "You already sent request!";
        }

        SellerRequest request = new SellerRequest();
        request.setUser(user);
        request.setStatus("PENDING");
        sellerRequestRepository.save(request);

        return "Request sent!";
    }

    @Override
    public String approveSeller(Long userId) {
        SellerRequest request = sellerRequestRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Request does not exist!"));

        Optional<Role> sellerRole = roleRepository.findByName(RoleName.ROLE_SELLER);
        if (sellerRole.isEmpty()) {
            throw new EntityNotFoundException("ROLE SELLER does not exist!");
        }

        User user = request.getUser();

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("ROLE SELLER does not exist!"));
        user.getRoles().remove(userRole);


        user.getRoles().add(sellerRole.get());
        userRepository.save(user);

        request.setStatus("APPROVED");
        sellerRequestRepository.save(request);

        return "Request approved!";
    }

    @Override
    public String rejectSeller(Long userId) {
        SellerRequest request = sellerRequestRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Request does not exist!"));
        sellerRequestRepository.save(request);

        return "Request denied!";
    }

    @Override
    public List<SellerRequest> getAllSellerRequests() {
        return sellerRequestRepository.findAll();
    }

    @Override
    public User findByRole(String username) {
        return null;
    }

    @Override
    public List<User> searchUsers(String searchTerm) {
        return List.of();
    }

    @Override
    public List<User> searchNameOrUsername(String searchName) {
        return userRepository.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(searchName, searchName);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByUsername(email) != null;  // có thể sửa lại thành `findByEmail` nếu có phương thức này trong repository
    }

    // Phương thức chuyển đổi từ user sang UserDTO
    public UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return dto;
    }

    // Phương thức lấy danh sách người dùng
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public String changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Verify the old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Check if the new password is different from the old password
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the old password");
        }

        // Encode and save the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password changed successfully";
    }

    public UpdateUser updateUser(FormUpdateUser formUpdateUser) throws IOException {
        Optional<User> userOptional = userRepository.findById(formUpdateUser.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            //Thuc hien cap nhat thong tin
            user.setName(formUpdateUser.getName());
            user.setPhoneNumber(formUpdateUser.getPhoneNumber());
            user.setAddress(formUpdateUser.getAddress());
            user.setBirthDate(formUpdateUser.getBirthDate());
            String avatar = null;
            if (formUpdateUser.getAvatar() != null && !formUpdateUser.getAvatar().isEmpty()) {
                avatar = saveAvatar(formUpdateUser.getAvatar());
                user.setAvatar(avatar);
            }
            User updateUser = userRepository.save(user);

            return UpdateUser.builder()
                    .id(updateUser.getId())
                    .name(updateUser.getName())
                    .phoneNumber(updateUser.getPhoneNumber())
                    .address(updateUser.getAddress())
                    .birthDate(updateUser.getBirthDate())
                    .avatar(updateUser.getAvatar())
                    .build();
        } else {
            throw new RuntimeException("User not found with ID" + formUpdateUser.getId());

        }

    }

    private String saveAvatar(MultipartFile avatarFile) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
        String filePath = uploadPathImage + "/" + fileName;
        File dest = new File(filePath);
        avatarFile.transferTo(dest);
        return fileName;
    }

    public UpdateUser getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UpdateUser.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .birthDate(user.getBirthDate())
                .avatar(user.getAvatar())
                .build();
    }

}
