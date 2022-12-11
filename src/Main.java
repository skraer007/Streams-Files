import java.io.*;
import java.util.Scanner;

class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File saveBasket = new File("basket.bin");
        Scanner scanner = new Scanner(System.in);
        String[] products = {"Молоко", "Хлеб", "Гречневая крупа", "Хурма"};
        int[] prices = {50, 14, 80, 50};
        Basket basket;
        if (saveBasket.exists()) {
            basket = Basket.loadFromBinFile(saveBasket);
        } else basket = new Basket(prices, products);
        basket.printProducts();
        while (true) {
            System.out.println("Выберите товар и количество или введите `end`: ");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                break;
            }
            String[] subInput = input.split(" ");
            int firstPart = Integer.parseInt(subInput[0]);
            int secondPart = Integer.parseInt(subInput[1]);
            if (firstPart <= products.length) {
                basket.addToCart(firstPart - 1, secondPart);
            } else {
                System.out.println("Нет такого товара");
            }
        }
        basket.saveBin(saveBasket);
        basket.printCart();
    }
}