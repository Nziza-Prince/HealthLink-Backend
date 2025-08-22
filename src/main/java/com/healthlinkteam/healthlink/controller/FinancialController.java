package com.healthlinkteam.healthlink.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/financials")
@RequiredArgsConstructor
public class FinancialController {
    private final FinancialService finance;


    @GetMapping
    public FinancialsDTO get() {
        return new FinancialsDTO(
                finance.totalRevenue(),
                finance.totalExpenses(),
                finance.totalProfit(),
                finance.overduePaymentsCount()
        );
    }
}