package com.seathub.schedule.service;

import com.seathub.common.api.ErrorCode;
import com.seathub.common.exception.BusinessException;
import com.seathub.product.domain.Product;
import com.seathub.product.repository.ProductRepository;
import com.seathub.schedule.domain.Schedule;
import com.seathub.schedule.dto.CreateScheduleRequest;
import com.seathub.schedule.dto.ScheduleResponse;
import com.seathub.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private final ProductRepository productRepository;
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ProductRepository productRepository, ScheduleRepository scheduleRepository) {
        this.productRepository = productRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public ScheduleResponse createSchedule(Long productId, CreateScheduleRequest request) {
        validateScheduleTime(request);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        Schedule schedule = Schedule.create(product, request.startAt(), request.endAt());

        return ScheduleResponse.from(scheduleRepository.save(schedule));
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedules(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return scheduleRepository.findByProductIdOrderByStartAtAsc(productId).stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    private void validateScheduleTime(CreateScheduleRequest request) {
        if (!request.endAt().isAfter(request.startAt())) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
    }
}
