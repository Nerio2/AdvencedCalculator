public class Operations {
    public static double add(double a, double b){
        return a+b;
    }
    public static double sub(double a, double b){
        return a-b;
    }
    public static double mult(double a, double b){
        return a*b;
    }
    public static double div(double a, double b){
        if(b==0)return a;
        else return a/b;
    }
    public static double pow(double a, double b){
        return Math.pow(a,b);
    }
    public static double sqrt(double a, double b){
        if(b==0) return a;
        else return pow(a,1/b);
    }
}
