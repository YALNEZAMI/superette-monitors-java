package main.mp;

import main.Afficheur;
import main.threads.Client;

public class Superette {
    /* à synchroniser avec le switch de ProductFactory */
    public static String[] availableProductsNames = { "beurre", "farine", "sucre", "lait" };
    /* Nombre de chariots initial */
    public static int NBR_CHARIOTS = 3;
    /**
     * le maximum qu'un client peut convoiter d'une catégorie donné
     * exemple autorisé avec 5: 5 farine, 0 sucre, 3 beurre, 1 lait
     * exemple interdit avec 5: 0 farine, 2 sucre, 8 beurre, 6 lait
     */
    public static int MAX_PRODUCT_BY_CATEGORY_PER_CLIENT = 5;
    /**
     * le nombre de clients dans le supermarché (fourni dans les specs du rapport)
     */
    public final static int NBR_INIT_CLIENTS = 5;
    /* Le nombre maximum qu'un rayon peut contenir de produits */
    public static int MAX_SIZE_RAYON = 10;
    /** La taille du tapis de la caisse */
    public static int SIZE_CAISSE_TAPIS = 5;
    /** Capacité du chef de rayon par catégorie de produit (fourni dans le sujet */
    public final static int MAX_CHEF_RAYON_CAPACITY = 5;
    // les rayons sont remplis à l'initialisation
    public static boolean ARE_RAYONS_PLEIN_PAR_DEFAUT = true;
    // les temps d'attente en ms (fourni dans le sujet)
    public final static int TEMPS_CHEF_RAYON_FAIRE_PLEIN = 500;
    public final static int TEMPS_CHEF_RAYON_ENTRE_RAYONS = 200;
    public final static int TEMPS_CLIENT_ENTRE_RAYONS = 300;
    public final static int TEMPS_CLIENT_DEPOSE_PRODUIT_SUR_TAPIS = 20;

    private RangeeChariots rangeeChariots;
    private RangeeRayons rangeeRayons;
    private Caisse caisse;
    private int nbrClientSortie = 0;

    public Superette() {
        rangeeChariots = new RangeeChariots();
        rangeeRayons = new RangeeRayons();
        caisse = new Caisse();
    }

    public int getNbrClientSortie() {
        return nbrClientSortie;
    }

    public RangeeChariots getRangeeChariots() {
        return rangeeChariots;
    }

    public RangeeRayons getRangeeRayons() {
        return rangeeRayons;
    }

    public Caisse getCaisse() {
        return caisse;
    }

    /**
     * 
     * @param client le client qui entre au supermarché
     */
    public void entrer(Client client) {
        System.out.println(Afficheur.colorer("Superette",
                "Client---" + client.getName() + " est entré au supermarché et veut acheter "
                        + client.getListCourse().values().stream().mapToInt(Integer::intValue).sum() + " produits"));
    }

    /**
     * 
     * @param client le client qui sort
     * @ensure les clients sorte un par un pour pouvoir bien les compter
     * @ensure notifier le caissier au dernier client sortant
     */
    synchronized public void sortir(Client client) {
        nbrClientSortie++;
        if (client.getNbrProduitDansListCourse() > 0) {
            System.out
                    .println(Afficheur.colorer("Superette",
                            "Client---" + client.getName() + " est sorti du supermarché."));

        } else {
            System.out
                    .println(Afficheur.colorer("Superette", "Client---" + client.getName()
                            + " est sorti du supermarché car il n'avait rien à acheter."));

        }
        // notifier le caissier que le travail est fini
        if (nbrClientSortie == Superette.NBR_INIT_CLIENTS) {
            caisse.reveillerCaissierPourRentrer();
        }
    }

    @Override
    public String toString() {
        String s = "";
        s += Afficheur.colorer("Chariot", "Etat du stock de chariot: " + rangeeChariots.toString() + " \n");
        s += Afficheur.colorer("Rayon",
                "Etat du stock de praduits dans les rayons: \n" + rangeeRayons.toString() + "\n");
        s += Afficheur.colorer("Caisse", "Etat de la caisse: \n" + caisse.toString() + "\n");
        return s;
    }
}
