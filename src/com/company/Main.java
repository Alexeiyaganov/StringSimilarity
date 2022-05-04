package com.company;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.io.Writer;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class Main {


    // Реализация функции, которая проверяет уровень схожести двух строк
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0;}
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }


    // Реализация алгоритма Левештейна
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    // Функция, которая формирует массив с индексами из второго введенного массива, в соответствии с массивом первых введенных чисел
    // Индекс -1 соответствет тем элементам первого массива, для которого не нашлось пары из второго массива
    public static Integer [] make_indexes(int n, Double[] maximum, Double[][] matr_sim,  int m){
        Integer [] indexes=new Integer[maximum.length];
        for (int i=0; i<maximum.length; i++){
            indexes[i]=-1;
        }
        for (int i=0; i<n; i++) {
            double max = maximum[0];
            maximum[0]=0.0;
            int index = 0;
            for (int j = 0; j < maximum.length; j++) {
                if (maximum[j] > max) {
                    maximum[index]=max;
                    index = j;
                    max=maximum[j];
                    maximum[j]=0.0;
                    }
            }
            max=0;
            for (int k=0; k<m; k++){
                if(matr_sim[index][k]>max) {
                    max=matr_sim[index][k];
                    indexes[index]=k;
                }
            }

        }
        return indexes;
    }


    public static void main(String[] args) {
        String file_in = "input.txt";
        int n,m;
        String[]  nArray, mArray;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file_in));
            String firstLine = reader.readLine();
            n = Integer.parseInt(firstLine);
            nArray = new String[n];
            for(int i=0; i<n; i++){
                nArray[i]=reader.readLine();
            }
            m = Integer.parseInt(reader.readLine());
            mArray = new String[m];
            for(int i=0; i<m; i++){
                mArray[i]=reader.readLine();
            }

            Double [][] matr_sim=new Double[n][m];
            Double [] maximum=new Double[n];

            // создаем матрицу схожести и массив maximum, в котором для каждого элемента из первых введенных чисел записывается
            // максимальный уровень схожести, чтобы начинать сопоставления один к одному из списка  вторых введенных чисел по
            // убыванию уровня схожести
            for(int i=0; i< n; i++){
                maximum[i]=0.0;
                for(int j=0; j< m; j++){
                    matr_sim[i][j]= similarity(nArray[i], mArray[j]);
                    if(matr_sim[i][j]>maximum[i]) maximum[i]=matr_sim[i][j];
                }
            }
            Integer [] indexes=make_indexes(Math.min(n, m), maximum,matr_sim, m);
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("output.txt"), "utf-8"));

            for (int i=0; i<n; i++){
                writer.write(nArray[i] +":"+(indexes[i]==-1 ? "?" : mArray[indexes[i]])+"\n");
            }
            writer.close();

        } catch (IOException exp) {
            System.out.println(exp);
        }
    }
}
