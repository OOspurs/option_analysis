package com.spurspro.option.dto;

import com.spurspro.option.dto.BaseResponse;

import java.io.Serializable;

public class AnalysisResponse extends BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long consumingTime;

    public Long getConsumingTime() {
        return consumingTime;
    }

    public void setConsumingTime(Long consumingTime) {
        this.consumingTime = consumingTime;
    }
}
