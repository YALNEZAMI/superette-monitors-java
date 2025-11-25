package main;

public class Caissier extends Thread {
    Superette superette;

    public Caissier(Superette superette) {
        this.superette = superette;
    }

    @Override
    public void run() {
        while (Superette.NBR_INIT_CLIENTS > superette.getNbrClientSortie()) {
            superette.getCaisse().prendre(this);
        }
        System.out.println("caissier a fini son travail");
    }
}