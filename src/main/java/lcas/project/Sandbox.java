package lcas.project;
class Sandbox {
    public static void main(String[] args){
        println(Review.sentimentVal("Kevin"));
        println(Review.sentimentVal("pleasant"));
        println(Review.sentimentVal("good"));
        println(Review.sentimentVal("greatness"));
        println(Review.sentimentVal("Matthew"));
        println(Review.sentimentVal("horrible"));
        println(Review.sentimentVal("Emma"));
    }



    static void println(String text){
        System.out.println(text);
    }
    static void println(double text){
        System.out.println(text);
    }
}