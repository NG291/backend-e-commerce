package com.casestudy5.service.user;

import com.casestudy5.config.UserPrinciple;
import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.user.*;
import com.casestudy5.repo.*;
import com.casestudy5.service.email.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IProductRepository productRepository;

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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .toList()
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public String requestSellerRole(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (sellerRequestRepository.existsByUser(user)) {
            return "You already sent a request!";
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
                .orElseThrow(() -> new EntityNotFoundException("ROLE USER does not exist!"));

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

        request.setStatus("REJECTED");
        sellerRequestRepository.save(request);

        return "Request denied!";
    }

    @Override
    public List<SellerRequest> getAllSellerRequests() {
        return sellerRequestRepository.findAll();
    }

    @Override
    public Optional<User> findByRole(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> searchUsers(String searchTerm) {
        return userRepository.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(searchTerm, searchTerm);
    }

    @Override
    public List<User> searchNameOrUsername(String searchName) {
        return List.of();
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

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

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public String changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password changed successfully";
    }

    public boolean isCartItemExists(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return cartItemRepository.findByUserAndProduct(user, product) != null;
    }


}
