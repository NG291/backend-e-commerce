package com.casestudy5.service.user;

import com.casestudy5.config.UserPrinciple;
import com.casestudy5.model.entity.user.*;
import com.casestudy5.repo.IRoleRepository;
import com.casestudy5.repo.ISellerRequestRepository;
import com.casestudy5.repo.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            return "Bạn đã gửi yêu cầu rồi.";
        }

        SellerRequest request = new SellerRequest();
        request.setUser(user);
        request.setStatus("PENDING");
        sellerRequestRepository.save(request);

        return "Yêu cầu của bạn đã được gửi.";
    }

    @Override
    public String approveSeller(Long userId) {
        SellerRequest request = sellerRequestRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Yêu cầu không tồn tại"));

        Optional<Role> sellerRole = roleRepository.findByName(RoleName.ROLE_SELLER);
        if (sellerRole.isEmpty()) {
            throw new EntityNotFoundException("Vai trò SELLER không tồn tại");
        }

        User user = request.getUser();

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Vai trò USER không tồn tại"));
        user.getRoles().remove(userRole);


        user.getRoles().add(sellerRole.get());
        userRepository.save(user);

        request.setStatus("APPROVED");
        sellerRequestRepository.save(request);

        return "Yêu cầu đã được phê duyệt.";
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
        return userRepository.findByUsername(email) != null;  // Bạn có thể sửa lại thành `findByEmail` nếu có phương thức này trong repository
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
}
