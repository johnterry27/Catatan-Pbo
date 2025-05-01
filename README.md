# Catatan-Pbo

## Benner.java
```java
import java.awt.*;
import javax.swing.*;

public class Banner extends JPanel implements Runnable {
    private String baseText = "Your name here";
    private String scrollingText = "";
    private Thread t;
    private boolean stopFlag;

    public Banner() {
        setBackground(Color.CYAN);
        setForeground(Color.RED);
        t = new Thread(this);
        t.start();
    }

    public synchronized void setText(String text) {
        this.baseText = text;
        rebuildScrollingText();
    }

    private void rebuildScrollingText() {
       
        StringBuilder sb = new StringBuilder();
        int panelWidth = getWidth() > 0 ? getWidth() : 400; 
        int repeat = panelWidth / (baseText.length() * 10); 
        for (int i = 0; i < repeat + 10; i++) {
            sb.append(baseText).append(" ");
        }
        scrollingText = sb.toString();
    }

    public void run() {
        rebuildScrollingText();
        while (!stopFlag) {
            repaint();
            synchronized (this) {
                char ch = scrollingText.charAt(0);
                scrollingText = scrollingText.substring(1) + ch;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(scrollingText, 10, 30);
    }

    public void stop() {
        stopFlag = true;
    }
}

```

## BannerServer.java
```java
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import javax.swing.*;

public class BannerServer {
    public static void main(String[] args) throws IOException {
        Banner banner = new Banner();

        JFrame frame = new JFrame("Banner Animation");
        frame.add(banner);
        frame.setSize(10000000, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

      
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/setname", new NameHandler(banner));
        server.setExecutor(null); 
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class NameHandler implements HttpHandler {
        private final Banner banner;

        public NameHandler(Banner banner) {
            this.banner = banner;
        }

        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                banner.setText(body);  

                String response = "Name updated to: " + body;
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); 
            }
        }
    }
}


```

