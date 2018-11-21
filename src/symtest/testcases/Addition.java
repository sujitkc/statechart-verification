package testcases;

import java.util.Scanner;

public class Addition {

    public static void addition(){                  //  A

        while(true)                                 //  B
        {
            Scanner sc = new Scanner(System.in);    //  B
            int x = sc.nextInt();                   //  B
            int y = sc.nextInt();                   //  B

            int z = x + y;                          //  C

            if(z>10){                               //  D
                if(x>5){                            //  E
                    if(y<3){                        //  F
                        x = 6;                      //  G
                    }
                    if(y>5){                        //  H
                        x = 4;                      //  I
                    }
                }
            }
        }                                           //  W
    }                                               //

    public static void main( String[] args){
        addition();
    }

}
