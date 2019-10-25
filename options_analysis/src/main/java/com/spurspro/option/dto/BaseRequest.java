package com.spurspro.option.dto;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String shopId;
    private String trNo;
    private String businessCode;

    /**
     * 门店Id列表
     * @Since 2.00.00
     */
    private List<String> shopIds;

    public String getShopId() {
        if (StringUtils.isBlank(shopId) && shopIds != null && shopIds.size() > 0) {
            shopId = shopIds.get(0);
        }
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTrNo() {
        return trNo;
    }

    public void setTrNo(String trNo) {
        this.trNo = trNo;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getTransactionDataPath() {
        return "/" + this.trNo + "/" + this.businessCode;
    }

    public List<String> getShopIds() {
        if (StringUtils.isNotBlank(shopId)) {
            if (shopIds == null) {
                shopIds = new ArrayList<>();
                shopIds.add(shopId);
            } else if (shopIds.size() == 0) {
                shopIds.add(shopId);
            }
        }
        return shopIds;
    }

    public void setShopIds(List<String> shopIds) {
        this.shopIds = shopIds;
    }

    public void validateShops() throws Exception{
        if (this.getShopIds() == null || this.getShopIds().size() <= 0) {
            throw new Exception("门店信息不能为空");
        }
    }
}
