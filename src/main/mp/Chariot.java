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

    // pas de contrainte pour le nombre(sinon conflit avec les besoin des clients)
    public void setProduct(String product, int nbr) {
        products.put(product, nbr);
    }

    public void decremente(String product) {
        products.put(product, products.get(product) - 1);
    }

    public void incremente(String product) {
        products.put(product, products.get(product) + 1);
    }

    public Map<String, Integer> getProducts() {
        return products;
    }
}
