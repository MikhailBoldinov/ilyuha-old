package main.beans;

import core.utils.Utils;

import java.util.List;

import static main.ProductGenerator.DASH;
import static main.ProductGenerator.DOT;
import static main.ProductGenerator.SLASH;

/**
 * @author Mikhail Boldinov
 */
public class ProductResult {

    private String producer;
    private String serialNumber;
    private String shortDescription;
    private String description;
    private String imageExtension;

    private List<Code> productCodes;

    public String getProducer_serialNumber() {
        return Utils.buildString(producer, SLASH, serialNumber);
    }

    public String getModel(String code) {
        return Utils.buildString(serialNumber, DASH, code);
    }

    public String getProducer_model(String code) {
        return Utils.buildString(producer, DASH, serialNumber, DASH, code);
    }

    public String getProducer() {
        return producer;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription(String code, String decoding) {
        String description = this.description;
        description = description.replace("%code%", code);
        description += decoding;
        return description;
    }

    public String getImage() {
        return Utils.buildString(serialNumber, DOT, imageExtension);
    }

    public List<Code> getProductCodes() {
        return productCodes;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageExtension(String imageExtension) {
        this.imageExtension = imageExtension;
    }

    public void setProductCodes(List<Code> productCodes) {
        this.productCodes = productCodes;
    }
}
