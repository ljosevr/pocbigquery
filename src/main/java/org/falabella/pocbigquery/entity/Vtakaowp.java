package org.falabella.pocbigquery.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vtakaowp")
@Getter
@Setter
@Builder
@ToString
@IdClass(VtakaowpId.class)

public class Vtakaowp {
    @Column(name = "dproceso", length = 30, nullable = true)
    private String dproceso;

    @Column(name = "dcreacion", length = 30, nullable = true)
    private String dcreacion;

    @Id
    @Column(name = "status", length = 50, nullable = true)
    private String status;

    @Id
    @Column(name = "cvendedor", nullable = true)
    private Long cvendedor;

    @Column(name = "nom_vendedor", length = 200, nullable = true)
    private String nomVendedor;

    @Column(name = "crut", length = 25, nullable = true)
    private String crut;

    @Id
    @Column(name = "nordencmp", nullable = true)
    private Long nordencmp;

    @Id
    @Column(name = "lineid", length = 50, nullable = true)
    private String lineid;

    @Column(name = "invoicetype", length = 50, nullable = true)
    private String invoicetype;

    @Id
    @Column(name = "gid_lvl_number", length = 15, nullable = true)
    private String gidLvlNumber;

    @Id
    @Column(name = "sku", length = 15, nullable = true)
    private String sku;

    @Column(name = "gid_name_full", length = 250, nullable = true)
    private String gidNameFull;

    @Column(name = "mventa", precision = 13, scale = 2, nullable = true)
    private Double mventa;

    @Column(name = "mventa_sdescto", precision = 13, scale = 2, nullable = true)
    private Double mventaSdescto;

    @Column(name = "mneto", precision = 13, scale = 2, nullable = true)
    private Double mneto;

    @Column(name = "dactualiza", nullable = true)
    private LocalDate dactualiza;

    @Column(name = "dcompra", nullable = true)
    private LocalDate dcompra;

    public Vtakaowp() {
    }

}
