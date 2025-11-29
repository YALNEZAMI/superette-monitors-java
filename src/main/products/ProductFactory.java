package main.products;

public class ProductFactory {
    /**
     * 
     * @param name doit être un nom de produit valide (voir
     *             Superette.availableProductsNames)
     * @return une instance de la classe correspondante à name
     */
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
                throw new IllegalArgumentException("error: ProductFactory-createProduct nom produit non trouvé");
        }
    }
}
