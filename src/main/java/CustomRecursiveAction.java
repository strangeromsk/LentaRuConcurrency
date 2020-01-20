import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

public class CustomRecursiveAction extends RecursiveAction
{

    Set<String> urls = ConcurrentHashMap.newKeySet();
    String html = "https://lenta.ru";

    private long workLoad = 0;

    public CustomRecursiveAction(long workLoad) {
        this.workLoad = workLoad;
    }

    @Override
    protected void compute() {
        if(this.workLoad > 4) {
            System.out.println("Splitting workLoad : " + this.workLoad);
            getLinks(html, urls);
            List<CustomRecursiveAction> subtasks =
                    new ArrayList<>();

            subtasks.addAll(createSubtasks());

            for(RecursiveAction subtask : subtasks){
                subtask.fork();
            }

        } else {
            System.out.println("Doing workLoad myself: " + this.workLoad);
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

    static void getLinks(String url, Set<String> urls) {
        if (urls.contains(url)) {
            return;
        }
        if (url.contains("https://www.lenta.ru") || url.contains("https://lenta.ru")) {
            urls.add(url);

            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a");
                //System.out.println(url);
                for (Element element : elements) {
                    System.out.println(element.absUrl("href"));
                    getLinks(element.absUrl("href"), urls);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
