package com.example.ordering.item.controller;

import com.example.ordering.common.CommonResponseDto;
import com.example.ordering.item.domain.Item;
import com.example.ordering.item.dto.ItemSearchDto;
import com.example.ordering.item.dto.Request.ItemReqDto;
import com.example.ordering.item.dto.Response.ItemResDto;
import com.example.ordering.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/item/create")
    public ResponseEntity<CommonResponseDto> ItemCreate(ItemReqDto itemReqDto) {
        Item item = itemService.create(itemReqDto);

        return new ResponseEntity<>(
                new CommonResponseDto(HttpStatus.OK, "item successfully create", item.getId())
                , HttpStatus.OK);
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResDto>> ItemList(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<ItemResDto> itemResDtos = itemService.findAll(itemSearchDto, pageable);

        return new ResponseEntity<>(itemResDtos, HttpStatus.OK);
    }

    @GetMapping("/item/{id}/image")
    public ResponseEntity<Resource> getItemImage(@PathVariable Long id) {
        Resource resource = itemService.getImage(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/item/{id}/update")
    public ResponseEntity<CommonResponseDto> ItemUpdate(@PathVariable Long id, ItemReqDto itemReqDto) {
        Item item = itemService.update(id, itemReqDto);
        return new ResponseEntity<>(
                new CommonResponseDto(HttpStatus.OK, "item successfully updated", item.getId())
                , HttpStatus.OK);    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/item/{id}/delete")
    public ResponseEntity<CommonResponseDto> ItemDelete(@PathVariable Long id) {
        Item item = itemService.itemDelete(id);
        return new ResponseEntity<>(
                new CommonResponseDto(HttpStatus.OK, "item successfully delete", item.getId())
                , HttpStatus.OK);
    }

}
