package main;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import interfaces.RayonI;

public class Client extends Thread {
    private Map<String, Integer> panier;
    private String name;
    private Superette superette;
    private List<Product> products;

    public Client(String name, Superette superette) {
        this.name = name;
        this.superette = superette;
        this.panier = new java.util.HashMap<>();
        this.products = new ArrayList<>();
        for (String productName : Superette.availableProductsNames) {
            // nbr aleatoir entre 0 et 10
            int nbrProduits = (int) (Math.random() * 10);
            panier.put(productName, nbrProduits);
        }
    }

    public Map<String, Integer> getPanier() {
        return panier;
    }

    public String getClientName() {
        return name;
    }

    public void entrer() {
        superette.entrer(this);
    }

    public void prendreChariot() {
        superette.getRangeeChariots().prendreChariot();
    }

    public void tournerRayons() {
        for (RayonI rayon : superette.getRangeeRayons().getRayons()) {
            if (this.getPanier().containsKey(rayon.getId())) {
                while (this.getPanier().get(rayon.getId()) > products.size()) {
                    rayon.prendre();
                    products.add(ProductFactory.createProduct(rayon.getId()));

                }
            }
        }
    }

    public void entrerEnCaisse() {
        superette.getCaisse().entrerEnCaisse(this);
    }

    public void sortir() {
        superette.sortir(this);
    }

    public void passerEnMagasin() {
        entrer();
        prendreChariot();
        tournerRayons();
        entrerEnCaisse();
        sortir();
    }

    @Override
    public void run() {
        passerEnMagasin();
    }

}