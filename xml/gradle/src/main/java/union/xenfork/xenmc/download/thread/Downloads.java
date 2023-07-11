package union.xenfork.xenmc.download.thread;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Downloads {
    public ArrayList<Downloader> downloads = new ArrayList<>();
    public Downloads(File dir, String... urls) {
        for (String url : urls) {
            downloads.add(new Downloader(url, dir));
        }
        for (Downloader download : downloads) {
            download.start();
        }
    }

    public Downloads(File dir, int thread, String... urls) {
        for (String url : urls) {
            downloads.add(new Downloader(url, thread, dir));
        }
        for (Downloader download : downloads) {
            download.start();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void isDone() {
        var ref = new Object() {
            boolean b = true;
        };
        for (Downloader download : downloads) ref.b &= download.isIsDone();
        while (!ref.b);
    }
}
