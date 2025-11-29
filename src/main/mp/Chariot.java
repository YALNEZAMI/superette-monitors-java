package main.mp;

import java.util.HashMap;
import java.util.Map;

public class Chariot {
    // identifiant du chariot
    private String id;
    // les nombre d'items dans le chariot en fonction de la catégory de produit
    private Map<String, Integer> products;

    public String getId() {
        return id;
    }

    public Chariot(String id) {
        this.id = id;
        products = new HashMap<String, Integer>();
        for (String productName : Superette.availableProductsNames) {
            products.put(productName, 0);
        }
    }

    /**
     * 
     * @param product nom de la catégorie du produit à modifier
     * @param nbr     nouveau nombre d'items dans le chariot
     */
    public void setProduct(String product, int nbr) {
        products.put(product, nbr);
    }

    /**
     * 
     * @param product nom de la catégorie du produit à modifier
     * @ensure le nombre d'items dans le chariot est decrémenté de 1
     */
    public void decremente(String product) {
        products.put(product, products.get(product) - 1);
    }

    /**
     * 
     * @param product nom de la catégorie du produit à modifier
     * @ensure le nombre d'items dans le chariot est incrémenté de 1
     */
    public void incremente(String product) {
        products.put(product, products.get(product) + 1);
    }

    public Map<String, Integer> getProducts() {
        return products;
    }
}
