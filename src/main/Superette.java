package main;

public class Superette {
    public static String[] availableProductsNames = { "beurre", "farine", "sucre", "lait" };// à synchroniser avec les
                                                                                            // sous type de produit et
                                                                                            // ProductFactory
    public static int NBR_CHARIOTS = 5;
    public static int MAX_PRODUCT_BY_CATEGORY_PER_CLIENT = 5;
    public static int NBR_INIT_CLIENTS = 2;
    public static int MAX_SIZE_RAYON = 6;
    public static int SIZE_CAISSE_TAPIS = 5;

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
        // System.out.println("nbr clients sortie" + nbrClientSortie);
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

    public void entrer(Client client) {
        System.out.println(client.getName() + " est entré au supermarché");
    }

    public void sortir(Client client) {
        nbrClientSortie++;
        System.out.println(client.getName() + " est sorti du supermarché");
    }

    @Override
    public String toString() {
        String s = "Superette: \n";
        s += "Rangee chariots: \n";
        rangeeChariots.toString();
        s += "Rangee rayons: \n";
        rangeeRayons.toString();
        s += "Caisse: \n";
        s += caisse.toString() + "\n";
        return s;
    }
}
