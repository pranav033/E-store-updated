package com.springboot.estore.Estore.helpers;

import com.springboot.estore.Estore.converters.UserConverter;
import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.dtos.UserDto;
import com.springboot.estore.Estore.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {


    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type)
    {
        List<U> entity = page.getContent();
        List<V> userDtos = entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());
        PageableResponse<V> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(userDtos);
        pageableResponse.setPageNumber(page.getNumber());
        pageableResponse.setPageSize(page.getSize());
        pageableResponse.setTotalPages(page.getTotalPages());
        pageableResponse.setTotalElements(page.getTotalElements());
        pageableResponse.setLastPage(page.isLast());
        return pageableResponse;

    }
}
