package com.spurspro.option.service;

import com.spurspro.option.dto.AnalysisRequest;
import com.spurspro.option.dto.AnalysisResponse;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Analysis options
 *
 * @since 02.12.03
 */
public interface OptionsAnalysisService {

    AnalysisResponse analysisOptions(final AnalysisRequest request) throws Exception;

}
