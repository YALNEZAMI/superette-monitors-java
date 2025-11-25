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
    }

    public void tournerRayons() {
        for (RayonI rayon : superette.getRangeeRayons().getRayons()) {
            while (chariotChefRayon.get(rayon.getId()) < rayon.getMax_size()
                    && chariotChefRayon.get(rayon.getId()) > 0) {
                rayon.ajouter(ProductFactory.createProduct(rayon.getId()));
                chariotChefRayon.put(rayon.getId(), chariotChefRayon.get(rayon.getId()) - 1);
            }
        }
    }

    public void travailler() {
        faireLePlein();
        tournerRayons();
    }

    @Override
    public void run() {
        while (superette.getNbr_clients() < superette.getNbrClientSortie()) {
            travailler();
        }
    }
}