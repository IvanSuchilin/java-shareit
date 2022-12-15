package ru.practicum.shareit.user.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface UserRepositoryMapper {
    UserRepositoryMapper INSTANCE = Mappers.getMapper(UserRepositoryMapper.class);

    User toUser(UserDto userDto);
     UserDto toUserDto(User user);
@Mapping(target = "id", ignore = true)
     void updateUser (UserDto userDto, @MappingTarget User user);
}
