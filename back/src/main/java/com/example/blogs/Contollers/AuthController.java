package com.example.blogs.Contollers;

import com.example.blogs.Repositories.UserRepository;
import com.example.blogs.entities.User;
import com.example.blogs.payloads.JwtAuthRequest;
import com.example.blogs.payloads.JwtAuthResponse;
import com.example.blogs.payloads.UserDto;
import com.example.blogs.security.JwtTokenHelper;
import com.example.blogs.services.UserService;
import com.example.blogs.exceptions.ApiException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;



@RestController
@CrossOrigin("http://localhost:9090")
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
        this.authenticate(request.getUsername(), request.getPassword());
       User userDetails = userService.findByUsername(request.getUsername());

        if(passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {

            String token = jwtTokenHelper.generateToken(userDetails.getUsername());

           JwtAuthResponse response = new JwtAuthResponse();
           response.setToken(token);
          response.setUser(this.mapper.map((User) userDetails, UserDto.class));
           return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
          //  return ResponseEntity.ok().body("Successfully logged In.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        try {

            authenticationManager.authenticate(authenticationToken);

        } catch (BadCredentialsException e) {
            System.out.println("Invalid Detials !!");
            throw new ApiException("Invalid username or password !!");
        }

    }

    // register new user api

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto registeredUser = this.userService.registerNewUser(userDto);
        return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
    }

    // get loggedin user data


//    @GetMapping("/current-user/")
//    public ResponseEntity<UserDto> getUser(Principal principal) {
//        User user = this.userRepo.findByEmail(principal.getName()).get();
//        return new ResponseEntity<UserDto>(this.mapper.map(user, UserDto.class), HttpStatus.OK);
//    }
}
