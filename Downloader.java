package downloader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class Downloader {
    private ArrayList<Weblink> resources = new ArrayList<>();
    private HashMap<Long, String> downloadedSites = new HashMap();
    private ScheduledExecutorService downloadExecutor;
    private BufferedReader fileReader;
    private File sourceFile;
    private int timeInterval;

    public Downloader(File sourceFile, int timeInterval, int threadPoolSize) throws IOException {
        new File("src/downloader/downloads").mkdir();
        this.sourceFile = sourceFile;
        this.timeInterval = timeInterval;
        this.downloadExecutor = Executors.newScheduledThreadPool(threadPoolSize);
        this.fileReader = new BufferedReader(new FileReader(sourceFile));
    }

    private void start() throws IOException {
        String line;
        while ((line = fileReader.readLine()) != null) {
            resources.add(parseWeblink(line));
        }
        run();
    }

    void start(Indexer indexer) throws IOException {
        start();
        indexer.start(downloadedSites);
    }

    private void run() {
        resources.stream().filter(this::checkLinkSourceType).forEach(weblink -> {
            Runnable saveSite = () -> {
                File file = new File("src/downloader/downloads/" + weblink.getId() + ".txt");
                URL url = weblink.getUrl();
                StringBuilder siteText = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(weblink.getConnection().getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
                    System.out.println("Start to download " + url);
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        siteText.append(inputLine + "\n");
                    }
                    writer.write(siteText.toString());
                    System.out.println(url + " saved to " + file.getName());
                    weblink.setStatus(Status.SUCCESS);
                } catch (IOException e) {
                    weblink.setStatus(Status.FAILED);
                    e.printStackTrace();
                }
                downloadedSites.put(weblink.getId(), siteText.toString());
            };
            downloadExecutor.scheduleAtFixedRate(saveSite, 0, timeInterval, TimeUnit.SECONDS);
        });
        downloadExecutor.scheduleAtFixedRate(() -> saveResources(resources), 0, timeInterval, TimeUnit.SECONDS);
    }

    private boolean checkLinkSourceType(Weblink weblink) {
        try {
            if (!Objects.equals(weblink.getConnection().getContentType().split(";")[0], "text/html")) {
                System.out.println(weblink.getUrl() + " not eligible content type");
                weblink.setStatus(Status.NOT_ELIGIBLE);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No connection");
            weblink.setStatus(Status.FAILED);
            return false;
        }
        return true;
    }

    private Weblink parseWeblink(String line) throws MalformedURLException {
        String[] fields = line.split("( *)(\\|)( *)");
        long id = Long.parseLong(fields[0]);
        String title = fields[1];
        List<String> tags = Arrays.asList(fields[2].substring(1, fields[2].length() - 1).split(","));
        URL url = new URL(fields[3]);
        Status status = Status.valueOf(fields[4]);
        return new Weblink(id, title, tags, url, status);
    }

    private synchronized void saveResources(ArrayList<Weblink> resources) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(sourceFile))) {
            System.out.println("Saving results...");
            resources.forEach(weblink -> {
                try {
                    fileWriter.write(weblink.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Result are saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        System.out.println("Shutting down downloader...");
        downloadExecutor.shutdown();
        fileReader.close();
        try {
            downloadExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        saveResources(resources);
    }
}
class b{
    final int b = 0;
}
class a extends b implements Serializable{
    static int a;
}