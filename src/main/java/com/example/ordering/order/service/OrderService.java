package com.example.ordering.order.service;

import com.example.ordering.item.domain.Item;
import com.example.ordering.item.repository.ItemRepository;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import com.example.ordering.order.domain.OrderStatus;
import com.example.ordering.order.domain.Ordering;
import com.example.ordering.order.dto.Request.OrderReqDto;
import com.example.ordering.order.dto.Response.OrderResDto;
import com.example.ordering.order.repository.OrderRepository;
import com.example.ordering.order_item.domain.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    public Ordering create(OrderReqDto orderReqDto) {
        Member member = findEmailMember();

        Ordering ordering = Ordering.builder()
                .member(member)
                .build();

        for (OrderReqDto.OrderItemReqDto dto : orderReqDto.getOrderItemReqDtos()) {
            Item item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new EntityNotFoundException("item not found"));
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .quantity((dto.getCount()))
                    .ordering(ordering)
                    .build();
            ordering.getOrderItems().add(orderItem);
            if (item.getStockQuantity() - dto.getCount() < 0) {
                throw new IllegalArgumentException("재고가 없습니다.");
            }
            orderItem.getItem().UpdateStockQuantity(item.getStockQuantity() - dto.getCount());

        }
        Ordering save = orderRepository.save(ordering);
        return save;
    }

    public Ordering cancel(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean roleAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Member member = findEmailMember();
        Ordering ordering = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("order not found"));

        if (!ordering.getMember().getId().equals(member.getId()) && !roleAdmin) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        if (ordering.getOrderStatus().equals(OrderStatus.CANCELED)) {
            throw new IllegalArgumentException("이미 취소된 주문입니다.");
        }
        ordering.orderCancel();
        for (OrderItem orderItem : ordering.getOrderItems()) {
            orderItem.getItem().UpdateStockQuantity((orderItem.getItem().getStockQuantity() + orderItem.getQuantity()));
        }

        return ordering;
    }

    public Member findEmailMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("member not found"));
    }

    public List<OrderResDto> findAll() {
        List<Ordering> all = orderRepository.findAll();
        return all.stream().map((OrderResDto::toDto)).collect(Collectors.toList());
    }

    public List<OrderResDto> findByMember(Long id) {
        List<Ordering> ordering = orderRepository.findByMemberId(id);
        return ordering.stream().map(OrderResDto::toDto).collect(Collectors.toList());
    }

    public List<OrderResDto> findMyOrders() {
        Member member = findEmailMember();

        List<Ordering> ordering = member.getOrdering();
        return ordering.stream().map(OrderResDto::toDto).collect(Collectors.toList());
    }
}
