package com.spurspro.option.strategy;

import com.spurspro.option.dto.option.ETFDailyPrice;
import com.spurspro.option.dto.option.OptionDailyData;
import com.spurspro.option.strategy.map.BackTestParams;
import com.spurspro.option.strategy.map.BacktestResult;

import java.util.List;

public interface Strategy {
    BacktestResult backTest(BackTestParams params) throws Exception;
}
