import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

public class CustomRecursiveAction extends RecursiveAction
{

    private static Set<String> uniqueURL = Collections.synchronizedSet(new HashSet<>());;
    private static String mySite;
    private static final Random random = new Random();
    private static BufferedWriter bufferedWriter;
    private static String tabulation = "\t";
    private long workLoad = 0;

    public CustomRecursiveAction(long workLoad) {
        this.workLoad = workLoad;
    }

    @Override
    protected void compute() {
        List<CustomRecursiveAction> subtasks =
                new ArrayList<>(createSubtasks());

        for(RecursiveAction subtask : subtasks){
            subtask.fork();
        }
        mySite = "https://lenta.ru";
        getLinks(mySite);

    }

    private static void getLinks(String url){
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            Elements links = doc.select("a");

            if (links.isEmpty()) {
                return;
            }

            links.stream().map((link) -> link.attr("abs:href")).forEachOrdered((thisUrl) -> {
                boolean add = uniqueURL.add(thisUrl);
                if (add && thisUrl.contains(mySite)) {
                    try {
                        Thread.sleep(random.nextInt(300));
                        long count = thisUrl.chars().filter(ch->ch == '/').count();
                        if (count <= 3){
                            getWriter().write("\n" + thisUrl);
                        }
                        else {
                            getWriter().write("\n"+ tabulation.repeat((int) count) + thisUrl);
                        }
                        getWriter().flush();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(thisUrl);
                    getLinks(thisUrl);
                }
            });
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
    }

    private List<CustomRecursiveAction> createSubtasks() {
        List<CustomRecursiveAction> subtasks = new ArrayList<>();

        CustomRecursiveAction subtask1 = new CustomRecursiveAction(this.workLoad / 2);
        CustomRecursiveAction subtask2 = new CustomRecursiveAction(this.workLoad / 2);

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;
    }

    private static synchronized BufferedWriter getWriter()
    {
        try{
            if(bufferedWriter == null )
            {
                bufferedWriter =  new BufferedWriter(new FileWriter("src/main/resources/links.txt", true));
            }
            return bufferedWriter;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
