/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leafdetection;

/**
 *
 * @author OorjaTech
 */
public class SingleDistanceFeature {

    public double d[];
    public double angle[];

    public SingleDistanceFeature() {
        d = new double[3];
        angle = new double[3];
    }

    public void print() {
        System.out.println("  " + d[0] + "  " + d[1] + "  " + d[2]);

    }
}
