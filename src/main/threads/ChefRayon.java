package main.threads;

import main.mp.Superette;
import main.Afficheur;
import main.mp.Chariot;
import main.mp.Rayon;
import main.products.ProductFactory;

public class ChefRayon extends Thread {
    Superette superette;
    // chariot du chef de rayon initialsé vide
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
        System.out.println(Afficheur.colorer("ChefRayon", "Chef rayon a fais le plein ."));
    }

    /**
     * @ensure les rayons sont remplis autant que possible
     * 
     */
    public void tournerRayons() {
        for (Rayon rayon : superette.getRangeeRayons().getRayons()) {
            int ancienStock = rayon.getProducts().size();
            int nbrAjouts = 0;

            try {
                Thread.sleep(Superette.TEMPS_CHEF_RAYON_ENTRE_RAYONS);
            } catch (Exception e) {
            }
            while (chariotChefRayon.getProducts().get(rayon.getId()) > 0) {
                nbrAjouts++;
                rayon.ajouter(ProductFactory.createProduct(rayon.getId()));
                chariotChefRayon.decremente(rayon.getId());
            }
            if (nbrAjouts > 0) {
                System.out.println(Afficheur.colorer("ChefRayon",
                        "ChefRayon---" + rayon.getId() + ": ancien stock: " + (ancienStock)
                                + " nouveau stock: " + rayon.getProducts().size() + " (" + nbrAjouts + " ajouté)"));

            }
        }
    }

    // faire le plein et tourner entre les rayons
    public void travailler() {
        faireLePlein();
        tournerRayons();
    }

    @Override
    public void run() {
        while (Superette.NBR_INIT_CLIENTS > superette.getNbrClientSortie()) {
            travailler();
        }
        System.out.println(Afficheur.colorer("ChefRayon", "Chef rayon a fini son travail"));
    }
}