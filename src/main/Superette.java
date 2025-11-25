package main;

public class Superette {
    public static String[] availableProductsNames = { "beurre", "farine", "sucre", "lait" };
    public static int NBR_CHARIOTS = 20;
    private int nbr_clients = 20;

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

    public int getNbr_clients() {
        return nbr_clients;
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
