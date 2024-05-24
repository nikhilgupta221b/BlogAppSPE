package com.example.blogs.services.implementations;

//import com.example.blogs.Repositories.RoleRepository;
import com.example.blogs.Repositories.UserRepository;
import com.example.blogs.config.AppConstants;
import com.example.blogs.entities.Role;
import com.example.blogs.entities.User;
import com.example.blogs.payloads.UserDto;
import com.example.blogs.services.UserService;
import com.example.blogs.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.catalina.startup.UserDatabase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.blogs.entities.Role.USER;

@Service
public class UserImplementation implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

   // @Autowired
   // private RoleRepository roleRepo;

    @Override
    public UserDto registerNewUser(UserDto userDto) {

        User user = this.modelMapper.map(userDto, User.class);

        // encoded the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // roles
        //Role role = this.roleRepo.findById(AppConstants.NUSER).get();

        user.setRole(USER);

        User newUser = this.userRepo.save(user);

        return this.modelMapper.map(newUser, UserDto.class);
    }


    @Override
    public UserDto createUser(UserDto userDto) {
        User user=this.dtoToUser(userDto);
        User saveduser=this.userRepo.save(user);
        return this.userToDto(saveduser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user=this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());

        User updatedUser=this.userRepo.save(user);
        UserDto userDto1=this.userToDto(updatedUser);
        return userDto1;
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user=this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        List<UserDto> userDtos=users.stream().map(user-> this.userToDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user=this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));
        this.userRepo.delete(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByEmail(username);
    }

    private User dtoToUser(UserDto userDto)
    {
        User user=this.modelMapper.map(userDto,User.class);
        return user;

    }

    public UserDto userToDto(User user)
    {
        UserDto userDto=this.modelMapper.map(user,UserDto.class);
        return userDto;
    }

    public List<User> getAdminList() {
        return userRepo.getAdmins(Role.ADMIN);
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

}
