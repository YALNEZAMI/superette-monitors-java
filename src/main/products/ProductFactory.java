package main.products;

public class ProductFactory {
    public static Product createProduct(String name) {
        switch (name) {
            case "beurre":
                return new Beurre();

            case "lait":
                return new Lait();

            case "sucre":
                return new Sucre();

            case "farine":
                return new Farine();

            default:
                System.out.println("error: ProductFactory-createProduct nom produit non trouvé");
                return null;
        }
    }
}
