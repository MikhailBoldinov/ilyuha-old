package main.service;

import core.utils.Utils;
import main.beans.Code;
import main.beans.Group;
import main.beans.Product;
import main.beans.ProductResult;

import java.util.ArrayList;
import java.util.List;

import static main.ProductGenerator.DASH;
import static main.ProductGenerator.NEW_LINE;
import static main.ProductGenerator.SPACE;

/**
 * @author Mikhail Boldinov
 */
public class ProductProcessor {

    private Product product;

    public ProductProcessor(Product product) {
        this.product = product;
    }

    public ProductResult process() {
        ProductResult productResult = new ProductResult();
        productResult.setProducer(product.getProducer());
        productResult.setSerialNumber(product.getSerialNumber());
        productResult.setShortDescription(product.getShortDescription());
        productResult.setDescription(product.getDescription());
        productResult.setImageExtension(product.getImageExtension());
        productResult.setProductCodes(getProductCodes(product.getGroups()));
        return productResult;
    }

    private List<Code> getProductCodes(List<Group> groups) {
        List<Code> productCodes = new ArrayList<>();
        boolean first = true;
        while (hasNext(groups)) {
            if (!first) {
                next(groups);
            }

            Code productCode = getProductCode(groups);
            productCodes.add(productCode);
            first = false;
        }
        return productCodes;
    }

    private Code getProductCode(List<Group> groups) {
        String codeKey = "";
        String decoding = "";
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            Code code = group.getCodes().get(group.getPosition());
            codeKey += code.getKey();
            if (i < groups.size() - 1) {
                codeKey += group.getSeparator();
            }
            decoding += Utils.buildString(NEW_LINE, group.getName(), SPACE, DASH, SPACE, code.getValue());
        }
        return new Code(codeKey, decoding);
    }

    private boolean hasNext(List<Group> groups) {
        for (Group group : groups) {
            if (group.getPosition() < group.getCodes().size() - 1) {
                return true;
            }
        }
        return false;
    }

    private void next(List<Group> groups) {
        for (int i = groups.size() - 1; i >= 0; i--) {
            Group group = groups.get(i);
            if (group.incrementPosition()) {
                break;
            }
        }
    }
}
