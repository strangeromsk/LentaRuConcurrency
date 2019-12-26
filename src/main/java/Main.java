import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main
{
    public static void main(String[] args) throws IOException {
//        List<String> list = new ArrayList<>();
        Set<String> urls = new HashSet<>();
        String html = "https://www.lenta.ru";
        getLinks(html, urls);
//        Document doc = Jsoup.connect(html).get();
//        Elements links = doc.select("a");
//
//        for(int i = 0; i < 100000; i++){
//            String node = links.get(i).absUrl("href");
//            list.add(node);
//            System.out.println(list.get(i));
//        }
        //list.forEach(System.out::println);
        //getLinksFromSite(5, urls);

    }


    static void getLinks(String url, Set<String> urls) {

        if (urls.contains(url)) {
            return;
        }
        if(url.contains("https://www.lenta.ru")){
            urls.add(url);

            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a");
                for(Element element : elements){
                    System.out.println(element.absUrl("href"));
                    getLinks(element.absUrl("href"), urls);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static Set<String> getLinksFromSite(int Level, Set<String> Links) {
        if (Level < 5) {
            Set<String> locallinks =  new HashSet<>();
            for (String link : Links) {
                Set<String> newLinks = new HashSet<>();
                locallinks.addAll(getLinksFromSite(Level+1, newLinks));
            }
            return locallinks;
        } else {
            return Links;
        }

    }
}
