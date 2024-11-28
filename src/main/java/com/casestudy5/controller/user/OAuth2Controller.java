package com.casestudy5.controller.user;

import com.casestudy5.config.jwt.JWTTokenHelper;
import com.casestudy5.config.service.OAuth2Service;
import com.casestudy5.model.entity.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    OAuth2Service oAuth2Service;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @GetMapping("/success")
    public void callbackOAuth2(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response) throws IOException {
        String userName = oAuth2User.getAttribute("email");
        if (userName == null) {
            throw new RuntimeException("Email not found in OAuth2User attributes!");
        }

        // Fetch or create the user
        User user = oAuth2Service.getUser(userName);
        if (user == null) {
            user = oAuth2Service.createUser(oAuth2User, "google");
        }

        // Generate JWT token
        String token = jwtTokenHelper.generateToken(user.getUsername());

        // Log the token and redirect
        System.out.println("Generated JWT: " + token);
        response.sendRedirect("http://localhost:3000/oauth2/callback?token=" + token);
    }
}
