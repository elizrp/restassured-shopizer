import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

public class ShippingPackage {

    private String code;
    private String itemName;
    private int shippingHeight;
    private int shippingLength;
    private int shippingMaxWeight;
    private int shippingQuantity;
    private int shippingWeight;
    private int shippingWidth;
    private int treshold;
    private String type;

    public ShippingPackage() {
        this.code = "code" + ThreadLocalRandom.current().nextInt();
        this.itemName = RandomStringUtils.random(10, true, true);
        this.shippingHeight = ThreadLocalRandom.current().nextInt();
        this.shippingLength = ThreadLocalRandom.current().nextInt();
        this.shippingMaxWeight = ThreadLocalRandom.current().nextInt();
        this.shippingQuantity = ThreadLocalRandom.current().nextInt();
        this.shippingWeight = ThreadLocalRandom.current().nextInt();
        this.shippingWidth = ThreadLocalRandom.current().nextInt();
        this.treshold = ThreadLocalRandom.current().nextInt();
        this.type = "ITEM";
    }

    public String getCode() {
        return code;
    }
}
