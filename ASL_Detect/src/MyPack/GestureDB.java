/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyPack;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author Ravi
 */
public class GestureDB implements Serializable{
    public Vector<SingleSample> db;

    public GestureDB() {
        db = new Vector<SingleSample>();
    }
    
}
