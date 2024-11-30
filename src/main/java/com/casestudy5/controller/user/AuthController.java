package com.casestudy5.controller.user;

import com.casestudy5.config.service.JwtResponse;
import com.casestudy5.config.service.JwtService;
import com.casestudy5.model.entity.user.Role;
import com.casestudy5.model.entity.user.RoleName;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.model.entity.user.UserDTO;
import com.casestudy5.service.role.IRoleService;
import com.casestudy5.service.role.RoleService;
import com.casestudy5.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.findByUsername(loginRequest.getUsername());

            return ResponseEntity.ok(new JwtResponse(
                    currentUser.getId(),
                    jwt,
                    userDetails.getUsername(),
                    currentUser.getName(),
                    userDetails.getAuthorities()));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra xem tên người dùng đã tồn tại chưa
        if (userService.existsByUsername(userDTO.getUsername())) {
            return new ResponseEntity<>("User already existed!", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra xem email đã tồn tại chưa
        if (userService.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity<>("Email already existed!", HttpStatus.BAD_REQUEST);
        }

        // Tạo đối tượng User từ UserDTO
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        // Lấy vai trò ROLE_USER từ cơ sở dữ liệu
        Role userRole = roleService.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Cannot find role!"));

        // Thiết lập vai trò cho người dùng
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Lưu người dùng
        userService.save(user);

        // Trả về phản hồi với mã trạng thái CREATED (Đã tạo thành công)
        return new ResponseEntity<>(HttpStatus.CREATED); // Trả về trạng thái CREATED cho việc đăng ký thành công
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtService.extractTokenFromRequest(request);
        if (token != null && jwtService.validateJwtToken(token)) {
            // Add token to a blacklist if required or perform other logout logic
            return ResponseEntity.ok("Logged out successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }

    // New method to check if username is already in use
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok().body(exists ? "Username is already taken" : "Username is available");
    }

    // New method to check if email is already in use
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok().body(exists ? "Email is already in use" : "Email is available");
    }
}
