package main.threads;

import java.util.Map;
import main.mp.Superette;
import main.mp.Chariot;
import main.mp.Rayon;
import main.products.ProductFactory;

public class Client extends Thread {
    private Map<String, Integer> listCourse;// list de course theorique
    private Superette superette;//
    private Chariot chariot;// chariot reel à remplir

    public Client(Superette superette) {
        this.superette = superette;
        this.listCourse = new java.util.HashMap<>();
        for (String productName : Superette.availableProductsNames) {
            // nbr aleatoir entre 0 et 10
            int nbrProduits = (int) (Math.random() * Superette.MAX_PRODUCT_BY_CATEGORY_PER_CLIENT);
            listCourse.put(productName, nbrProduits);
        }
    }

    public Chariot getChariot() {
        return chariot;
    }

    public Map<String, Integer> getListCourse() {
        return listCourse;
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
        for (Rayon rayon : superette.getRangeeRayons().getRayons()) {
            try {
                Thread.sleep(Superette.TEMPS_CLIENT_ENTRE_RAYONS);
            } catch (Exception e) {
            }
            if (listCourse.containsKey(rayon.getId())) {
                while (listCourse.get(rayon.getId()) > chariot.getProducts().get(rayon.getId())) {
                    rayon.prendre(this);
                    chariot.incremente(rayon.getId());
                }
            }
            System.out.println("Client---" + this.getName() + " a pris " + chariot.getProducts().get(rayon.getId())
                    + " " + rayon.getId()
                    + " -> stock = "
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
                    boolean isLastProduct = nbProduitsDeposes == nbrProduitDansChariot;
                    try {
                        Thread.sleep(Superette.TEMPS_CLIENT_DEPOSE_PRODUIT_SUR_TAPIS);
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

    @Override
    public String toString() {
        return "Client [name=" + getName() + ", listCourse=" + listCourse + "]";
    }

}