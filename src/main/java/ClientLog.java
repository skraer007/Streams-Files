import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    List<Integer> productNum = new ArrayList<>();
    List<Integer> amount = new ArrayList<>();

    public void log(int productNum, int amount) {
        this.productNum.add(productNum);
        this.amount.add(amount);
    }

    public void exportAsCSV(File txtFile) throws IOException {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(txtFile))) {
            csvWriter.writeNext(new String[]{"productNum", "amount"});
            for (int i = 0; i < productNum.size(); i++) {
                csvWriter.writeNext(new String[]{productNum.get(i).toString(), amount.get(i).toString()});
            }
        }
    }
}