package main.mp;

public class RangeeRayons {
    Rayon[] rayons = new Rayon[Superette.availableProductsNames.length];

    public RangeeRayons() {
        for (int i = 0; i < rayons.length; i++) {
            rayons[i] = new Rayon(Superette.availableProductsNames[i]);
        }
    }

    public Rayon[] getRayons() {
        return rayons;
    }

    @Override
    public String toString() {
        String s = "";
        for (Rayon rayon : rayons) {
            s += rayon.getId() + " : " + rayon.getProducts().size() + " items.\n";
        }
        return s;
    }

}
