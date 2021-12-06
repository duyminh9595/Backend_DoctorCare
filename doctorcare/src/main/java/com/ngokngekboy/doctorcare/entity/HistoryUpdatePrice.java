package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "history_update_price_thuoc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryUpdatePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "thuoc_id", nullable = false)
    private Thuoc thuoc;

    @Column(name = "date_update_pricenew")
    private Date date_update_pricenew;

    @Column(name = "old_price")
    private float old_price;

    @Column(name = "new_price")
    private float new_price;
}
