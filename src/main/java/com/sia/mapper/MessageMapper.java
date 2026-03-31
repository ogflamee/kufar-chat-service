package com.sia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.sia.dto.MessageDTO;
import com.sia.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "sender", target = "senderId")
    @Mapping(source = "receiver", target = "receiverId")
    @Mapping(source = "ad", target = "adId")
    MessageDTO toDTO(Message message);

    @Mapping(source = "senderId", target = "sender")
    @Mapping(source = "receiverId", target = "receiver")
    @Mapping(source = "adId", target = "ad")
    Message toEntity(MessageDTO dto);
}
