package org.example;


import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class ConcurrentTest {
    public static void main(String[] args) throws InterruptedException {
        int numThreads = 20;
        int requestsPerUser = 5;
        ExecutorService testExecutor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());

        String[] urls = {
            "https://example.com",
            "https://www.wikipedia.org",
            "https://www.google.com",
            "https://www.whatsapp.com",
            "https://avatars.githubusercontent.com/u/143046270?v=4&size=64",
            "https://i.blogs.es/010020/chatgpt/840_560.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMSRjqyBIJcrrb_fi0n-DLNPqdmAyapYWLlw&s"
        };
        Random random = new Random();

        for (int i = 0; i < numThreads; i++) {
            final int userNum = i;
            testExecutor.execute(() -> {
                String userId = "user" + userNum;
                String lastSetCookie = null;
                try {
                    for (int j = 0; j < requestsPerUser; j++) {
                        long start = System.nanoTime();
                        Socket socket = new Socket("localhost", 8080);
                        OutputStream out = socket.getOutputStream();
                        String url = urls[random.nextInt(urls.length)];
                        String cookieHeader = (lastSetCookie != null) ? lastSetCookie : CookieManager.buildSetCookie("userId", userId).split(";", 2)[0];
                        String request = "POST / HTTP/1.1\r\n" +
                                "Host: localhost\r\n" +
                                "Content-Type: text/plain\r\n" +
                                "Cookie: " + cookieHeader + "\r\n" +
                                "Content-Length: " + url.length() + "\r\n\r\n" +
                                url;

                        out.write(request.getBytes());
                        out.flush();

                        InputStream in = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        boolean gotSetCookie = false;
                        while ((line = reader.readLine()) != null && !line.isEmpty()) {
                            if (line.toLowerCase().startsWith("set-cookie:")) {
                                String setCookieValue = line.substring("set-cookie:".length()).trim().split(";", 2)[0];
                                lastSetCookie = setCookieValue;
                                gotSetCookie = true;
                            }
                        }
                        socket.close();
                        long end = System.nanoTime();
                        responseTimes.add((end - start) / 1_000_000); // ms
                        if (gotSetCookie) {
                            successCount.incrementAndGet();
                        } else {
                            failCount.incrementAndGet();
                        }
                    }
                } catch (IOException e) {
                    failCount.incrementAndGet();
                    System.err.println("Usuario " + userNum + " error: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        testExecutor.shutdown();


        System.out.println("Solicitudes exitosas: " + successCount.get());
        System.out.println("Solicitudes fallidas: " + failCount.get());
        if (!responseTimes.isEmpty()) {
            long min = Collections.min(responseTimes);
            long max = Collections.max(responseTimes);
            double avg = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
            System.out.println("Tiempo de respuesta (ms): min=" + min + ", max=" + max + ", avg=" + avg);
        }
        System.out.println("Todas las solicitudes completadas");
    }
}