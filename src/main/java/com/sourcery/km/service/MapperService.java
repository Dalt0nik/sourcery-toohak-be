package com.sourcery.km.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class MapperService {
    @Autowired
    ModelMapper modelMapper;

    public <T, K> List<K> mapList(List<T> list, Class<K> targetClass) {
        return list.stream().map(item -> modelMapper.map(item, targetClass)).toList();
    }

    public <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }
}
