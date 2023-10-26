package co.grtk.um.dto;

import java.time.LocalDateTime;

public record BeanInitDTO(String beanName, long elapsed, LocalDateTime start, LocalDateTime end, long gap) {}
