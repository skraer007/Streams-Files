import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Basket implements Serializable {
    private String[] products;
    private int[] prices;
    private int[] productsCount;

    public Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        this.productsCount = new int[products.length];
    }

    public void addToCart(int productNum, int amount) {
        productsCount[productNum] = amount;
    }

    public void printCart() {
        System.out.println("Ваша корзина:");
        int sum = 0;
        for (int i = 0; i < products.length; i++) {
            if (productsCount[i] != 0) {
                sum += productsCount[i] * prices[i];
                System.out.println((i + 1) + ". " + products[i] + " " + productsCount[i] + " шт " + prices[i] + " руб/шт "
                        + prices[i] * productsCount[i] + " руб в сумме");
            }
        }
        System.out.println("Итого " + sum + " руб");
    }

    public void printProducts() {
        System.out.println("Список доступных продуктов:");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
        }
    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter saveTxt = new PrintWriter(textFile)) {
            for (String product : products) {
                saveTxt.print(product);
            }
            saveTxt.println();
            for (int price : prices) {
                saveTxt.print(price + " ");
            }
            saveTxt.println();
            for (int productCount : productsCount) {
                saveTxt.print(productCount + " ");
            }
        }
    }

    public void setProductsCount(int[] productsCount) {
        this.productsCount = productsCount;
    }

    public String[] getProducts() {
        return products;
    }

    public int[] getPrices() {
        return prices;
    }

    static Basket loadFromTxtFile(File textFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String info;
            ArrayList<String> list = new ArrayList<>();
            while ((info = reader.readLine()) != null) {
                if (!info.isEmpty()) {
                    list.add(info);
                }
            }
            String[] infoFromFile = list.toArray(new String[0]);

            String[] products = infoFromFile[0].split("(?=\\p{Lu})");
            int[] prices = Arrays.stream(infoFromFile[1].split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] productsCount = Arrays.stream(infoFromFile[2].split(" ")).mapToInt(Integer::parseInt).toArray();
            Basket basket = new Basket(prices, products);
            basket.setProductsCount(productsCount);
            return basket;
        }
    }

    public void saveJSON(File textFile) throws IOException {
        try (PrintWriter saveFile = new PrintWriter(textFile)) {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            saveFile.println(json);
        }
    }

    public static Basket loadFromJSONFile(File textFile) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile))) {
            String gsonText = bufferedReader.readLine();
            Gson gson = new Gson();
            return gson.fromJson(gsonText, Basket.class);
        }
    }
}