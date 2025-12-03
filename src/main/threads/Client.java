package main.threads;

import java.util.Map;
import main.mp.Superette;
import main.Afficheur;
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

    // entrer au magasin
    public void entrer() {
        superette.entrer(this);
    }

    // prendre un chariot
    public void prendreChariot() {
        Chariot chariot = superette.getRangeeChariots().prendreChariot(this);
        this.chariot = chariot;
    }

    // restituer le chariot
    public void restituerChariot() {
        superette.getRangeeChariots().restituerChariot(this);
        this.chariot = null;
    }

    /**
     * @ensure faire la tournée des rayons
     * @ensure prendre les produits un par un
     * @ensure attendre dans chaque rayon jusqu'à remplire les besoin
     */
    public void tournerRayons() {
        for (Rayon rayon : superette.getRangeeRayons().getRayons()) {
            try {
                Thread.sleep(Superette.TEMPS_CLIENT_ENTRE_RAYONS);
            } catch (Exception e) {
            }
            if (listCourse.containsKey(rayon.getId()) && listCourse.get(rayon.getId()) != 0) {
                while (listCourse.get(rayon.getId()) > chariot.getProducts().get(rayon.getId())) {
                    rayon.prendre(this);
                    chariot.incremente(rayon.getId());
                }

            }
        }
    }

    // entrer en caisse
    public void entrerEnCaisse() {
        superette.getCaisse().entrerEnCaisse(this);
    }

    /**
     * 
     * @return le nombre total d'items dans la chariot
     */
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

    /**
     * 
     * @return le nombre total d'items dans la liste de course
     */
    public int getNbrProduitDansListCourse() {
        int res = 0;
        // parcours des rayons
        for (int j = 0; j < Superette.availableProductsNames.length; j++) {
            String productName = Superette.availableProductsNames[j];
            if (listCourse.containsKey(productName)) {
                res += listCourse.get(productName);
            }
        }
        return res;
    }

    /**
     * @ensure déposer tous les produit un par un
     * @ensure à chaque categorie totalement déposé, remettre à 0
     */
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

    // sortir du magasin
    public void sortir() {
        superette.sortir(this);
    }

    // sortir de la caisse
    public void sortirCaisse() {
        superette.getCaisse().sortirCaisse(this);
    }

    // passer en magasin selon les étapes précisées
    public void passerEnMagasin() {
        entrer();
        if (getNbrProduitDansListCourse() > 0) {
            prendreChariot();
            tournerRayons();
            entrerEnCaisse();
            deposer();
            sortirCaisse();
            restituerChariot();
        }
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