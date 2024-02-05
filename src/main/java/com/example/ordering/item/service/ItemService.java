package com.example.ordering.item.service;

import com.example.ordering.item.domain.Item;
import com.example.ordering.item.dto.ItemSearchDto;
import com.example.ordering.item.dto.Request.ItemReqDto;
import com.example.ordering.item.dto.Response.ItemResDto;
import com.example.ordering.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(ItemReqDto itemReqDto) {
        MultipartFile multipartFile = itemReqDto.getItemImage();
        String fileName = multipartFile.getOriginalFilename();
        Item item = Item.builder()
                .name(itemReqDto.getName())
                .category(itemReqDto.getCategory())
                .price(itemReqDto.getPrice())
                .stockQuantity(itemReqDto.getStockQuantity())
                .build();
        Item saveItem = itemRepository.save(item);
        Path path = Paths.get("/Users/m1/Desktop/temp", item.getId() + "_" + fileName);
        saveItem.setImagePath(path.toString());

        try {
            byte[] bytes = multipartFile.getBytes();
            Files.write(path, bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalArgumentException("image not available");
        }
        return item;
    }

    public Item itemDelete(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("item not found"));
        item.deleteItem();
        return item;
    }

    public Resource getImage(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("item not found"));
        String imagePath = item.getImagePath();
        Path path = Paths.get(imagePath);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url form is not valid");
        }
        return resource;
    }

    public Item update(Long id, ItemReqDto itemReqDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("item not found"));

        MultipartFile multipartFile = itemReqDto.getItemImage();
        String fileName = multipartFile.getOriginalFilename();
        Path path = Paths.get("/Users/m1/Desktop/temp", item.getId() + "_" + fileName);
        item.updateItem(itemReqDto.getName(), itemReqDto.getCategory(), itemReqDto.getPrice(), itemReqDto.getStockQuantity(), path.toString());

        try {
            byte[] bytes = multipartFile.getBytes();
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalArgumentException("image not available");
        }
        return item;
    }

    public List<ItemResDto> findAll(ItemSearchDto itemSearchDto, Pageable pageable) {
//        검색을 위해 Specification 객체 사용
//        Specification 객체는 복잡한 쿼리를 명세를 이용, 정의하여 쉽게 생성
        Specification<Item> spec = new Specification<Item>() {
            @Override
//            root : 엔티티의 속성에 접근하기 위한 객체, , builder 쿼리를 생성하기 위한 객체
            public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (itemSearchDto.getName() != null) {
                    predicates.add(criteriaBuilder
                            .like(root.get("name"), "%" + itemSearchDto.getName() + "%"));
                }
                if (itemSearchDto.getCategory() != null) {
                    predicates.add(criteriaBuilder
                            .like(root.get("category"), "%" + itemSearchDto.getCategory() + "%"));
                }
                if (itemSearchDto.getDelYN() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("delYn"), "N"));
                }
                Predicate[] predicateArr = new Predicate[predicates.size()];
                for (int i = 0; i < predicates.size(); i++) {
                    predicateArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }

        };
        Page<Item> items = itemRepository.findAll(spec, pageable);
        List<Item> itemList = items.getContent();
        List<ItemResDto> resDtos = itemList.stream().map(i -> ItemResDto.builder()
                .id(i.getId())
                .name(i.getName())
                .price(i.getPrice())
                .category(i.getCategory())
                .stockQuantity(i.getStockQuantity())
                .imagePath(i.getImagePath())
                .build()).collect(Collectors.toList());
        return resDtos;
    }
}
