package downloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Weblink {
    private long id;
    private String title;
    private List<String> tags;
    private URL url;
    private HttpURLConnection connection;
    private Status status;

    public Weblink(long id, String title, List<String> tags, URL url, Status status) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.url = url;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Weblink setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Weblink setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public Weblink setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public URL getUrl() {
        return url;
    }

    public Weblink setUrl(URL url) {
        this.url = url;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Weblink setStatus(Status status) {
        this.status = status;
        return this;
    }

    public URLConnection getConnection() throws IOException {
        if (connection != null){
            return connection;
        }
        return url.openConnection();
    }

    @Override
    public String toString() {
        return id + " | " + title + " | " + tags + " | " + url + " | " + status + "\n";
    }
}
