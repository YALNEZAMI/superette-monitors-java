package main;

import java.util.LinkedList;
import java.util.List;
import interfaces.RayonI;

public class RangeeRayons {
    Rayon[] rayons = new Rayon[Superette.availableProductsNames.length];

    public RangeeRayons() {
        for (int i = 0; i < rayons.length; i++) {
            rayons[i] = new Rayon(Superette.availableProductsNames[i]);
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

        private String id;
        // private int nbProduits=MAX_SIZE;
        private List<Product> products;

        public List<Product> getProducts() {
            return products;
        }

        @Override
        synchronized public void ajouter(Product p) {
            // while (products.size() == max_size) {
            // try {
            // wait();
            // } catch (Exception e) {
            // }
            // }
            if (products.size() <= Superette.MAX_SIZE_RAYON) {
                products.add(p);
                notifyAll();
            }

        }

        synchronized public void prendre(Client client) {
            while (products.size() == 0) {
                try {
                    System.out.println(client.getName() + " attend le remplissage du rayon " + id);
                    wait();
                } catch (Exception e) {
                }
            }
            products.remove(0);
            // notifyAll();
        }

        public String getId() {
            return id;
        }

        public Rayon(String id) {
            this.id = id;
            products = new LinkedList<Product>();
            for (int i = 0; i < Superette.MAX_SIZE_RAYON / 2; i++) {
                products.add(ProductFactory.createProduct(id));

            }
        }

    }

}
