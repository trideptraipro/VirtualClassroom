package Comm;

public class Utils {
    public static void sleep(int i){
        try {
            Thread.sleep(i);
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
}
