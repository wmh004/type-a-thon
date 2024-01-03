package AranProblem;

public class Main {
    public static void main(String[] args){
        Aran aran1 = new Aran();
        Aran aran2 = new Aran(5);

        System.out.println("PTM : " + Aran.isValid("PTM"));
        System.out.println("BPTM : " + Aran.isValid("BPTM"));
        System.out.println("BPTMBPTM : " + Aran.isValid("BPTMBPTM"));

        System.out.println(aran1);
        System.out.println(aran2);
    }
}
