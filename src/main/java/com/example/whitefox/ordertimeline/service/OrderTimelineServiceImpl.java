package com.example.whitefox.ordertimeline.service;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.ordertimeline.dto.TimelineResponse;
import com.example.whitefox.ordertimeline.entity.OrderTimeline;
import com.example.whitefox.ordertimeline.repository.OrderTimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderTimelineServiceImpl
        implements OrderTimelineService {

    private final OrderTimelineRepository repository;

    @Override
    public void addTimeline(
            LaundryOrder order,
            OrderStatus status,
            String action,
            String remarks,
            UUID userId,
            String userName,
            String module
    ) {

        repository.save(

                OrderTimeline.builder()
                        .order(order)
                        .status(status)
                        .action(action)
                        .remarks(remarks)
                        .userId(userId)
                        .userName(userName)
                        .module(module)
                        .actionTime(LocalDateTime.now())
                        .build()

        );

    }

    @Override
    public List<TimelineResponse> getTimeline(UUID orderId) {

        return repository.findByOrderIdOrderByActionTimeAsc(orderId)

                .stream()

                .map(t ->

                        TimelineResponse.builder()

                                .id(t.getId())

                                .status(t.getStatus())

                                .action(t.getAction())

                                .remarks(t.getRemarks())

                                .userId(t.getUserId())

                                .userName(t.getUserName())

                                .module(t.getModule())

                                .actionTime(t.getActionTime())

                                .build()

                )

                .toList();
    }
}