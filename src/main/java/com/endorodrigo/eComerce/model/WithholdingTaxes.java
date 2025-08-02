package com.endorodrigo.eComerce.model;

public class WithholdingTaxes {
    private String code;
    private boolean withholding_tax_rate;

    public WithholdingTaxes() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
                ", withholding_tax_rate='" + withholding_tax_rate + '\'' +
                '}';
    }
}
