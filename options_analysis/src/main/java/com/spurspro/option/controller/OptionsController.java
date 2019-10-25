package com.spurspro.option.controller;

import com.spurspro.option.dto.AnalysisRequest;
import com.spurspro.option.dto.AnalysisResponse;
import com.spurspro.option.service.OptionsAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * DataImport Controller
 * Created by Jimmy.Liu on 2018-09-18 06:59
 *
 * @since 2.01.06
 */
@RestController
public class OptionsController {

    private static final Logger logger = LoggerFactory.getLogger(OptionsController.class);

    @Autowired
    private OptionsAnalysisService optionsAnalysisService;

    @RequestMapping(path = "/optionAnalysis", method = RequestMethod.POST)
    public AnalysisResponse analysisOptions(@RequestBody final AnalysisRequest request) {
        AnalysisResponse res = new AnalysisResponse();
        long begin = System.currentTimeMillis();
        try {
            logger.info("Begin to analysis options strategy: " + request);
            res = optionsAnalysisService.analysisOptions(request);
            res.setConsumingTime((System.currentTimeMillis() - begin));
            logger.info("Total execution time：" + (System.currentTimeMillis() - begin));
        } catch (Exception e) {
            res.setMessage("Analysis Error!");
            res.setSuccess(Boolean.FALSE);
            logger.error("Analysis Error!", e);
        }
        return res;
    }


}
