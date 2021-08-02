package portfolioManagement;

public class SupplyPoint {

    private String ean;
    private Energy energy;
    private String supplierName;

    public SupplyPoint(String ean, String supplierName){
        this.ean = ean;
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "SupplyPoint{" +
                "ean='" + ean + '\'' +
                ", energy=" + energy +
                ", supplierName='" + supplierName + '\'' +
                '}';
    }

    public Energy getEnergy() {
        return energy;
    }

    public String getEan() {
        return ean;
    }

    public String getSupplierName() {
        return supplierName;
    }
}
