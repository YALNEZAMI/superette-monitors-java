package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import interfaces.RayonI;
import main.RangeeChariots.Chariot;

public class Client extends Thread {
    private Map<String, Integer> panier;
    private String name;
    private Superette superette;
    private Chariot chariot;

    public Client(String name, Superette superette) {
        this.name = name;
        this.superette = superette;
        this.panier = new java.util.HashMap<>();
        for (String productName : Superette.availableProductsNames) {
            // nbr aleatoir entre 0 et 10
            int nbrProduits = (int) (Math.random() * Superette.MAX_PRODUCT_BY_CATEGORY_PER_CLIENT);
            panier.put(productName, nbrProduits);
        }
    }

    public Chariot getChariot() {
        return chariot;
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
        Chariot chariot = superette.getRangeeChariots().prendreChariot(this);
        this.chariot = chariot;
    }

    public void restituerChariot() {
        superette.getRangeeChariots().restituerChariot(this);
        this.chariot = null;
    }

    public void tournerRayons() {
        for (RayonI rayon : superette.getRangeeRayons().getRayons()) {
            try {
                Thread.sleep(300);
            } catch (Exception e) {
            }
            int nbrPrisRayon = 0;
            // System.out.println(this.getName() + " visite rayon " + rayon.getId()
            // + " dont stock à " + rayon.getProducts().size());
            if (panier.containsKey(rayon.getId())) {
                while (panier.get(rayon.getId()) > chariot.getProducts().get(rayon.getId())) {
                    rayon.prendre(this);
                    chariot.setProduct(rayon.getId(), chariot.getProducts().get(rayon.getId()) + 1);
                    nbrPrisRayon++;
                }
            }
            System.out.println("Client---" + this.getName() + " a pris " + nbrPrisRayon + " " + rayon.getId()
                    + " -> stock ="
                    + rayon.getProducts().size());
        }
    }

    public void entrerEnCaisse() {
        superette.getCaisse().entrerEnCaisse(this);
    }

    private int getNbrProduitDansChariot() {
        int res = 0;
        // parcours des rayons
        for (int j = 0; j < Superette.availableProductsNames.length; j++) {
            String productName = Superette.availableProductsNames[j];
            if (chariot.getProducts().containsKey(productName)) {
                res += chariot.getProducts().get(productName);
            }
        }
        return res;
    }

    public void deposer() {
        int nbProduitsDeposes = 0;
        int nbrProduitDansChariot = getNbrProduitDansChariot();
        // parcours des rayons
        for (int j = 0; j < Superette.availableProductsNames.length; j++) {
            String productName = Superette.availableProductsNames[j];
            if (chariot.getProducts().containsKey(productName)) {
                for (int i = 0; i < chariot.getProducts().get(productName); i++) {
                    nbProduitsDeposes++;
                    System.out.println("nbrproduit dans chariot " + nbrProduitDansChariot);
                    System.out.println("nbrproduit depose " + nbProduitsDeposes);
                    boolean isLastProduct = nbProduitsDeposes == nbrProduitDansChariot;
                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {
                    }
                    superette.getCaisse().deposer(this, ProductFactory.createProduct(productName), isLastProduct);
                }
                chariot.setProduct(productName, 0);
            }
        }
    }

    public void sortir() {
        superette.sortir(this);
    }

    public void sortirCaisse() {
        superette.getCaisse().sortirCaisse(this);
    }

    public void passerEnMagasin() {
        entrer();
        prendreChariot();
        tournerRayons();
        entrerEnCaisse();
        deposer();
        sortirCaisse();
        restituerChariot();
        sortir();
    }

    @Override
    public void run() {
        passerEnMagasin();
    }

}