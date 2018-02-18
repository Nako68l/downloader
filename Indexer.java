package downloader;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Indexer {
    private final int timeInterval;
    private ScheduledExecutorService indexerExecutor;
    private HashMap<Long, String> downloadedSites;

    Indexer(int timeInterval, int threadPoolSize) {
        this.timeInterval = timeInterval;
        this.indexerExecutor = Executors.newScheduledThreadPool(threadPoolSize);
    }

    void start(HashMap<Long, String> downloadedSites) {
        this.downloadedSites = downloadedSites;
        indexerExecutor.scheduleAtFixedRate(runIndexer, 0, timeInterval, TimeUnit.SECONDS);
    }

    private Runnable runIndexer = () -> {
        if (downloadedSites.isEmpty()) {
            System.out.println("There are no downloaded sites yet...");
        } else {
            downloadedSites.forEach((id, siteText) -> {
                indexerExecutor.schedule(() -> {
                    System.out.println("Start indexing " + id + ".txt");
//                    System.out.println(siteText);
                    System.out.println("Finish indexing " + id + ".txt");
                }, 0, TimeUnit.SECONDS);
            });
        }
    };

    public void close() {
        System.out.println("Shutting down indexer...");
        indexerExecutor.shutdown();
        try {
            indexerExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
