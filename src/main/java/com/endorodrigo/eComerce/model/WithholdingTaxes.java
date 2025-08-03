package com.endorodrigo.eComerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class WithholdingTaxes {

    @Id
    private int code;
    private boolean withholding_tax_rate;

    public WithholdingTaxes() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isWithholding_tax_rate() {
        return withholding_tax_rate;
    }

    public void setWithholding_tax_rate(boolean withholding_tax_rate) {
        this.withholding_tax_rate = withholding_tax_rate;
    }

    @Override
    public String toString() {
        return "WithholdingTaxes{" +
                "code=" + code +
                ", withholding_tax_rate=" + withholding_tax_rate +
                '}';
    }
}
