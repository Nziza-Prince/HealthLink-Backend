package com.healthlinkteam.healthlink.dto;



import java.math.BigDecimal;


public record FinancialsDTO(BigDecimal totalRevenue,
                            BigDecimal totalExpenses,
                            BigDecimal totalProfit,
                            long overduePayments) { }
