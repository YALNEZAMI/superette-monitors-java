package main;

import java.util.LinkedList;
import java.util.List;
import interfaces.RayonI;

public class RangeeRayons {
    Rayon[] rayons = new Rayon[Superette.availableProductsNames.length];

    public RangeeRayons() {
        for (int i = 0; i < rayons.length; i++) {
            rayons[i] = new Rayon(Superette.availableProductsNames[i], (i + 10));
        }
    }

    public Rayon[] getRayons() {
        return rayons;
    }

    @Override
    public String toString() {
        String s = "";
        for (Rayon rayon : rayons) {
            s += rayon.getId() + " : " + rayon.getProducts().size() + " items.";
        }
        return s;
    }

    private class Rayon implements RayonI {
        private int max_size = 20;// TODO demander au prof combien

        private String id;
        // private int nbProduits=MAX_SIZE;
        private List<Product> products;

        public List<Product> getProducts() {
            return products;
        }

        @Override
        synchronized public void ajouter(Product p) {
            while (products.size() == max_size) {
                try {
                    wait();
                } catch (Exception e) {
                }
            }
            products.add(p);
            notifyAll();
        }

        synchronized public void prendre() {
            while (products.size() == 0) {
                try {
                    wait();
                } catch (Exception e) {
                }
            }
            products.remove(0);
            notifyAll();
        }

        public String getId() {
            return id;
        }

        public int getMax_size() {
            return max_size;
        }

        public Rayon(String id, int max_size) {
            this.id = id;
            this.max_size = max_size;
            products = new LinkedList<Product>();
            for (int i = 0; i < max_size / 2; i++) {
                products.add(ProductFactory.createProduct(id));

            }
        }

    }

}
