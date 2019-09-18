package com.syhb.project.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String productcode;

    private String referencenumber;

    @Basic(optional = false)
    @Column(name = "date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private double credit;

    private boolean status;

    public Product() {
    }

    public Product(String productCode, String referenceNumber, double credit) {
        this.productcode = productCode;
        this.referencenumber = referenceNumber;
        this.credit = credit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductCode() {
        return productcode;
    }

    public void setProductCode(String productCode) {
        this.productcode = productCode;
    }

    public String getReferenceNumber() {
        return referencenumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referencenumber = referenceNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
