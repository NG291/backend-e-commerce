package com.casestudy5.controller.user;

import com.casestudy5.config.jwt.JWTTokenHelper;
import com.casestudy5.config.service.OAuth2Service;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    OAuth2Service oAuth2Service;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private UserService userService;

    @GetMapping("/success")
    public void callbackOAuth2(HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oAuth2User) {
            String email = oAuth2User.getAttribute("email");
            if (email == null) {
                throw new RuntimeException("Email not found in OAuth2User attributes!");
            }

            // Check if the user exists, and create or update the user accordingly
            Optional<User> userOpt = oAuth2Service.getUser(email);
            User user;
            if (userOpt.isEmpty()) {
                // Create a new user if they don't exist
                user = oAuth2Service.createUser(oAuth2User, "google");
            } else {
                // Update the user if they already exist
                user = userOpt.get();
                oAuth2Service.updateUser(oAuth2User, user);  // Assume you have an update method
            }

            // Ensure the user is saved to the database (if needed)
            userService.save(user);

            // Generate JWT token
            String token = jwtTokenHelper.generateToken(user.getUsername());

            // Redirect to the client with the token
            response.sendRedirect("http://localhost:3000/oauth2/callback?token=" + token);
        } else {
            throw new RuntimeException("Unexpected principal type: " + principal.getClass());
        }
    }

}
