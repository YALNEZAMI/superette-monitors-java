package main;

public class Caissier extends Thread {
    Superette superette;

    public Caissier(Superette superette) {
        this.superette = superette;
    }

    @Override
    public void run() {
        while (superette.getNbr_clients() < superette.getNbrClientSortie()) {
            superette.getCaisse().prendre();
        }
    }
}