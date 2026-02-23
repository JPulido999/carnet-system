package com.zkteco.apizk.dto;

public class AccesosPorDispositivoDTO {

    private String devAlias;
    private Long total;

    public AccesosPorDispositivoDTO(String devAlias, Long total) {
        this.devAlias = devAlias;
        this.total = total;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public Long getTotal() {
        return total;
    }
}
