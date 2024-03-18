package org.falabella.pocbigquery.entity;

import java.io.Serializable;

public class VtakaowpId implements Serializable {
    private String status;
    private Long cvendedor;
    private Long nordencmp;
    private String lineid;
    private String gidLvlNumber;

    public VtakaowpId() {
    }
    public VtakaowpId(String status, Long cvendedor, Long nordencmp, String lineid, String gidLvlNumber) {
        this.status = status;
        this.cvendedor = cvendedor;
        this.nordencmp = nordencmp;
        this.lineid = lineid;
        this.gidLvlNumber = gidLvlNumber;
    }
}
