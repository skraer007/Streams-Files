import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        ClientLog clientLog = new ClientLog();
        File saveLog = new File("log.csv");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse("shop.xml");
        XPath xPath = XPathFactory.newInstance().newXPath();
        boolean enabled = Boolean.parseBoolean(xPath.compile("/config/load/enabled").evaluate(document));
        String loadFileName = xPath.compile("/config/load/fileName").evaluate(document);
        String loadFormat = xPath.compile("/config/load/format").evaluate(document);

        Scanner scanner = new Scanner(System.in);
        String[] products = {"Молоко", "Хлеб", "Гречневая крупа", "Хурма"};
        int[] prices = {50, 14, 80, 50};
        Basket basket = null;
        if (enabled) {
            switch (loadFormat) {
                case "json":
                    basket = Basket.loadFromJSONFile(new File(loadFileName));
                    break;
                case "txt":
                    basket = Basket.loadFromTxtFile(new File(loadFileName));
                    break;
            }
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
            clientLog.log(firstPart, secondPart);
            if (firstPart <= products.length) {
                basket.addToCart(firstPart - 1, secondPart);
            } else {
                System.out.println("Нет такого товара");
            }
        }

        enabled = Boolean.parseBoolean(xPath.compile("/config/save/enabled").evaluate(document));
        loadFileName = xPath.compile("/config/save/fileName").evaluate(document);
        loadFormat = xPath.compile("/config/save/format").evaluate(document);
        if (enabled) {
            switch (loadFormat) {
                case "json":
                    basket.saveJSON(new File(loadFileName));
                    break;
                case "txt":
                    basket.saveTxt(new File(loadFileName));
                    break;
            }
        }

        enabled = Boolean.parseBoolean(xPath.compile("/config/log/enabled").evaluate(document));
        loadFileName = xPath.compile("/config/log/fileName").evaluate(document);
        if (enabled) {
            clientLog.exportAsCSV(new File(loadFileName));
        }

        basket.printCart();
    }
}