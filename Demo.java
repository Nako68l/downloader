package downloader;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Demo {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter time interval in seconds, to re-download pages.. ");
        int downloaderInterval = 10;
        System.out.println("Enter downloader executor thread pull size..");
        int downloaderThreadPullSize = 4;
        System.out.println("Enter time interval in seconds, to re-index pages.. ");
        int indexerInterval = 10;
        System.out.println("Enter indexer executor thread pull size..");
        int indexerThreadPullSize = 4;
        System.out.println("Enter 0 to end");
        try {
            Downloader downloader = new Downloader(new File("src/downloader/demo.csv"), downloaderInterval, downloaderThreadPullSize);
            Indexer indexer = new Indexer(indexerInterval, indexerThreadPullSize);
            downloader.start(indexer);
            if (exitInput()){
                downloader.close();
                indexer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean exitInput() {
        int input = scanner.nextInt();
        if (input != 0) {
            System.out.println("no such option");
            exitInput();
        }
        return true;
    }
}