package mapper;

import dto.UserDTO;
import entites.UserEntity;
import enums.EGender;

public class UserMapper {
    public static UserDTO toDTO(UserEntity userEntity) {

        if (userEntity == null) {
            return null;
        }

        UserDTO dto = new UserDTO();

        dto.setUserName(userEntity.getUserName());

        dto.setGender(userEntity.getGender());

        dto.setFirstName(userEntity.getFirstName());

        dto.setLastName(userEntity.getLastName());


        return dto;
    }


    public static UserEntity toEntity(UserDTO dto) {

        if (dto == null) {
            return null;
        }

        return new UserEntity.Builder()
                .userName(dto.getUserName())
                .gender(dto.getGender())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }


    private static EGender convertGender(String gender) {

        if (gender == null || gender.isBlank()) {
            return EGender.MALE;
        }

        try {
            return EGender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EGender.MALE;
        }
    }

}