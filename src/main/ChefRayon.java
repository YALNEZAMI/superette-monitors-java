package main;

import java.util.HashMap;
import java.util.Map;

import interfaces.RayonI;

public class ChefRayon extends Thread {
    Superette superette;
    Map<String, Integer> chariotChefRayon;

    public ChefRayon(Superette superette) {
        this.superette = superette;
        this.chariotChefRayon = new HashMap<>();
    }

    public void faireLePlein() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
        for (String productName : Superette.availableProductsNames) {
            chariotChefRayon.put(productName, 5);
        }
        System.out.println("chef rayon a fais le plein .");
    }

    public void tournerRayons() {
        for (RayonI rayon : superette.getRangeeRayons().getRayons()) {
            int nbrAjouts = 0;

            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }
            // System.out
            // .println(" chef rayon visite " + rayon.getId() + " dont stock est à " +
            // rayon.getProducts().size());
            while (chariotChefRayon.get(rayon.getId()) < Superette.MAX_SIZE_RAYON
                    && chariotChefRayon.get(rayon.getId()) > 0) {
                nbrAjouts++;
                rayon.ajouter(ProductFactory.createProduct(rayon.getId()));
                chariotChefRayon.put(rayon.getId(), chariotChefRayon.get(rayon.getId()) - 1);
            }
            System.out.println(
                    "ChefRayon---" + rayon.getId() + ": ancien  stock: " + (rayon.getProducts().size() - nbrAjouts)
                            + " nouveau stock: " + rayon.getProducts().size());

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