package main.threads;

import java.util.HashMap;
import java.util.Map;

import main.mp.Superette;
import main.mp.Chariot;
import main.mp.Rayon;
import main.products.ProductFactory;

public class ChefRayon extends Thread {
    Superette superette;
    // chariot du chef de rayon
    Chariot chariotChefRayon;

    public ChefRayon(Superette superette) {
        this.superette = superette;
        this.chariotChefRayon = new Chariot(getName());
    }

    /**
     * @ensure le chariot du chef de rayon est rempli
     */
    public void faireLePlein() {
        try {
            Thread.sleep(Superette.TEMPS_CHEF_RAYON_FAIRE_PLEIN);
        } catch (Exception e) {
        }
        for (String productName : Superette.availableProductsNames) {
            chariotChefRayon.setProduct(productName, Superette.MAX_CHEF_RAYON_CAPACITY);
        }
        System.out.println("Chef rayon a fais le plein .");
    }

    /**
     * @ensure les rayons sont remplis autant que possible
     */
    public void tournerRayons() {
        for (Rayon rayon : superette.getRangeeRayons().getRayons()) {
            int ancienStock = rayon.getProducts().size();
            int nbrAjouts = 0;

            try {
                Thread.sleep(Superette.TEMPS_CHEF_RAYON_ENTRE_RAYONS);
            } catch (Exception e) {
            }
            while (rayon.getProducts().size() < Superette.MAX_SIZE_RAYON
                    && chariotChefRayon.getProducts().get(rayon.getId()) > 0) {
                nbrAjouts++;
                rayon.ajouter(ProductFactory.createProduct(rayon.getId()));

                chariotChefRayon.decremente(rayon.getId());
            }
            System.out.println(
                    "ChefRayon---" + rayon.getId() + ": ancien stock: " + (ancienStock)
                            + " nouveau stock: " + rayon.getProducts().size() + " (" + nbrAjouts + " ajouté)");

        }
    }

    public void travailler() {
        faireLePlein();
        tournerRayons();
    }

    @Override
    public void run() {
        while (Superette.NBR_INIT_CLIENTS > superette.getNbrClientSortie()) {
            travailler();
        }
        System.out.println("chef rayon a fini son travail");
    }
}