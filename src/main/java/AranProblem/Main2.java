package AranProblem;


public class Main2 {
    public static void main(String[] args) {
        String[] testcases = new String[] {
                "PTM", "PTMMTP", "BPTMBPTM", "PT", "PTMQ", "PTMQH", "BPTMQH", "PTMQH", "BPDTMGQH",
                "PTMQH", "PTMQH", "12PTHQGM", "12PTQHMG", "12PTMGQH", "PTMQBPTMQBPTMQ", "DTBP123QHMG",
                "BTDP", "TP", "TBP", "BDT", "DTP", "ABCD", "D"
        };

        for (String input : testcases) {
            System.out.printf("%-15s : %s\n", input, Aran.isValid(input));
        }

        Aran2 a1 = new Aran2();
        Aran2 a2 = new Aran2(5);
        System.out.println(a1);
        System.out.println(a2);
    }
}