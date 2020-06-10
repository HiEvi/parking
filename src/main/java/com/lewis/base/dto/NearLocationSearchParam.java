package com.lewis.base.dto;

import java.math.BigDecimal;

public class NearLocationSearchParam {
    private BigDecimal targetLng;
    private BigDecimal targetLat;

    private BigDecimal minLng;
    private BigDecimal maxLng;
    private BigDecimal minLat;
    private BigDecimal maxLat;

    private Integer start;
    private Integer limit;

    public BigDecimal getTargetLng() {
        return targetLng;
    }

    public void setTargetLng(BigDecimal targetLng) {
        this.targetLng = targetLng;
    }

    public BigDecimal getTargetLat() {
        return targetLat;
    }

    public void setTargetLat(BigDecimal targetLat) {
        this.targetLat = targetLat;
    }

    public BigDecimal getMinLng() {
        return minLng;
    }

    public void setMinLng(BigDecimal minLng) {
        this.minLng = minLng;
    }

    public BigDecimal getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(BigDecimal maxLng) {
        this.maxLng = maxLng;
    }

    public BigDecimal getMinLat() {
        return minLat;
    }

    public void setMinLat(BigDecimal minLat) {
        this.minLat = minLat;
    }

    public BigDecimal getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(BigDecimal maxLat) {
        this.maxLat = maxLat;
    }
    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
