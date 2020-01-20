import java.util.concurrent.ForkJoinPool;

public class LinkCrawler
{
    public static void main(String[] args) {
        CustomRecursiveAction customRecursiveAction = new CustomRecursiveAction(12);
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        //new ForkJoinPool().invoke(customRecursiveAction);
        commonPool.invoke(customRecursiveAction);
    }
}
