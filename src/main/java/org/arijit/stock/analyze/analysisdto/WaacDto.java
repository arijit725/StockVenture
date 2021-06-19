package org.arijit.stock.analyze.analysisdto;

public class WaacDto {
    double incomeTaxExpense;
    double incomeBeforeTax;
    double riskFreeRate;
    double marketReturn;
    double discountRate;

    public double getIncomeTaxExpense() {
        return incomeTaxExpense;
    }

    public void setIncomeTaxExpense(double incomeTaxExpense) {
        this.incomeTaxExpense = incomeTaxExpense;
    }

    public double getIncomeBeforeTax() {
        return incomeBeforeTax;
    }

    public void setIncomeBeforeTax(double incomeBeforeTax) {
        this.incomeBeforeTax = incomeBeforeTax;
    }

    public double getRiskFreeRate() {
        return riskFreeRate;
    }

    public void setRiskFreeRate(double riskFreeRate) {
        this.riskFreeRate = riskFreeRate;
    }

    public double getMarketReturn() {
        return marketReturn;
    }

    public void setMarketReturn(double marketReturn) {
        this.marketReturn = marketReturn;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public String toString() {
        return "WaacDto{" +
                "incomeTaxExpense=" + incomeTaxExpense +
                ", incomeBeforeTax=" + incomeBeforeTax +
                ", riskFreeRate=" + riskFreeRate +
                ", marketReturn=" + marketReturn +
                '}';
    }
}
