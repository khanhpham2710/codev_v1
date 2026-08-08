package service;

import dto.UserDTO;
import entites.UserEntity;
import mapper.UserMapper;
import respositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository repository;

    public UserService() {
        this.repository = new UserRepository();
    }

    public UserDTO create(UserEntity entity) {
        if (repository.save(entity)) {
            return UserMapper.toDTO(entity);
        }
        return null;
    }

    public boolean update(UserDTO dto) {

        UserEntity entity = UserMapper.toEntity(dto);

        return repository.update(entity);
    }

    public boolean delete(UUID userId) throws Exception {
        Optional<UserEntity> entity = repository.findById(userId);

        if (entity.isPresent()){
            return repository.delete(userId);
        } else {
            throw new Exception("User not found");
        }
    }

    public Optional<UserEntity> findByUsername(String userName){

        return repository.findByUsername(userName);
    }
}