package com.teclead.ventures.javatask.service;

import com.teclead.ventures.javatask.dto.RequestUserDto;
import com.teclead.ventures.javatask.dto.ResponseUserDto;
import com.teclead.ventures.javatask.entity.User;
import com.teclead.ventures.javatask.exception.UserNotFoundException;
import com.teclead.ventures.javatask.mapper.UserMapper;
import com.teclead.ventures.javatask.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper mapper;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = Mappers.getMapper(UserMapper.class);
    }

    /**
     * the method searches for all users that exist in the database and returns them
     *
     * @return all users or a empty list
     */
    public List<ResponseUserDto> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().filter(Objects::nonNull).map(mapper::map).toList();
    }

    /**
     * the method searches for all users whose vorname was requested and returns them
     *
     * @param vorname String
     * @return all users whose vorname was requested or a empty list
     */
    public List<ResponseUserDto> getAllUsersByVorname(String vorname) {
        String upperCaseVorname = vorname.toUpperCase();

        List<User> users = (List<User>) userRepository.findAll();
        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        List<User> usersWithCurrenVorname = users
                .stream()
                .filter(Objects::nonNull)
                .filter(user -> user.getVorname().toUpperCase().equals(upperCaseVorname)).toList();

        return usersWithCurrenVorname.stream().map(mapper::map).toList();
    }

    /**
     * the method searches the user with the requested id
     * @param id Long
     * @return user with the requested id
     * @throws UserNotFoundException if it is not exist
     */
    public ResponseUserDto getUserById(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(buildExceptionMessage(id)));

        return mapper.map(user);
    }

    /**
     * the method saves new user
     * @param userDto RequestUserDto
     * @return saved user
     */
    public ResponseUserDto addUser(RequestUserDto userDto) {
        User user = userRepository.save(mapper.map(userDto));
        return mapper.map(user);
    }

    /**
     * the method updates info of the user with the requested id
     * @param id Long
     * @param userDto RequestUserDto
     * @throws UserNotFoundException if user with id is not exist
     */
    public void updateUser(Long id, RequestUserDto userDto) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(buildExceptionMessage(id)));
        user.setName(userDto.getName());
        user.setVorname(userDto.getVorname());
        user.setEmail(userDto.getEmail());

        userRepository.save(user);
    }

    /**
     * the method deletes a user with the requested id
     * @param id Long
     * @throws UserNotFoundException if the user is not exist
     */
    public void deleteUser(Long id) {
        userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(buildExceptionMessage(id)));
        userRepository.deleteById(id);
    }

    private String buildExceptionMessage(Long id) {
        return "User with id " + id + " doesn't exist";
    }
}
