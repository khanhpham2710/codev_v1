package service;

import dto.UserDTO;
import entites.UserEntity;
import mapper.UserMapper;
import respositories.UserRepository;

import java.util.UUID;

public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean create(UserDTO dto) {

        UserEntity entity = UserMapper.toEntity(dto);

        return repository.save(entity);
    }

    public boolean update(UserDTO dto) {

        UserEntity entity = UserMapper.toEntity(dto);

        return repository.update(entity);
    }

    public boolean delete(UUID userId) throws Exception {
        UserEntity entity = repository.findById(userId);

        if (entity != null){
            return repository.delete(userId);
        } else {
            throw new Exception("User not found");
        }
    }
}