package main.threads;

import java.util.HashMap;
import java.util.Map;

import main.mp.Superette;
import main.mp.RangeeRayons.Rayon;
import main.products.ProductFactory;

public class ChefRayon extends Thread {
    Superette superette;
    Map<String, Integer> chariotChefRayon;

    public ChefRayon(Superette superette) {
        this.superette = superette;
        this.chariotChefRayon = new HashMap<>();
    }

    public void faireLePlein() {
        try {
            Thread.sleep(Superette.TEMPS_CHEF_RAYON_FAIRE_PLEIN);
        } catch (Exception e) {
        }
        for (String productName : Superette.availableProductsNames) {
            chariotChefRayon.put(productName, Superette.MAX_CHEF_RAYON_CAPACITY);
        }
        // for (String str : Superette.availableProductsNames) {
        // System.out.println(chariotChefRayon.get(str));
        // ;
        // }
        System.out.println("chef rayon a fais le plein .");
    }

    public void tournerRayons() {

        for (Rayon rayon : superette.getRangeeRayons().getRayons()) {
            int ancienStock = rayon.getProducts().size();
            int nbrAjouts = 0;

            try {
                Thread.sleep(Superette.TEMPS_CHEF_RAYON_ENTRE_RAYONS);
            } catch (Exception e) {
            }
            // System.out
            // .println(" chef rayon visite " + rayon.getId() + " dont stock est à " +
            // rayon.getProducts().size());
            while (rayon.getProducts().size() < Superette.MAX_SIZE_RAYON
                    && chariotChefRayon.get(rayon.getId()) > 0) {
                nbrAjouts++;
                rayon.ajouter(ProductFactory.createProduct(rayon.getId()));

                chariotChefRayon.put(rayon.getId(), chariotChefRayon.get(rayon.getId()) - 1);
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