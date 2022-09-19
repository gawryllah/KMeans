import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    int k;

    public static void main(String[] args) {

        List<List<Double>> wszystkieWektory;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj sciezke pliku");

        String path = scanner.next();

        System.out.println("Podaj ilosc klastrow");

        int k = scanner.nextInt();
        System.out.println();

        String line;

        List<List<Double>> listaListaW = new ArrayList<>();

        List<Double> wektory;

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            while ((line = br.readLine()) != null) {

                wektory = new ArrayList<>();

                String[] split = line.split(",");

                for (String s : split) wektory.add(Double.parseDouble(s));

                listaListaW.add(wektory);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        wszystkieWektory = listaListaW;

        int listaSize = wszystkieWektory.get(1).size();

        Map<Integer, List<Double>> centroidy = nowyCentroid(wszystkieWektory, k);

        Map<List<Double>, Integer> klastry = kSrednie(wszystkieWektory, centroidy, k);


        double odl = 0;
        double poprzOdl = 0;

        List<Double> doubleList;


        while (true) {

            for (int j = 0; j < k; j++) {

                List<List<Double>> list = new ArrayList<>();

                for (List<Double> key : klastry.keySet()) {

                    if (klastry.get(key) == j) {
                        list.add(key);
                    }
                }

                doubleList = liczCentroid(list, listaSize);

                centroidy.put(j, doubleList);
            }

            for (int klaster = 0; klaster < k; klaster++) {

                double sumKwOdl = 0;

                for (List<Double> key : klastry.keySet()) {

                    if (klastry.get(key) == klaster) {

                        sumKwOdl += Math.pow((odlEuklesedesa(key, centroidy.get(klaster))), 2);
                    }

                }

                odl += sumKwOdl;

                System.out.println();
                System.out.println("Suma kwadratow odleglosci (E), dla  " + klaster + " = " + odl);
                System.out.println();
            }

            if (poprzOdl - odl == 0) {
                break;
            }

            poprzOdl = odl;


            odl = 0;

            klastry.clear();

            centroidy = nowyCentroid(wszystkieWektory, k);

            klastry = kSrednie(wszystkieWektory, centroidy, k);
        }

        int j = 0;
        int lasti = 0;
        System.out.println("=============== RESULT ============");
        System.out.println();
        for (List<Double> key : klastry.keySet()) {

            System.out.print("Punkt: " + (j + 1) + ", {");

            for (int i = 0; i < key.size() - 1; i++) {

                double wartosc = key.get(i);

                lasti = i;
                System.out.print(wartosc + ", ");
            }

            System.out.print(key.get(lasti + 1) + "}, ");

            System.out.print("K: " + klastry.get(key) + "\n");

            j++;
        }

    }


    public static double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

    public static double odlEuklesedesa(List<Double> x1, List<Double> x2) {

        double sum = 0.0;
        for (int i = 0; i < x1.size(); i++) {

            sum += (Math.pow(x1.get(i) - x2.get(i), 2));
        }
        sum = Math.sqrt(sum);

        return sum;
    }


    static Map<List<Double>, Integer> kSrednie(List<List<Double>> wszykiePkt, Map<Integer, List<Double>> centroids, int k) {

        Map<List<Double>, Integer> klastry = new HashMap<>();


        double odl;
        int kId = 0;

        int nrListy = 0;

        for (List<Double> lista : wszykiePkt) {
            double minimum = Double.MAX_VALUE;


            for (int i = 0; i < k; i++) {

                odl = odlEuklesedesa(centroids.get(i), lista);

                if (odl < minimum) {
                    minimum = odl;
                    kId = i;
                }

            }
            nrListy++;
            System.out.println("ID listy: " + nrListy + ", Lista: " + lista + ", ID klaster: " + kId);
            klastry.put(lista, kId);
        }

        return klastry;

    }


    public static List<Double> liczCentroid(List<List<Double>> doubleList, int listRoz) {

        int licz;
        double sum;

        List<Double> centroidy = new ArrayList<>();


        for (int i = 0; i < listRoz; i++) {

            sum = 0.0;
            licz = 0;

            for (List<Double> list : doubleList) {
                licz++;
                sum = sum + list.get(i);
            }

            double val = sum / licz;

            centroidy.add(val);
        }

        return centroidy;

    }


    static Map<Integer, List<Double>> nowyCentroid(List<List<Double>> wektory, int k) {

        Map<Integer, List<Double>> nowyCentroid = new HashMap<>();

        for (int i = 0; i < k; i++) {

            nowyCentroid.put(i, wektory.get((int) getRandomNumber(0, wektory.size() - 1)));
        }

        return nowyCentroid;
    }


    public void setK(int k) {
        this.k = k;
    }

    public int getK() {
        return k;
    }
}

