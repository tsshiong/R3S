package com.ting.R3S;

import com.ting.R3S.Account.Account;
import com.ting.R3S.Account.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost")
@SessionAttributes({ "accountId" })
public class LoginController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public LoginController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account loginaccount, HttpSession session, HttpServletResponse response) {
        Account accountdb = accountRepository.findByEmail(loginaccount.getEmail());

        if (accountdb == null || !accountdb.getPassword().equals(loginaccount.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        if (!accountdb.getRole().equals(loginaccount.getRole())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid account type");
        }

        if (!accountdb.getStatus().equals("active")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is not active");
        }

        // Generate a session ID
        String sessionId = UUID.randomUUID().toString();

        // Store necessary information in the session entity
        Session sessionEntity = new Session();
        sessionEntity.setSessionId(sessionId);
        sessionEntity.setAccountId(accountdb.getAccountId());
        sessionRepository.save(sessionEntity);

        // Set the session ID as a cookie in the response
        Cookie sessionCookie = new Cookie("sessionId", sessionId);
        sessionCookie.setDomain("localhost"); // Set the domain to the common domain name
        sessionCookie.setPath("/"); // Set the path to the root path ("/")
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);

        // Set the session ID as an attribute in HttpSession
        session.setAttribute("sessionId", sessionId);

        // Return a success response with redirect URL based on role
        String redirectUrl;
        if (accountdb.getRole().equals("admin")) {
            redirectUrl = "http://localhost/frontend/components/AdminSide/Dashboard.html";
        } else if (accountdb.getRole().equals("manager")) {
            redirectUrl = "http://localhost/frontend/components/ManagerSide/restaurantdailyschedule.html";
        } else {
            // Handle other roles if needed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid account type");
        }

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("redirect-url", redirectUrl);
        return ResponseEntity.ok().body(responseMap);
    }

    @GetMapping("/get-session")
    public ResponseEntity<Long> getSessionData(HttpServletRequest request, HttpServletResponse response) {
        // Retrieve the session identifier (JSESSIONID) from the JSESSIONID cookie
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }

        // Check if sessionId exists in the session
        if (sessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Retrieve the session entity from the database
        Session sessionEntity = sessionRepository.findBySessionId(sessionId);

        // Check if sessionEntity exists and the session ID matches
        if (sessionEntity == null || !sessionEntity.getSessionId().equals(sessionId)) {
            // Invalidate the session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Redirect to the login page
            try {
                response.sendRedirect("http://localhost/frontend/components/index1.html"); // Adjust the URL as per your application's login page
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Access the session data (account ID)
        Long accountId = sessionEntity.getAccountId();
        System.out.println("Account ID: " + accountId);

        return ResponseEntity.ok().body(accountId);
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Retrieve the session identifier (JSESSIONID) from the JSESSIONID cookie
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }

        // Check if sessionId exists in the session
        if (sessionId == null) {
            return ResponseEntity.ok().build(); // No session to invalidate
        }

        // Retrieve the session entity from the database
        Session sessionEntity = sessionRepository.findBySessionId(sessionId);

        // Check if sessionEntity exists
        if (sessionEntity != null) {
            // Remove the session entity from the database
            sessionRepository.delete(sessionEntity);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Logout failed
        }

        // Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().build();
    }


}
